package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.chanan.ideaTrading.Order.OrderType;

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
	@Path("/game/login")
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(@QueryParam("playerName") String playerName, @QueryParam("password") String password) {
		boolean result = PlayerManager.getInstance().login(playerName, password);
		return Response.status(200).entity(result).build();
	}

	@GET
	@Path("/game/data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGameData(@QueryParam("playerName") String playerName, @QueryParam("password") String password) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.putOpt("orders", GameManager.getInstance().getOrderMapJson());
		jsonObject.putOpt("playerData", PlayerManager.getInstance().getPlayerData(playerName, password));
		return Response.status(200).entity(jsonObject.toMap()).build();
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
		Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
		JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
		return Response.status(200).entity(playerOrdersJson.toMap()).build();
	}

	@GET
	@Path("/orders/{playerName}/{orderType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrderByType(@PathParam("playerName") String playerName,
			@PathParam("orderType") String orderType) {
		Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
		JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
		return Response.status(200).entity(playerOrdersJson.getJSONObject(orderType).toMap()).build();
	}

	@GET
	@Path("/orders/{playerName}/{orderType}/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrderByType(@PathParam("playerName") String playerName,
			@PathParam("orderType") String orderType, @PathParam("orderId") String orderId) {
		Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
		JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
		return Response.status(200).entity(playerOrdersJson.getJSONObject(orderType).getJSONObject(orderId).toMap())
				.build();
	}

	@POST
	@Path("/orders/{idea}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response putOrder(@PathParam("idea") String idea, String orderDetails) {
		JSONObject json = new JSONObject(orderDetails);
		if (PlayerManager.getInstance().login(json.getString("playerName"), json.getString("password"))) {
			Order newOrder = new Order(json);
			String msg = GameManager.getInstance().addOrder(idea, newOrder);
			return Response.status(200).entity(msg).build();
		} else {
			return Response.status(200).entity("Order failed!\nWrong username or password").build();
		}
	}
}
