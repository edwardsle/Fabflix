

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
		PrintWriter out = response.getWriter();
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
			// the following few lines are for connection pooling
            // Obtain our environment naming context

            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");

            if (ds == null)
                out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
	            
			//Connection connection = dataSource.getConnection();
			
			PreparedStatement statement = dbcon.prepareStatement("SELECT id, title from movies WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset);
			
			//String query = "SELECT id, title from movies WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset;
			
			
			//FuzzySearch
			//String query = "SELECT id, title from movies WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) or edth(title, '" +keywordFuzzy+ "', 2) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset;
			
			System.out.println(statement);
			
			ResultSet rs = statement.executeQuery();
			
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
			dbcon.close();
			
		} catch (SQLException | NamingException e) {
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
