package com.chanan.webSocket;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.chanan.ideaTrading.GameManager;
import com.chanan.ideaTrading.PlayerManager;
import com.chanan.webSocket.SessionManager.SessionInstance;

@ServerEndpoint("/ideasSocket/{username}")
public class IdeasSocket {

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		System.out.println("Opened connection for " + username + ". Session id: " + session.getId());
		SessionManager.getInstance(SessionInstance.IDEAS).addSession(username, session);
		JSONObject gameData = GameManager.getInstance().getGameData(username,
				PlayerManager.getInstance().getPlayers().get(username).getPassword());
		session.getBasicRemote().sendText(gameData.toString());
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("username") String username)
			throws IOException, EncodeException {
		System.out.println("Message from " + username + ", session id: " + session.getId() + ": " + message);
		// We have no need to get a message from client, only send messages to them.
	}

	@OnClose
	public void onClose(Session session, @PathParam("username") String username) throws IOException {
		System.out.println("Closed connection from " + username + ". Session id: " + session.getId());
		SessionManager.getInstance(SessionInstance.IDEAS).removeSession(username);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Error registered from " + session.getId());
		// TODO: Handle error
	}

}
