package com.chanan.webSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

public class SessionManager {

	public enum SessionInstance {
		IDEAS, NOTIFICATIONS
	}

	// Multi-Singleton implementation. There are multiple sessions of this manager
	// each containing a list of sessions for different web sockets

	private static Map<SessionInstance, SessionManager> sessionManager = new HashMap<SessionInstance, SessionManager>();

	public static SessionManager getInstance(SessionInstance instance) {
		if (sessionManager.get(instance) == null) {
			sessionManager.put(instance, new SessionManager());
		}
		return sessionManager.get(instance);
	}

	// Class implementation

	private Map<String, Session> sessions = new HashMap<String, Session>();

	public void addSession(String playerName, Session session) {
		sessions.put(playerName, session);
	}

	public Session getSession(String playerName) {
		return sessions.get(playerName);
	}

	public void removeSession(String playerName) {
		sessions.remove(playerName);
	}

	public boolean isConnected(String playerName) {
		return sessions.containsKey(playerName);
	}

	public void sendMessage(String player, String message) {
		try {
			if (isConnected(player)) {
				sessions.get(player).getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			System.out.println("Failed sending message to session " + player);
			e.printStackTrace();
		}
	}

	public void broadcast(String message) {
		for (Session s : sessions.values()) {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				System.out.println("Failed sending message to session " + s.getId());
				e.printStackTrace();
			}
		}
	}
}
