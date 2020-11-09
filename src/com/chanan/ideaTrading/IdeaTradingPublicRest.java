package com.chanan.ideaTrading;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/orders")
public class IdeaTradingPublicRest {
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrders() {
		JSONObject jsonObject = OrderManager.getInstance().getOrdersJson();
		return Response.status(200).entity(jsonObject.toMap()).build();
	}
	
	@GET
	@Path("/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrder(@PathParam("orderId") String orderId) {
		JSONObject object = OrderManager.getInstance().getOrdersJson();
		return Response.status(200).entity(object.getJSONObject(orderId).toMap()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response putOrder(String orderDetails) {
		Order newOrder = new Order(new JSONObject(orderDetails));
		OrderManager.getInstance().addOrder(newOrder);
		return Response.status(200).entity("Order Placed Successfuly!").build();
	}
}
