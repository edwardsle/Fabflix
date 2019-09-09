/*
 * Name: An Le - Will Trinh
 * Team 20
 * 
 * Course: CS 122B 
 * UCI SPRING 18
 * 
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import Models.Movie;
import Models.Star;


@WebServlet("/api/star/view")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
    /**
     * An empty constructor
     */
    public SingleStarServlet() {
        super();
        
    }
    
	/**
	 * An Empty Destructor
	 */
	public void destroy() 
	{
		
	}

	/**
	 * Response to the GET Method
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get parameters
		String starId;
		
		try {
			starId = request.getParameter("id").toLowerCase();
		}
		catch (Exception e) {
			starId = null;
		}
		
		if (starId == null)
		{
			response.setStatus(404);
			return;
		}
		
		try {
				Context initCtx = new InitialContext();
	            Context envCtx = (Context) initCtx.lookup("java:comp/env");	         
	            // Look up our data source
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
	            Connection dbcon = ds.getConnection();
				// Connect database
				//Connection connection = dataSource.getConnection();
				
				PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM stars WHERE id = ? LIMIT 1");
				statement.setString(1, starId);
				
				// execute query
				ResultSet rs = statement.executeQuery();
				
				//Create ArrayLists
				Star star = null;
				while (rs.next())
				{
					String id = rs.getString("id");
					String name = rs.getString("name");
        			String birthYear = rs.getString("birthYear");
        			if (birthYear == null) birthYear = "Unknown";
					//star = new Star(rs.getString("id"), rs.getInt("birthYear"), rs.getString("name"));
					ArrayList<Movie> movies = new ArrayList<Movie>();
					//Statement newStm = dbcon.createStatement();
					PreparedStatement queryMovie = dbcon.prepareStatement("SELECT * FROM movies WHERE id IN (SELECT movieId from stars_in_movies WHERE starId = '" + id + "')");
					//String queryMovie = "SELECT * FROM movies WHERE id IN (SELECT movieId from stars_in_movies WHERE starId = '" + id + "')";
					ResultSet rsMovie = queryMovie.executeQuery();
					while (rsMovie.next())
        			{
        				movies.add(new Movie(rsMovie.getString("id"), rsMovie.getString("title")));
        			}
					queryMovie.close();
        			rsMovie.close();
        			star = new Star(id, birthYear, name, movies);
				}
				
				
				//Close connection
				rs.close();
				statement.close();
				dbcon.close();
				
				if (star != null) {
					//Convert to JsonObject
					Gson gson = new Gson();
					String json = gson.toJson(star);
					response.getWriter().print(json);
					response.setStatus(200);
				}
				else
				{
					response.setStatus(404);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}	
	}
}
