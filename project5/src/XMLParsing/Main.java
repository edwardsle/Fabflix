package XMLParsing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import DAO.MovieDAO;
import Models.Star;

public class Main {

	public static void main(String[] args) {
		
		
		
		long startTime;
		long endTime;
		
		HashMap<String, String> categories = new HashMap<String, String>();
		categories.put("Ctxx","Uncategorized");
		categories.put("Actn","Violence");
		categories.put("Camp","Now - camp");
		categories.put("Comd","Comedy");
		categories.put("Disa","Disaster");
		categories.put("Epic","Epic");
		categories.put("Horr","Horror");
		categories.put("Noir","Black");
		categories.put("ScFi","Science Fiction");
		categories.put("West","Western");
		categories.put("Advt","Adventure");
		categories.put("Cart","Cartoon");
		categories.put("Docu","Documentary");
		categories.put("Faml","Family");
		categories.put("Musc","Musical");
		categories.put("Porn","Pornography");
		categories.put("Surl","Sureal");
		categories.put("AvGa","Avant Garde");
		categories.put("CnR","Cops and Robbers");
		categories.put("Dram","Drama");
		categories.put("Hist","History");
		categories.put("Myst","Mystery");
		categories.put("Romt","Romantic");
		categories.put("Susp","Thriller");
		
		startTime = System.currentTimeMillis(); 
		XMLSAXMovieParser movieParse = new XMLSAXMovieParser();
		HashMap<String, MovieWithGenre> movies = movieParse.myMovies;
		
		XMLSAXStarInMovieParser starMovieParse = new XMLSAXStarInMovieParser();
		List<StarInMovie> stars = starMovieParse.getStars();
			
		Iterator<StarInMovie> star = stars.iterator();
		   while (star.hasNext()) {
			   StarInMovie each = star.next();
			   if (movies.get(each.getFid()) != null)
				   movies.get(each.getFid()).setStar(each.getName());
		    }
		   
	   try {
		MovieDAO mvDAO = new MovieDAO();
		mvDAO.commit();
		mvDAO.setAutoCommit(0);
		Iterator it = movies.entrySet().iterator();
		   while (it.hasNext()) {
			   HashMap.Entry pair = (HashMap.Entry)it.next();
			   MovieWithGenre movie = (MovieWithGenre) pair.getValue();
			   String genre = movie.getGenre();
			   if (categories.containsKey(genre))
				  movie.setGenre(categories.get(genre));
			   
			   if (movie.getTitle() != null && movie.getYear() > 0 && movie.getDirector() != null && movie.getStar() != null && movie.getGenre() != null)
			   {
				   //System.out.println(movie.toString());
				   mvDAO.insertByStoredProcedure(movie.getTitle(), movie.getYear(), movie.getDirector(), movie.getStar(), movie.getGenre());
			   }
		    }
		   mvDAO.commit();
		   mvDAO.setAutoCommit(1);
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   endTime = System.currentTimeMillis(); 
	   System.out.println("Total execution time for StarInMovie Parser in seconds: " + ((endTime - startTime) / 1000.0));
	}
}
