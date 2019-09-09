

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.google.gson.JsonParser;
import Helpers.InvalidInputException;
import Helpers.RequestHelper;
import Models.Genre;
import Models.Movie;
import Models.Star;

/**
 * Servlet implementation class AutoCompleteServlet
 */
@WebServlet("/api/fullsearch")
public class AndroidSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidSearchServlet() {
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
			
            PreparedStatement statement = dbcon.prepareStatement("SELECT * from (movies LEFT JOIN ratings ON ratings.movieId = movies.id) WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset);
			
			//String query = "SELECT * from (movies LEFT JOIN ratings ON ratings.movieId = movies.id) WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset;
			
			// FuzzySearch
			//String query = "SELECT * from (movies LEFT JOIN ratings ON ratings.movieId = movies.id) WHERE MATCH (title) AGAINST ('" +keyword+ "' in boolean mode) or edth(title, '" +keywordFuzzy+ "', 2) ORDER BY title ASC LIMIT " + limit + " OFFSET " + offset;
			
			ResultSet rs = statement.executeQuery();
			
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
    			Statement newStm = dbcon.createStatement();
				
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
			dbcon.close();
			
			//Convert to JsonObject
			Gson gson = new Gson();
			
			//String str = gson.toJson(movies);
			JsonElement element = gson.toJsonTree(movies);
			JsonObject obj = new JsonObject();
			obj.add("data", element);
			
			response.getWriter().print(obj.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
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
