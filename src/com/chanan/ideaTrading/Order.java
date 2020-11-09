package com.chanan.ideaTrading;

import org.json.JSONObject;

public class Order {
	
	private Long id;
	private OrderType type;
	private double price;
	
	
	public enum OrderType{
		BUY, SELL
	}
	
	public Order(JSONObject jsonObject) {
		this.id = OrderManager.getNextOrderId();
		this.type = OrderType.valueOf(jsonObject.getString("orderType"));
		this.price = jsonObject.getDouble("price");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
