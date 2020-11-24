package com.chanan.ideaTrading;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class PlayerManager {

	// Singleton implementation

	private static PlayerManager playerManager;

	public static PlayerManager getInstance() {
		if (playerManager == null) {
			playerManager = new PlayerManager();
		}
		return playerManager;
	}

	// Class Implementation

	private Map<String, Player> players;

	public Map<String, Player> getPlayers() {
		return players;
	}

	public void initPlayers(JSONObject config) {
		players = new HashMap<String, Player>();
		for (String playerName : config.keySet()) {
			JSONObject playerConfig = config.getJSONObject(playerName);
			getPlayers().put(playerName,
					new Player(playerName, playerConfig.getString("password"), playerConfig.getDouble("money")));
		}
	}

	public boolean login(String playerName, String password) {
		try {
			return players.get(playerName).getPassword().equals(password);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAdmin(String password) {
		return "Crs5p]zN?7QFw>GJ".equals(password);
	}

	public JSONObject getPlayersData() {
		JSONObject obj = new JSONObject();
		for (Player p : players.values()) {
			obj.put(p.getName(), getPlayerData(p.getName(), p.getPassword()));
		}
		return obj;
	}

	public JSONObject getPlayerData(String playerName, String password) {
		JSONObject obj = new JSONObject();
		if (login(playerName, password)) {
			Player p = players.get(playerName);
			obj.put("money", p.getMoney());
			obj.put("openOrders", OrderManager.getInstance().getOrdersJson(p.getOpenOrders()));
			obj.put("stocks", p.getStocks());
		}
		return obj;
	}

}
