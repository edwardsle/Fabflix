/*
 * Name: An Le - Will Trinh
 * Team 20
 * 
 * Course: CS 122B 
 * UCI SPRING 18
 * 
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Models.Genre;
import Models.Movie;
import Models.Star;


@WebServlet("/api/movie/view")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
    /**
     * An empty constructor
     */
    public SingleMovieServlet() {
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
		String movieId;
		
		try {
			movieId = request.getParameter("id").toLowerCase();
		}
		catch (Exception e) {
			movieId = null;
		}
		
		if (movieId == null)
		{
			return;
		}
		
		try {
				// Connect database
				Connection connection = dataSource.getConnection();
				
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM (movies INNER JOIN ratings ON ratings.movieId = movies.id) WHERE id = ?");
				statement.setString(1, movieId);
				
				// execute query
				ResultSet rs = statement.executeQuery();
				
				//Create ArrayLists
				Movie movie = null;

				// Iterate each movie and add it to the list.
				while (rs.next())
				{
					// get all columns
					String id = rs.getString("movies.id");
					String title = rs.getString("movies.title");
        			int year = rs.getInt("movies.year");
        			String director = rs.getString("movies.director");
        			float rating = rs.getFloat("ratings.rating");
        			
        			// create lists for stars and genres
        			ArrayList<Star> stars = new ArrayList<Star>();
        			ArrayList<Genre> genres = new ArrayList<Genre>();
        			
        			// create new statement for executing queries
        			Statement newStm = connection.createStatement();
    				
        			// Get all stars relating to this movie
    				String queryStar = "SELECT * FROM stars WHERE id IN (SELECT starId from stars_in_movies WHERE movieId = '" + id + "')";
    				ResultSet rsStar = newStm.executeQuery(queryStar);
        			while (rsStar.next())
        			{
        				stars.add(new Star(rsStar.getString("id"),rsStar.getString("birthYear"),rsStar.getString("name")));
        			}
        			rsStar.close();
							
    				// Get all genres relating to this movie
    				String queryGenres = "SELECT * FROM genres WHERE id IN (SELECT genreId from genres_in_movies WHERE movieId = '" + id + "')";
    				ResultSet rsGenres = newStm.executeQuery(queryGenres);
        			while (rsGenres.next())
        			{
        				genres.add(new Genre(rsGenres.getString("id"),rsGenres.getString("name")));
        			}
        			
        			// close the resultset and statement
        			rsGenres.close();
        			newStm.close();
        			
        			// add this movie object to the list
        			movie = new Movie(id, title, year, director, stars, genres, rating);
        		}
				
				//Close connection
				rs.close();
				statement.close();
				connection.close();
				
				if (movie != null) {
					//Convert to JsonObject
					Gson gson = new Gson();
					String json = gson.toJson(movie);
					response.getWriter().print(json);
					response.setStatus(200);
				}
				else
				{
					response.getWriter().print(movieId);
				}
				
		} catch (Exception e) {
			response.getWriter().print(movieId);
		}	
	}
}
