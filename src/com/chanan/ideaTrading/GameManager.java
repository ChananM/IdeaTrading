package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;

public class GameManager {

	// Singleton implementation
	
	private static GameManager gameManager;

	public static GameManager getInstance() {
		if(gameManager == null) {
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
		List<Player> players = PlayerManager.getInstance().getPlayers();
		for(Player player : players) {
			ordersMap.put(player.getName(), new HashMap<OrderType, ArrayList<Order>>());
			ordersMap.get(player.getName()).put(OrderType.BUY, new ArrayList<Order>());
			ordersMap.get(player.getName()).put(OrderType.SELL, new ArrayList<Order>());
		}
	}
	
	public void addOrder(String playerName, Order order) {
		ordersMap.get(playerName).get(order.getType()).add(order);
	}

	public JSONObject getOrderMapJson() {
		JSONObject result = new JSONObject();
		for(Map.Entry<String, Map<OrderType, ArrayList<Order>>> entry : ordersMap.entrySet()) {
			JSONObject ordersList = OrderManager.getInstance().getOrdersJson(entry.getValue());
			result.putOpt(entry.getKey(), ordersList);
		}
		return result;
	}
	
}
