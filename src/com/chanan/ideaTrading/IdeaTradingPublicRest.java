package com.chanan.ideaTrading;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/")
public class IdeaTradingPublicRest {
		
	@POST
	@Path("/game")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response initGame(String gameConfig) {
		JSONObject gameConfiguration = new JSONObject(gameConfig);
		GameManager.getInstance().initGame(gameConfiguration);
		return Response.status(200).entity("Game initiated successfuly!").build();
	}
	
	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrdersMap() {
		JSONObject jsonObject = GameManager.getInstance().getOrderMapJson();
		return Response.status(200).entity(jsonObject.toMap()).build();
	}
	
	@GET
	@Path("/orders/{playerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrders(@PathParam("playerName") String playerName) {
		ArrayList<Order> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
		JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
		return Response.status(200).entity(playerOrdersJson.toMap()).build();
	}
	
	@GET
	@Path("/orders/{playerName}/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrder(@PathParam("playerName") String playerName, @PathParam("orderId") String orderId) {
		ArrayList<Order> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
		JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
		return Response.status(200).entity(playerOrdersJson.getJSONObject(orderId).toMap()).build();
	}
	
	@POST
	@Path("/orders/{playerName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response putOrder(@PathParam("playerName") String playerName, String orderDetails) {
		Order newOrder = new Order(new JSONObject(orderDetails));
		GameManager.getInstance().addOrder(playerName, newOrder);
		return Response.status(200).entity("Order Placed Successfuly!").build();
	}
}
