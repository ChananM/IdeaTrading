package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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
	
	private Map<String, ArrayList<Order>> ordersMap;
	
	public Map<String, ArrayList<Order>> getOrdersMap() {
		return ordersMap;
	}
	
	public void initGame(JSONObject config) {
		PlayerManager.getInstance().initPlayers(config);
		ordersMap = new HashMap<String, ArrayList<Order>>();
		List<Player> players = PlayerManager.getInstance().getPlayers();
		for(Player player : players) {
			getOrdersMap().put(player.getName(), new ArrayList<Order>());
		}
	}
	
	public void addOrder(String playerName, Order order) {
		ordersMap.get(playerName).add(order);
	}

	public JSONObject getOrderMapJson() {
		JSONObject result = new JSONObject();
		for(Map.Entry<String, ArrayList<Order>> entry : getOrdersMap().entrySet()) {
			JSONObject ordersList = OrderManager.getInstance().getOrdersJson(entry.getValue());
			result.putOpt(entry.getKey(), ordersList);
		}
		return result;
	}
	
}
