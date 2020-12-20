package com.chanan.initialization;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import com.chanan.ideaTrading.GameManager;

public class IdeaTradingDataProvider implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String initJson = "{\"Chanan\":{\"password\":\"imbatman\",\"money\":100},\"Yotam\":{\"password\":\"imrobin\",\"money\":100},\"Mordechai\":{\"password\":\"imjoker\",\"money\":100},\"Inbal\":{\"password\":\"imbatman\",\"money\":100},\"Shoul\":{\"password\":\"imrobin\",\"money\":100},\"Arie\":{\"password\":\"imjoker\",\"money\":100},\"Rivka\":{\"password\":\"imbatman\",\"money\":100},\"Sharon\":{\"password\":\"imrobin\",\"money\":100},\"Nir\":{\"password\":\"imjoker\",\"money\":100},\"Noam\":{\"password\":\"imbatman\",\"money\":100},\"Yael\":{\"password\":\"imrobin\",\"money\":100},\"Moran\":{\"password\":\"imjoker\",\"money\":100}}";
		GameManager.getInstance().initGame(new JSONObject(initJson));
		GameManager.getInstance().setStatus(true);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Notification that the servlet context is about to be shut down.
	}
}
