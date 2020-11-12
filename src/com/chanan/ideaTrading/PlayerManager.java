package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class PlayerManager {
	
	// Singleton implementation
	
	private static PlayerManager playerManager;

	public static PlayerManager getInstance() {
		if(playerManager == null) {
			playerManager = new PlayerManager();
		}
		return playerManager;
	}
	
	
	// Class Implementation
	
	private List<Player> players;
	
	public void initPlayers(JSONObject config) {
		players = new ArrayList<Player>();
		for(String playerName : config.keySet()) {
			JSONObject playerConfig = config.getJSONObject(playerName);
			getPlayers().add(new Player(playerName, playerConfig.getString("password"), playerConfig.getDouble("money")));
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

}
