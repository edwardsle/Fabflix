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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Models.Genre;
import Models.Movie;
import Models.Star;


@WebServlet("/api/movie")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
    /**
     * An empty constructor
     */
    public MovieListServlet() {
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
		int genreId;
		char letter;
		String title_key;
		int year_key;
		String director_key;
		String star_key;
		String sort;
		String sortBy;
		int page;
		int limit;
		
		try {
			genreId = Integer.parseInt(request.getParameter("genreId"));
		}
		catch (Exception e) {
			genreId = -1;
		}
		
		try {
			letter = request.getParameter("letter").charAt(0);
		}
		catch (Exception e)
		{
			letter = 0;
		}
		try {
			year_key = Integer.parseInt(request.getParameter("year"));
		}
		catch (Exception e) {
			year_key = -1;
		}
		
		try {
			title_key = request.getParameter("title").toLowerCase();
		}
		catch (Exception e)
		{
			title_key = null;
		}
		
		try {
			director_key = request.getParameter("director").toLowerCase();
		}
		catch (Exception e)
		{
			director_key = null;
		}
		
		try {
			star_key = request.getParameter("star").toLowerCase();
		}
		catch (Exception e)
		{
			star_key = null;
		}
		
		try {
			sortBy = request.getParameter("sortBy").toLowerCase();
		}
		catch (Exception e)
		{
			sortBy = "";
		}
		
		try {
			sort = request.getParameter("sort").toLowerCase();
		}
		catch (Exception e)
		{
			sort = "";
		}
		
		try {
			page = Integer.parseInt(request.getParameter("page"));
		}
		catch (Exception e) {
			page = 0;
		}
		
		try {
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		catch (Exception e) {
			limit = 20;
		}
		
		try {
				// Connect database
				Connection connection = dataSource.getConnection();
				// declare statement
				Statement statement = connection.createStatement();
				
				// where query part
				String whereQ = "";
				
				if (genreId != -1)
				{
					whereQ = "WHERE id IN (SELECT movieId from genres_in_movies WHERE genreId='" + genreId + "')";
				}
				else if (letter != 0)
				{
					whereQ = "WHERE title LIKE '" + letter + "%'"; 
				}
				else
				{
					// where query part
					String titleCondition = ((title_key != null) ? "title LIKE '%"+ title_key+"%'" : null);
					String directorCondition = ((director_key != null) ? "director LIKE '%"+ director_key+"%'" : null);;
					String starCondition = ((star_key != null) ? "id IN (SELECT movieId FROM stars_in_movies WHERE starId IN (SELECT id FROM stars WHERE name LIKE '%"+ star_key + "%'))" : null);;
					String yearCondition = ((year_key != -1) ? "year ='"+ year_key+"'" : null);;
					
					whereQ = "";
					if (titleCondition != null || directorCondition != null || starCondition != null || yearCondition != null)
					{
						whereQ = "WHERE " + Stream.of(titleCondition, directorCondition, starCondition, yearCondition)
				          .filter(s -> s != null)
				          .collect(Collectors.joining(" AND "));
					}
				}
				
				// sort query part
				String sortQ = "ORDER BY title";
				if (sortBy.equals("rating"))
					sortQ = "ORDER BY rating";			
				if (sort.equals("desc"))
				{
					sortQ += " DESC";
				}
				else
				{
					sortQ += " ASC";
				}
				
				// page query part
				String pageQ = "LIMIT " + limit;
				if (page >= 1)
				{
					pageQ += " OFFSET " + ((page-1) * limit);
				}
				
				String query = "SELECT * FROM (movies LEFT JOIN ratings ON ratings.movieId = movies.id) " + whereQ + " " +sortQ + " " + pageQ;
				String queryToCount = "SELECT count(*) as c FROM (movies LEFT JOIN ratings ON ratings.movieId = movies.id) " + whereQ + " " +sortQ;
				
				ResultSet rs = statement.executeQuery(queryToCount);
				
				int count = 0;
				if (rs.next())
					count = rs.getInt("c");
				
				if (count <= 0)
				{
					response.getWriter().print("hello");
					response.getWriter().print(count);
				}
				
				int maxPage = (count / limit) + (count % limit == 0 ? 0 : 1);

				// execute query
				rs = statement.executeQuery(query);
				System.out.println(query);
				
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
				
				//Convert to JsonObject
				Gson gson = new Gson();
				//String str = gson.toJson(movies);
				JsonElement element = gson.toJsonTree(movies);
				JsonObject obj = new JsonObject();
				obj.add("data", element);
				obj.addProperty("maxPage", maxPage);
				
				response.getWriter().print(obj.toString());
				
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
