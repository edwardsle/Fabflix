import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Models.Customer;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	PrintWriter out = response.getWriter();
    	String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject js = parser.parse(data).getAsJsonObject();
		
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (email == null && password == null)
        {
        	email = js.get("email").getAsString();
        	password = js.get("password").getAsString();
        }
        
    	
        try {
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
			// Connect database
			//Connection connection = dataSource.getConnection();
			// declare statement
			PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM customers WHERE email = '"+email+"' AND password = '"+password+"' LIMIT 1");
			// query customer
			//String query = "SELECT * FROM customers WHERE email = '"+email+"' AND password = '"+password+"' LIMIT 1"; 
			// execute query
			ResultSet rs = statement.executeQuery();
			
			// initialize a response json
			JsonObject responseJsonObject = new JsonObject();
			
			//PrintWriter out = response.getWriter();
			
			response.setContentType("application/json");
			
			if (rs.next())
			{
				Customer customer = new Customer(
						rs.getInt("id"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("ccId"),
						rs.getString("address"),
						rs.getString("email"),
						rs.getString("password"));
				
				// set this user into the session
	            request.getSession().setAttribute("customer", customer);
	            // FOR TEST PURPOSE: session will never expire unless cookie is deleted on client side
	            request.getSession().setMaxInactiveInterval(-1);
	            
	            responseJsonObject.addProperty("status", "success");
	            responseJsonObject.addProperty("message", "Successfully logged in!");
			}
			else {
	            // Login fail
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "Incorrect email or password!");
	            responseJsonObject.addProperty("email", email);
	            responseJsonObject.addProperty("password", password);
			}
			
			out.write(responseJsonObject.toString());
			response.setStatus(200);
			
			rs.close();
			statement.close();
			dbcon.close();
			
        } catch (Exception e) {
    		e.printStackTrace(); 
        }
    }
}

