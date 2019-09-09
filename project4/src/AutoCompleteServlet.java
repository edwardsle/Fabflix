

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mysql.jdbc.PreparedStatement;

import Helpers.InvalidInputException;
import Helpers.RequestHelper;
import Helpers.ResponseHelper;

/**
 * Servlet implementation class AutoCompleteServlet
 */
@WebServlet("/api/autocomplete")
public class AutoCompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject json = null;
		
		try {
			json = parser.parse(data).getAsJsonObject();
		}
		catch (Exception e)
		{
			System.out.println("Not json");
		}
		
		String keyword = null;
		String keywordFuzzy = null;
		Integer offset = null;
		Integer limit = null;
		try {
			keyword = RequestHelper.getRequiredParam("keyword", json, request);
			keywordFuzzy = keyword;
			keyword = "+" + keyword.replaceAll(" ","* +") + "*";
		} catch (InvalidInputException e) {
			System.out.println("Not valid");
			return;
		}	
		
		try {
			offset = RequestHelper.getParamAsInt("offset", json, request);
			limit = RequestHelper.getParamAsInt("limit", json, request);
		} catch (InvalidInputException e) {
		
		}
		
		if (offset == null)
			offset = 0;
		if (limit == null)
			limit = 10;
					
		System.out.println(keyword);
		
		try {
			Connection connection = dataSource.getConnection();
			
			Statement statement = connection.createStatement();
			
			// SQL Query: SELECT title from movies WHERE MATCH (title) AGAINST ('+star* +d*' in boolean mode) order by title asc limit 10;
			
			String query = "SELECT id, title from movies WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset;
			
			
			//FuzzySearch
			//String query = "SELECT id, title from movies WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) or edth(title, '" +keywordFuzzy+ "', 2) ORDER BY title ASC LIMIT 10";
			
			System.out.println(query);
			
			ResultSet rs = statement.executeQuery(query);
			
			ArrayList<JsonObject> suggestions = new ArrayList<JsonObject>();
			
			while (rs.next())
			{
				JsonObject obj = new JsonObject();
				obj.addProperty("value", rs.getString("title"));
				obj.addProperty("data", rs.getString("id"));
				
				suggestions.add(obj);
			}
			
			JsonObject jsonResponse = new JsonObject();
			Gson gson = new Gson();
					
			jsonResponse.add("suggestions", gson.toJsonTree(suggestions));
			
			ResponseHelper.sendObjectAsJson(jsonResponse, response);
			
			System.out.println(jsonResponse.toString());
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
