

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Models.Cart;
import Models.CartItem;
import Models.Customer;
import Models.Movie;

/**
 * Servlet implementation class CheckoutServlet
 */
@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Customer user = (Customer) request.getSession().getAttribute("customer");
//		if (user == null)
//		{
//			response.getWriter().print("Please log in");
//		}
//		
//		Cart cart = (Cart) request.getSession().getAttribute("cart");
//		if (cart == null)  
//		{
//			cart = new Cart();
//			request.getSession().setAttribute("cart", cart);
//		}
//		
//		response.getWriter().println(cart.toJsonString());
//	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String detail = request.getParameter("detail");
		PrintWriter out = response.getWriter();
		
		// Just output the cart in json format
		Customer user = (Customer) request.getSession().getAttribute("customer");
		if (user == null)
		{
			response.getWriter().print("Please log in");
			return;
		}
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)  
		{
			cart = new Cart();
			request.getSession().setAttribute("cart", cart);
		}

		if (detail != null && detail.equals("1"))
			try {
	            Context initCtx = new InitialContext();

	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                out.println("envCtx is NULL");

	            // Look up our data source
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/masterdb");

	            if (ds == null)
	                out.println("ds is null.");

	            Connection dbcon = ds.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
	            
				// Connect database
				//Connection connection = dataSource.getConnection();
				// declare statement		
				PreparedStatement stm = dbcon.prepareStatement("SELECT * FROM movies WHERE id = ? LIMIT 1");
				
				for (CartItem item : cart.getItems())
				{
					if (item.getMovie() == null)
					{
						stm.setString(1, item.getId());
						ResultSet rs = stm.executeQuery();
						
						Movie movie = null;
						while (rs.next())
						{
							// get all columns (We do not need genres and stars for cart information)
							String id = rs.getString("movies.id");
							String title = rs.getString("movies.title");
		        			int year = rs.getInt("movies.year");
		        			String director = rs.getString("movies.director");
		        			
		        			movie = new Movie(id, title, year, director, null, null, -1);
						}
						
						item.setMovie(movie);
						
						rs.close();
					}
				}
				
				stm.close();
				dbcon.close();
							
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		response.getWriter().println(cart.toJsonString());
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// We are doing POST method to prevent pre-fetching of browser, and so on...
		// Accept HTTP POST Attribute
		// If null, try to interpret the request's data in json format
		
		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject js = parser.parse(data).getAsJsonObject();
		
        String movieId = request.getParameter("movieId");
        String method = request.getParameter("method");
        String detail = request.getParameter("detail");
        
        if (method == null)
        {
        	try {
        		movieId = js.get("movieId").getAsString();	
        	}
        	catch (Exception e)
	        {
	        	// Error handle here	
	        }
	        try {
	        	method = js.get("method").getAsString();	
	    	}
	    	catch (Exception e)
	        {
	        	// Error handle here	
	        }
	        try {
	        	detail = js.get("detail").getAsString();	
        	}
        	catch (Exception e)
	        {
	        	// Error handle here	
	        }
        }
				
		Customer user = (Customer) request.getSession().getAttribute("customer");
		if (user == null)
		{
			response.getWriter().print("Please log in");
			return;
		}
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)  
		{
			cart = new Cart();
			request.getSession().setAttribute("cart", cart);
		}
		
		if (method != null)
			switch (method) {
				case "remove": cart.remove(movieId); break;
				case "add" : cart.add(movieId); break;
				case "increase": cart.increase(movieId); break;
				case "decrease": cart.decrease(movieId); break;
				case "clear" : cart.clear(); break;
				default:
					break;
			}
		
		response.getWriter().println(cart.toJsonString());
	}
}
