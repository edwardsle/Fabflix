package Admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Helpers.RequestHelper;
import Helpers.ResponseHelper;
import Models.Star;

/**
 * Servlet implementation class AdminStarServlet
 */
@WebServlet("/admin/api/star")
public class AdminStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminStarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(data).getAsJsonObject();
		
		try {
			String name = RequestHelper.getRequiredParam("name", json, request);
			Integer birthYear = RequestHelper.getParamAsPositiveInt("birthYear", json, request);
		
			Connection connection = dataSource.getConnection();
			
			PreparedStatement stmGetMaxID = connection
					.prepareStatement("SELECT max(id) as max from stars");
			
			ResultSet rs = stmGetMaxID.executeQuery();
			
			String id = null;
			if (rs.next())
			{
				// parse max ID
				String maxID = rs.getString("max");
				String prefix = StringUtils.left(maxID, 2);
				int num = Integer.parseInt(StringUtils.mid(maxID, 2, maxID.length()));
				
				// get next ID
				id = prefix + (++num);			
			}
			else
			{
				ResponseHelper.sendMessageAsJson(false, "Unable to generate next id", response);
				return;
			}
			// declare preparedstatement
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)");
			
			statement.setString(1, id);
			statement.setString(2, name);
			if (birthYear != null)
				statement.setInt(3, birthYear);
			else
				statement.setNull(3, Types.NULL);
			
			int count = statement.executeUpdate();
			
			if (count > 0)
				ResponseHelper.sendMessageAsJson(true, "Successfully inserted!", id, response);
			else
				ResponseHelper.sendMessageAsJson(false, "Unable to insert!", response);

			statement.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ResponseHelper.sendMessageAsJson(false, e.getMessage() , response);
		}
	}
}
