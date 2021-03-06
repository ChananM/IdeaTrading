package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;
import com.chanan.webSocket.SessionManager;
import com.chanan.webSocket.SessionManager.SessionInstance;

public class GameManager {

	// Singleton implementation

	private static GameManager gameManager;

	public static GameManager getInstance() {
		if (gameManager == null) {
			gameManager = new GameManager();
		}
		return gameManager;
	}

	// Class Implementation

	private boolean gameStatus = false;

	// This maps playerName to a map of order type to order
	private Map<String, Map<OrderType, ArrayList<Order>>> ordersMap;

	public Map<String, Map<OrderType, ArrayList<Order>>> getOrdersMap() {
		return ordersMap;
	}

	public void initGame(JSONObject config) {
		PlayerManager.getInstance().initPlayers(config);
		ordersMap = new HashMap<String, Map<OrderType, ArrayList<Order>>>();
		Map<String, Player> players = PlayerManager.getInstance().getPlayers();
		for (Player player : players.values()) {
			ordersMap.put(player.getName(), new HashMap<OrderType, ArrayList<Order>>());
			ordersMap.get(player.getName()).put(OrderType.BUY, new ArrayList<Order>());
			ordersMap.get(player.getName()).put(OrderType.SELL, new ArrayList<Order>());
		}
	}

	public void setStatus(boolean status) {
		this.gameStatus = status;
	}

	public void addMoneyToPlayers(double amount) {
		Collection<Player> players = PlayerManager.getInstance().getPlayers().values();
		for (Player p : players) {
			p.setMoney(p.getMoney() + amount);
		}
		SessionManager.getInstance(SessionInstance.NOTIFICATIONS)
				.broadcast("You just received additional " + amount + " GBP to your wallet! Go out and spend it!");
	}

	public void finalizeGame(JSONObject winners) {
		Map<String, Player> players = PlayerManager.getInstance().getPlayers();
		String firstPlace = winners.getString("firstPlace"), secondPlace = winners.getString("secondPlace"),
				thirdPlace = winners.getString("thirdPlace");
		if (players.get(firstPlace) != null && players.get(secondPlace) != null && players.get(thirdPlace) != null) {
			for (Player p : players.values()) {
				Map<String, Integer> playerStocks = p.getStocks();
				for (Map.Entry<String, Integer> playerStock : playerStocks.entrySet()) {
					if (playerStock.getKey().equals(firstPlace)) {
						p.setMoney(p.getMoney() + (playerStock.getValue() * 10));
					} else if (playerStock.getKey().equals(secondPlace)) {
						p.setMoney(p.getMoney() + (playerStock.getValue() * 5));
					} else if (playerStock.getKey().equals(thirdPlace)) {
						p.setMoney(p.getMoney() + (playerStock.getValue() * 2));
					} else {
						p.setMoney(p.getMoney() + (playerStock.getValue() * 1));
					}
				}
				p.getStocks().clear();
				p.getOpenOrders().clear();
				ordersMap.get(p.getName()).get(OrderType.BUY).clear();
				ordersMap.get(p.getName()).get(OrderType.SELL).clear();
			}
			broadcastGameData();
		}
	}

	/**
	 * @param idea     The idea on which the order is put
	 * @param newOrder The new order
	 * @return A message describing success or failure of the process
	 */
	public synchronized String addOrder(String idea, Order newOrder) {
		String msg;
		Player requester = PlayerManager.getInstance().getPlayers().get(newOrder.getOwnerName());
		if (!this.gameStatus) {
			return "The game has been stopped by admin, orders can't be placed";
		}
		boolean newOrderCondition = getOrderCondition(requester, newOrder);
		if (newOrderCondition) {
			List<Order> matchingOrders = getMatchingOrders(newOrder);
			if (matchingOrders.size() > 0) {
				performTransaction(requester, newOrder, matchingOrders);
				if (newOrder.getShareAmount() > 0) {
					saveOrder(idea, requester, newOrder);
					msg = "Order submitted and processed partially.";
				} else {
					msg = "Order submitted and processed fully";
				}
			} else {
				saveOrder(idea, requester, newOrder);
				msg = "Order submitted successfully";
			}
			broadcastGameData();
		} else {
			msg = "Not enough money or shares to fulfill the request";
		}
		return msg;
	}

	private boolean getOrderCondition(Player requester, Order newOrder) {
		if (newOrder.getType() == OrderType.BUY) {
			double requesterAggregatedDebt = requester.getOpenOrders().stream()
					.filter(openOrder -> openOrder.getType() == OrderType.BUY)
					.mapToDouble(openOrder -> openOrder.getPricePerShare() * openOrder.getShareAmount()).sum();
			return (newOrder.getPricePerShare() * newOrder.getShareAmount()) <= (requester.getMoney()
					- requesterAggregatedDebt);
		} else {
			int requesterAggregatedDebt = requester.getOpenOrders().stream()
					.filter(openOrder -> openOrder.getType() == OrderType.SELL
							&& openOrder.getIdea().equals(newOrder.getIdea()))
					.mapToInt(openOrder -> openOrder.getShareAmount()).sum();
			return newOrder.getShareAmount() <= (requester.getStocks().get(newOrder.getIdea()) == null ? 0
					: requester.getStocks().get(newOrder.getIdea())) - requesterAggregatedDebt;
		}
	}

	private List<Order> getMatchingOrders(Order order) {
		return ordersMap.get(order.getIdea()).get(order.getType() == OrderType.BUY ? OrderType.SELL : OrderType.BUY)
				.stream().sorted().filter((possibleMatch) -> {
					return order.getType() == OrderType.BUY
							? possibleMatch.getPricePerShare() <= order.getPricePerShare()
							: possibleMatch.getPricePerShare() >= order.getPricePerShare();
				}).collect(Collectors.toList());
	}

	private void performTransaction(Player requester, Order newOrder, List<Order> orderMatches) {
		Iterator<Order> it = orderMatches.iterator();
		while (it.hasNext() && newOrder.getShareAmount() > 0) {
			Order match = it.next();
			Player matchingOrderPlayer = PlayerManager.getInstance().getPlayers().get(match.getOwnerName());
			if (newOrder.getShareAmount() >= match.getShareAmount()) {
				matchingOrderPlayer.getOpenOrders().remove(match);
				ordersMap.get(match.getIdea()).get(match.getType()).remove(match);
				double transactionAmount = match.getShareAmount() * match.getPricePerShare();
				transact(newOrder.getIdea(), newOrder.getType(), requester, matchingOrderPlayer, transactionAmount,
						match.getShareAmount());
				newOrder.setShareAmount(newOrder.getShareAmount() - match.getShareAmount());
				SessionManager.getInstance(SessionInstance.NOTIFICATIONS).sendMessage(match.getOwnerName(),
						"Your " + match.getType().toString().toLowerCase() + " order on " + match.getIdea()
								+ " was matched fully");
			} else {
				match.setShareAmount(match.getShareAmount() - newOrder.getShareAmount());
				double transactionAmount = newOrder.getShareAmount() * match.getPricePerShare();
				transact(newOrder.getIdea(), newOrder.getType(), requester, matchingOrderPlayer, transactionAmount,
						newOrder.getShareAmount());
				newOrder.setShareAmount(0);
				SessionManager.getInstance(SessionInstance.NOTIFICATIONS).sendMessage(match.getOwnerName(),
						"Your " + match.getType().toString().toLowerCase() + " order on " + match.getIdea()
								+ " was matched partially");
			}
		}
	}

	private void transact(String idea, OrderType type, Player requester, Player matcher, double amount, int shares) {
		if (type == OrderType.BUY) {
			matcher.setMoney(matcher.getMoney() + amount);
			matcher.getStocks().put(idea, matcher.getStocks().get(idea) - shares);
			requester.setMoney(requester.getMoney() - amount);
			requester.getStocks().put(idea,
					requester.getStocks().get(idea) == null ? shares : requester.getStocks().get(idea) + shares);
		} else {
			matcher.setMoney(matcher.getMoney() - amount);
			matcher.getStocks().put(idea,
					matcher.getStocks().get(idea) == null ? shares : matcher.getStocks().get(idea) + shares);
			requester.setMoney(requester.getMoney() + amount);
			requester.getStocks().put(idea, requester.getStocks().get(idea) - shares);
		}
	}

	private void saveOrder(String idea, Player requester, Order newOrder) {
		ordersMap.get(idea).get(newOrder.getType()).add(newOrder);
		requester.getOpenOrders().add(newOrder);
	}

	private void broadcastGameData() {
		JSONObject ordersMapJson = getOrderMapJson();
		Collection<Player> players = PlayerManager.getInstance().getPlayers().values();
		for (Player p : players) {
			JSONObject playerDataJson = PlayerManager.getInstance().getPlayerData(p.getName(), p.getPassword());
			JSONObject gameData = new JSONObject();
			gameData.putOpt("playerData", playerDataJson);
			gameData.putOpt("orders", ordersMapJson);
			SessionManager.getInstance(SessionInstance.IDEAS).sendMessage(p.getName(), gameData.toString());
		}
	}

	public synchronized void removeOrder(Player requester, String idea, String orderId, OrderType orderType) {
		Order orderToRemove = new Order(Long.valueOf(orderId));
		ordersMap.get(idea).get(orderType).remove(orderToRemove);
		requester.getOpenOrders().remove(orderToRemove);
		broadcastGameData();
	}

	public JSONObject getGameData(String playerName, String password) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.putOpt("orders", getOrderMapJson());
		if ("admin".equals(playerName) && PlayerManager.getInstance().isAdmin(password)) {
			jsonObject.putOpt("playerData", PlayerManager.getInstance().getPlayersData());
		} else {
			jsonObject.putOpt("playerData", PlayerManager.getInstance().getPlayerData(playerName, password));
		}
		return jsonObject;
	}

	public JSONObject getOrderMapJson() {
		JSONObject result = new JSONObject();
		for (Map.Entry<String, Map<OrderType, ArrayList<Order>>> entry : ordersMap.entrySet()) {
			JSONObject ordersList = OrderManager.getInstance().getOrdersJson(entry.getValue());
			result.putOpt(entry.getKey(), ordersList);
		}
		return result;
	}

}
