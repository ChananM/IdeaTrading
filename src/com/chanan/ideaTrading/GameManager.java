package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;

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

	/**
	 * @param idea     The idea on which the order is put
	 * @param newOrder The new order
	 * @return A message describing success or failure of the process
	 */
	public synchronized String addOrder(String idea, Order newOrder) {
		String msg;
		Player requester = PlayerManager.getInstance().getPlayers().get(newOrder.getOwnerName());
		if ((newOrder.getPricePerShare() * newOrder.getShareAmount()) <= requester.getMoney()) {
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
		} else {
			msg = "Not enough money to fulfill the request";
		}
		return msg;
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
			} else {
				match.setShareAmount(match.getShareAmount() - newOrder.getShareAmount());
				double transactionAmount = newOrder.getShareAmount() * match.getPricePerShare();
				transact(newOrder.getIdea(), newOrder.getType(), requester, matchingOrderPlayer, transactionAmount,
						newOrder.getShareAmount());
				newOrder.setShareAmount(0);
			}
			// TODO: Send message to matching player his order was processed?
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

	public JSONObject getOrderMapJson() {
		JSONObject result = new JSONObject();
		for (Map.Entry<String, Map<OrderType, ArrayList<Order>>> entry : ordersMap.entrySet()) {
			JSONObject ordersList = OrderManager.getInstance().getOrdersJson(entry.getValue());
			result.putOpt(entry.getKey(), ordersList);
		}
		return result;
	}

}
