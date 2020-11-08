package com.chanan.ideaTrading;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/pub")
public class IdeaTradingPublicRest {
	
	private static int number = 0;
	
	@GET
	@Path("/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion(@PathParam("num") Long number, @QueryParam("mult") Long mult) {
		this.number++;
		Logger x = Logger.getLogger("test");
		x.info("TEST-------------------------------------");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("number", this.number);
		return Response.status(200).entity(jsonObject.toMap()).build();
	}
}
