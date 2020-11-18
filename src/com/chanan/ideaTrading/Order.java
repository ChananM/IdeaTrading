package com.chanan.ideaTrading;

import org.json.JSONObject;

public class Order implements Comparable<Order> {

	private Long id;
	private String idea;
	private String ownerName;
	private OrderType type;
	private int shareAmount;
	private double pricePerShare;

	public enum OrderType {
		BUY, SELL
	}

	public Order(Long orderId) {
		this.id = orderId;
	}

	public Order(JSONObject jsonObject) {
		this.id = OrderManager.getNextOrderId();
		this.idea = jsonObject.getString("idea");
		this.ownerName = jsonObject.getString("ownerName");
		this.type = OrderType.valueOf(jsonObject.getString("orderType"));
		this.shareAmount = jsonObject.getInt("shareAmount");
		this.pricePerShare = jsonObject.getDouble("pricePerShare");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdea() {
		return idea;
	}

	public void setIdea(String idea) {
		this.idea = idea;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public int getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(int shareAmount) {
		this.shareAmount = shareAmount;
	}

	public double getPricePerShare() {
		return pricePerShare;
	}

	public void setPricePerShare(double price) {
		this.pricePerShare = price;
	}

	@Override
	public boolean equals(Object obj) {
		Order other = (Order) obj;
		return this.id == other.id;
	}

	@Override
	public int compareTo(Order o) {
		if (o.getType() == OrderType.SELL) {
			return Double.compare(this.getPricePerShare(), o.getPricePerShare());
		} else {
			return Double.compare(o.getPricePerShare(), this.getPricePerShare());
		}
	}
}
