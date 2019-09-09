package Helpers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

public class RequestHelper {
	public static String getParam(String key, JsonObject json, HttpServletRequest request)
	{
		if (request.getParameter(key) != null && !request.getParameter(key).isEmpty())
			return request.getParameter(key);
		
		if (json != null && json.get(key) != null && !json.get(key).getAsString().isEmpty())
		{
			return json.get(key).getAsString();
		}
		
		return null;
	}
	
	public static Integer getParamAsInt(String key, JsonObject json, HttpServletRequest request) throws InvalidInputException
	{
		String strVal = getParam(key, json, request);
		
		if (strVal == null)
			return null;
		
		return Integer.parseInt(strVal);
	}
	
	public static int getRequiredParamAsInt(String key, JsonObject json, HttpServletRequest request) throws InvalidInputException
	{
		Integer value = getParamAsInt(key, json, request);
		
		if (value == null)
			throw new RequiredInputException(key);
		
		return value;
	}
	
	public static Integer getParamAsPositiveInt(String key, JsonObject json, HttpServletRequest request) throws InvalidInputException
	{
		Integer value = getParamAsInt(key, json, request);
		if (value != null && value <= 0)
			throw new InvalidNumberException(key, "positive");
		
		return value;
	}
	
	public static int getRequiredParamAsPositiveInt(String key, JsonObject json, HttpServletRequest request) throws InvalidInputException
	{
		int value = getRequiredParamAsInt(key, json, request);
		
		if (value <= 0)
			throw new InvalidNumberException(key, "positive");
		
		return value;
	}
	
	public static String getRequiredParam(String key, JsonObject json, HttpServletRequest request) throws InvalidInputException
	{
		String param = getParam(key, json, request);
		
		if (param == null)
			throw new RequiredInputException(key);
		
		return param; 
	}
}
