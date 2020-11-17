package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

	private String name;
	private String password;
	private double money;
	private Map<String, Integer> stocks;
	private List<Order> openOrders;

	public Player(String name, String password, double money) {
		this.name = name;
		this.password = password;
		this.money = money;
		this.stocks = new HashMap<>(Map.of(name, 100));
		this.openOrders = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Map<String, Integer> getStocks() {
		return stocks;
	}

	public void setStocks(Map<String, Integer> stocks) {
		this.stocks = stocks;
	}

	public List<Order> getOpenOrders() {
		return openOrders;
	}

	public void setOpenOrders(List<Order> openOrders) {
		this.openOrders = openOrders;
	}

}
