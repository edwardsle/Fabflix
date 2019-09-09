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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project1.Genre;
import project1.Movie;
import project1.Star;


@WebServlet("/fablix")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String dbUser = "root";
    private static String dbPassword = "fantasy";
    private static String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
	
	
    /**
     * An empty constructor
     */
    public MovieServlet() {
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
		try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				// Connect database
				Connection connection = DriverManager.getConnection(loginUrl, dbUser, dbPassword);
				// declare statement
				Statement statement = connection.createStatement();

				String query = "SELECT * FROM (movies INNER JOIN ratings ON ratings.movieId = movies.id) ORDER BY rating DESC LIMIT 20";
				
				// execute query
				ResultSet rs = statement.executeQuery(query);
				
				//Create ArrayLists
				ArrayList<Movie> movies = new ArrayList<Movie>();
				
				
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
        			movies.add(new Movie(id, title, year, director, stars, genres, rating));
        		}
				
				//Close connection
				rs.close();
				statement.close();
				connection.close();
				
				// set the movie lists as an attribute of the request
    			request.setAttribute("movies", movies);
    			
    			// dispatch the request to the view movieList.jsp
    			request.getRequestDispatcher("/WEB-INF/templates/movieList.jsp").forward( request, response ); 
				
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
