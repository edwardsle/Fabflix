package Admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DAO.GenreDAO;
import DAO.MovieDAO;
import DAO.StarDAO;
import Helpers.RequestHelper;
import Helpers.ResponseHelper;
import Models.Movie;
import Models.Star;

/**
 * Servlet implementation class AdminStarServlet
 */
@WebServlet("/admin/api/movie")
public class AdminMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminMovieServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(data).getAsJsonObject();

		try {
			String title = RequestHelper.getRequiredParam("title", json, request);
			int year = RequestHelper.getRequiredParamAsPositiveInt("year", json, request);
			String director = RequestHelper.getRequiredParam("director", json, request);
			String starName = RequestHelper.getRequiredParam("starName", json, request);
			String genreName = RequestHelper.getRequiredParam("genreName", json, request);

			int count = insertByStoredProcedure(title, year, director, starName, genreName);
			if (count > 0)
				ResponseHelper.sendMessageAsJson(true, "Successfully inserted", response);
			else
				ResponseHelper.sendMessageAsJson(false, "The movie already exists!", response);
		} catch (Exception e) {
			
			ResponseHelper.sendMessageAsJson(false, e.getMessage(), response);
		}

	}

	private int insertByStoredProcedure(String title, int year, String director, String starName, String genreName)
			throws SQLException {
		Connection connection = dataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement("CALL add_movie(?, ?, ?, ?, ?)");

		statement.setString(1, title);
		statement.setInt(2, year);
		statement.setString(3, director);
		statement.setString(4, starName);
		statement.setString(5, genreName);

		int count = statement.executeUpdate();
		statement.close();
		connection.close();

		return count;
	}
}
