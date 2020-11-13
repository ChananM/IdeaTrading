package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;

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
	
	public JSONObject getOrdersJson(Map<OrderType, ArrayList<Order>> orderList) {
		JSONObject obj = new JSONObject();
		for(Map.Entry<OrderType, ArrayList<Order>> orderType : orderList.entrySet()) {	
			JSONObject ordersList = new JSONObject();
			for(Order o : orderType.getValue()) {
				JSONObject orderObject = new JSONObject();
				orderObject.put("type", o.getType());
				orderObject.put("price", o.getPrice());
				ordersList.putOpt(o.getId().toString(), orderObject);
			}
			obj.putOpt(orderType.getKey().name(), ordersList);
		}
		return obj;
	}
}
