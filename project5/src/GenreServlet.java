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

import Models.Genre;
import Models.Movie;
import Models.Star;


@WebServlet("/api/genre")
public class GenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
    /**
     * An empty constructor
     */
    public GenreServlet() {
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
		PrintWriter out = response.getWriter();
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
				
				PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM genres ORDER BY name");
				
				// execute query
				ResultSet rs = statement.executeQuery();
				
				//Create ArrayLists
				ArrayList<Genre> genres = new ArrayList<Genre>();
				while (rs.next())
				{
					genres.add(new Genre(rs.getString("id"), rs.getString("name")));
				}
				
				
				//Close connection
				rs.close();
				statement.close();
				dbcon.close();
				
				if (genres.size() > 0) {
					//Convert to JsonObject
					Gson gson = new Gson();
					String json = gson.toJson(genres);
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
