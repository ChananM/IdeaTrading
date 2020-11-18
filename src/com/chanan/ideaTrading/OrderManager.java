package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;

public class OrderManager {

	// Singleton implementation

	private static OrderManager orderManager;
	private static Long nextId = 1l;

	public static OrderManager getInstance() {
		if (orderManager == null) {
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
		for (Map.Entry<OrderType, ArrayList<Order>> orderType : orderList.entrySet()) {
			obj.putOpt(orderType.getKey().name(), getOrdersJson(orderType.getValue()));
		}
		return obj;
	}

	public JSONObject getOrdersJson(List<Order> ordersList) {
		JSONObject ordersObject = new JSONObject();
		for (Order o : ordersList) {
			JSONObject orderObject = new JSONObject();
			orderObject.put("id", o.getId());
			orderObject.put("type", o.getType());
			orderObject.put("idea", o.getIdea());
			orderObject.put("pricePerShare", o.getPricePerShare());
			orderObject.put("shareAmount", o.getShareAmount());
			ordersObject.putOpt(o.getId().toString(), orderObject);
		}
		return ordersObject;
	}
}
