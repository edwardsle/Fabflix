

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
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
import com.google.gson.JsonParser;

import Helpers.InvalidInputException;
import Helpers.RequestHelper;
import Models.Genre;
import Models.Movie;
import Models.Star;

import static java.nio.file.StandardOpenOption.*;

/**
 * Servlet implementation class AutoCompleteServlet
 */
@WebServlet("/api/search-both")
public class SearchPstmPool extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchPstmPool() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Time an event in a program to nanosecond precision // Code obtained from cs122b instruction
		long startTime = System.nanoTime();
		long tj1 = 0;
		long tj2 = 0;
		long tj3 = 0;
		
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
			limit = 50;
					
		System.out.println(keyword);
		
		try {
			Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
            	System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");

            if (ds == null)
            	System.out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
            	System.out.println("dbcon is null.");
			
            long startTj1 = System.nanoTime();
			PreparedStatement statement = dbcon.prepareStatement("SELECT * from (movies LEFT JOIN ratings ON ratings.movieId = movies.id) WHERE MATCH (title) AGAINST (? in boolean mode) ORDER BY title ASC LIMIT ? OFFSET ?");
			statement.setString(1, keyword);
			statement.setInt(2, limit);
			statement.setInt(3, offset);
			
			ResultSet rs = statement.executeQuery();
			long endTj1 = System.nanoTime();
			tj1 = endTj1 - startTj1;
			
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
    			long startTj2 = System.nanoTime();
    			PreparedStatement starStm = dbcon.prepareStatement("SELECT * FROM stars WHERE id IN (SELECT starId from stars_in_movies WHERE movieId = ?)");
				starStm.setString(1, id);
    			// Get all stars relating to this movie
				ResultSet rsStar = starStm.executeQuery();
				long endTj2 = System.nanoTime();
				
				tj2 = endTj2 - startTj2;
				
    			while (rsStar.next())
    			{
    				stars.add(new Star(rsStar.getString("id"),rsStar.getString("birthYear"),rsStar.getString("name")));
    			}
    			rsStar.close();
						
				// Get all genres relating to this movie
    			long startTj3 = System.nanoTime();
    			PreparedStatement genreStm = dbcon.prepareStatement("SELECT * FROM genres WHERE id IN (SELECT genreId from genres_in_movies WHERE movieId = ?)");
    			genreStm.setString(1, id);
				ResultSet rsGenres = genreStm.executeQuery();
				long endTj3 = System.nanoTime();
				tj3 = endTj3 - startTj3;
				
    			while (rsGenres.next())
    			{
    				genres.add(new Genre(rsGenres.getString("id"),rsGenres.getString("name")));
    			}
    			
    			// close the resultset and statement
    			starStm.close();
    			genreStm.close();
    			
    			// add this movie object to the list
    			movies.add(new Movie(id, title, year, director, stars, genres, rating));
    		}
			
			//Close connection
			rs.close();
			statement.close();
			dbcon.close();
			
			//Convert to JsonObject
			Gson gson = new Gson();
			
			//String str = gson.toJson(movies);
			JsonElement element = gson.toJsonTree(movies);
			JsonObject obj = new JsonObject();
			obj.add("data", element);
			
			response.getWriter().print(obj.toString());
			
			long endTime = System.nanoTime();
			long ts = endTime - startTime; 
			long tj = tj1 + tj2 + tj3;
			String str = "ts:"+ts+"-tj:"+tj+"\n";
			
			
			Path path = Paths.get(request.getServletContext().getRealPath("/"),"logboth.txt");
			
			
			
			System.out.println(path.toString());
			try (
			OutputStream out = new BufferedOutputStream(
		      Files.newOutputStream(path, CREATE, APPEND))) {
		      out.write(str.getBytes());
		    } catch (IOException x) {
		      System.err.println(x);
		    }
			
			//System.out.println(keyword);
			
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
