package com.chanan.ideaTrading;

import java.util.ArrayList;

import org.json.JSONObject;

public class OrderManager {
	
	// Singleton implementation
	
	private static OrderManager orderManager;
	private static Long nextId = 1l;

	public static OrderManager getInstance() {
		if(orderManager == null) {
			orderManager = new OrderManager();
		}
		return orderManager;
	}
	
	public static Long getNextOrderId() {
		return nextId++;
	}
	
	// Class Implementation
	
	public JSONObject getOrdersJson(ArrayList<Order> orderList) {
		JSONObject obj = new JSONObject();
		for(Order o : orderList) {
			JSONObject orderObject = new JSONObject();
			orderObject.put("type", o.getType());
			orderObject.put("price", o.getPrice());
			obj.putOpt(o.getId().toString(), orderObject);
		}
		return obj;
	}
}
