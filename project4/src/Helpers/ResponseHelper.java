package Helpers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResponseHelper {
	public static JsonObject composeMessageAsJson(boolean success, String message)
	{
		JsonObject json = new JsonObject();
		json.addProperty("success", success);
		json.addProperty("message", message);
		return json;
	}
	
	public static void sendMessageAsJson(boolean success, String message, HttpServletResponse response)
	{
		try {
			response.getWriter().print(composeMessageAsJson(success, message).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(500);
			e.printStackTrace();
		}
	}
	
	public static <T> void sendMessageAsJson(boolean success, String message, T obj, HttpServletResponse response)
	{
		JsonObject msg = composeMessageAsJson(success, message);
		Gson gson = new Gson();
		JsonElement payload = gson.toJsonTree(obj);
		msg.add("payload", payload);
		sendObjectAsJson(msg, response);
	}
	
	public static <T> void sendObjectAsJson(T obj, HttpServletResponse response)
	{
		Gson gson = new Gson();
		String json = gson.toJson(obj);		
		
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(500);
		}
	}
}
