package com.chanan.ideaTrading;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
	public Response initGame(@QueryParam("password") String password, String gameConfig) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			JSONObject gameConfiguration = new JSONObject(gameConfig);
			GameManager.getInstance().initGame(gameConfiguration);
			return Response.status(200).entity("Game initiated successfuly!").build();
		} else {
			return Response.status(403).build();
		}
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
		JSONObject gameData = GameManager.getInstance().getGameData(playerName, password);
		return Response.status(200).entity(gameData.toMap()).build();
	}

	@POST
	@Path("/game/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGameData(@QueryParam("password") String password, @QueryParam("status") boolean status) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			GameManager.getInstance().setStatus(status);
			return Response.status(200).build();
		} else {
			return Response.status(403).build();
		}
	}

	@POST
	@Path("/game/addMoney")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMoneyToPlayers(@QueryParam("password") String password, @QueryParam("amount") double amount) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			GameManager.getInstance().addMoneyToPlayers(amount);
			return Response.status(200).build();
		} else {
			return Response.status(403).build();
		}
	}

	@POST
	@Path("/game/finalize")
	@Produces(MediaType.APPLICATION_JSON)
	public Response finalizeGame(@QueryParam("password") String password, String winners) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			JSONObject jsonObject = new JSONObject(winners);
			GameManager.getInstance().finalizeGame(jsonObject);
			return Response.status(200).build();
		} else {
			return Response.status(403).build();
		}
	}

	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrdersMap(@QueryParam("password") String password) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			JSONObject jsonObject = GameManager.getInstance().getOrderMapJson();
			return Response.status(200).entity(jsonObject.toMap()).build();
		} else {
			return Response.status(403).build();
		}
	}

	@GET
	@Path("/orders/{playerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrders(@PathParam("playerName") String playerName,
			@QueryParam("password") String password) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
			JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
			return Response.status(200).entity(playerOrdersJson.toMap()).build();
		} else {
			return Response.status(403).build();
		}
	}

	@GET
	@Path("/orders/{playerName}/{orderType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerOrderByType(@PathParam("playerName") String playerName,
			@PathParam("orderType") String orderType, @QueryParam("password") String password) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
			JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
			return Response.status(200).entity(playerOrdersJson.getJSONObject(orderType).toMap()).build();
		} else {
			return Response.status(403).build();
		}
	}

	@GET
	@Path("/orders/{playerName}/{orderType}/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrder(@PathParam("playerName") String playerName, @PathParam("orderType") String orderType,
			@PathParam("orderId") String orderId, @QueryParam("password") String password) {
		if (PlayerManager.getInstance().isAdmin(password)) {
			Map<OrderType, ArrayList<Order>> playerOrders = GameManager.getInstance().getOrdersMap().get(playerName);
			JSONObject playerOrdersJson = OrderManager.getInstance().getOrdersJson(playerOrders);
			return Response.status(200).entity(playerOrdersJson.getJSONObject(orderType).getJSONObject(orderId).toMap())
					.build();
		} else {
			return Response.status(403).build();
		}
	}

	@DELETE
	@Path("/orders/{idea}/{orderType}/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrder(@PathParam("idea") String idea, @PathParam("orderType") String orderType,
			@PathParam("orderId") String orderId, @QueryParam("playerName") String playerName,
			@QueryParam("password") String password) {
		if (PlayerManager.getInstance().login(playerName, password)) {
			Player requester = PlayerManager.getInstance().getPlayers().get(playerName);
			GameManager.getInstance().removeOrder(requester, idea, orderId, OrderType.valueOf(orderType));
			return Response.status(200).build();
		} else {
			return Response.status(403).build();
		}
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
