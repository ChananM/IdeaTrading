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

import com.chanan.webSocket.SessionManager.SessionInstance;

@ServerEndpoint("/notifications/{username}")
public class NotificationSocket {

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		System.out.println("Opened notifications socket for " + username + ". Session id: " + session.getId());
		SessionManager.getInstance(SessionInstance.NOTIFICATIONS).addSession(username, session);
		session.getBasicRemote().sendText(gameData.toString());
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("username") String username)
			throws IOException, EncodeException {
		System.out.println(
				"Notification acknowledged from " + username + ", session id: " + session.getId() + ": " + message);
		// We have no need to get a message from client, only send messages to them.
	}

	@OnClose
	public void onClose(Session session, @PathParam("username") String username) throws IOException {
		System.out.println("Closed otifications socket from " + username + ". Session id: " + session.getId());
		SessionManager.getInstance(SessionInstance.NOTIFICATIONS).removeSession(username);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Error registered from notification socket: " + session.getId());
		// TODO: Handle error
	}

}
