

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Models.Cart;
import Models.CartItem;
import Models.Customer;
import Models.Sale;

/**
 * Servlet implementation class CheckoutServlet
 */
@WebServlet("/api/checkout")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String json = "{'payment':{'firstname':'Bill','lastname':'Wang','cc':'0011 2233 4455 6677','exp':'2009-09-07'},'cart':[{'movieId':'tt0094859','quantity':'12'},{'movieId':'tt0114447','quantity':'12'}]}";
		PrintWriter out = response.getWriter();
		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject js = parser.parse(data).getAsJsonObject();
		
		String fname = js.get("fname").getAsString();
		String lname = js.get("lname").getAsString();
		String cc = js.get("cc").getAsString();
		String exp = js.get("exp").getAsString();
		
		// Get cart from session
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart == null)  
		{
			response.getWriter().println("Error: Cart is empty");
			return;
		}
		
		
		// Validate user session
		Customer user = (Customer) request.getSession().getAttribute("customer");
		if (user == null)
		{
			response.getWriter().println("Error: Please log in first");
			return;
		}
			
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
            
			//Connection con = dataSource.getConnection();
			
			// Validate credit card information
			boolean paymentValidated = false;
			
			if (cc.equals(user.getCcId()))
			{
				PreparedStatement stm = dbcon.prepareStatement("SELECT * FROM creditcards WHERE firstName = ? AND lastName = ? AND id = ? AND expiration = ? LIMIT 1");
				stm.setString(1, fname);
				stm.setString(2, lname);
				stm.setString(3, cc);
				stm.setString(4, exp);
				paymentValidated = stm.executeQuery().next();
				stm.close();
			}
			
			if (!paymentValidated)
				return;
						
			/*
			// Validate cart's items
			boolean cartValidated = true;
			
			PreparedStatement stmCart = con.prepareStatement("SELECT id FROM movies WHERE id = ?");
			*/
			ArrayList<CartItem> items = cart.getItems();
			/*
			for (CartItem item : items)
			{
				stmCart.setString(1, item.getId());
				cartValidated = cartValidated && (stmCart.executeQuery().next());
			}
			*/
			ArrayList<Sale> sales = new ArrayList<Sale>();
			
			//if (paymentValidated && cartValidated)
			if (true)
			{
				// Insert transaction record
				PreparedStatement stmInsert = dbcon.prepareStatement("INSERT INTO sales(customerId, movieId, saleDate) VALUES (?, ?, ?)");
				PreparedStatement stmSaleId = dbcon.prepareStatement("SELECT sales.id, sales.movieId, movies.title from sales, movies WHERE sales.movieId = movies.id AND customerId = ? AND movieId = ? ORDER BY id DESC LIMIT 1");
				
				stmInsert.setInt(1, user.getId());
				
				for (CartItem item : items)
				{
					int qty = item.getQuantity();
					for (;qty > 0; qty--)
					{
					stmInsert.setString(2, item.getId());
					stmInsert.setDate(3, java.sql.Date.valueOf((java.time.LocalDate.now())));
					int rowEffected = stmInsert.executeUpdate();
					if (rowEffected == 1)
					{
						stmSaleId.setInt(1, user.getId());
						stmSaleId.setString(2, item.getId());
						ResultSet saleSet = stmSaleId.executeQuery();
						if (saleSet.next())
							sales.add(new Sale(saleSet.getInt("id"), saleSet.getString("movieId"), saleSet.getString("title")));
						saleSet.close();
					}
					}
				}
				
				// close all connection
				stmInsert.close();
				stmSaleId.close();
				
				// output the sale
				if (!sales.isEmpty()) {
					//Convert to JsonObject
					Gson gson = new Gson();
					String json = gson.toJson(sales);
					response.getWriter().print(json);
				}
				
				// reset cart
				cart.clear();
			}
			else
			{
				response.getWriter().println("Error: Transaction problem");
			}
			
//			stmCart.close();
//			stm.close();
			dbcon.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}

}
