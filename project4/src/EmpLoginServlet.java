import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Models.Employee;

@WebServlet("/admin/api/login")
public class EmpLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String data = request.getReader().lines().collect(Collectors.joining());
		JsonParser parser = new JsonParser();
		JsonObject js = parser.parse(data).getAsJsonObject();

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String gRecaptchaResponse = request.getParameter("captcha");

		if (email == null && password == null) {
			email = js.get("email").getAsString();
			password = js.get("password").getAsString();
			gRecaptchaResponse = js.get("captcha").getAsString();
		}

		PrintWriter out = response.getWriter();
		
		JsonObject responseJsonObject = new JsonObject();

		// Verify reCAPTCHA
		try {
			RecaptchaVerifyUtils.verify(gRecaptchaResponse);
		} catch (Exception e) {
			responseJsonObject.addProperty("status", "fail");
			responseJsonObject.addProperty("message", "Captcha is not accepted.");
			responseJsonObject.addProperty("email", email);
			responseJsonObject.addProperty("password", password);
			out.write(responseJsonObject.toString());
			return;
		}

		try {
			// Connect database
			Connection connection = dataSource.getConnection();
			// declare preparedstatement
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM employees WHERE email = '" + email + "' LIMIT 1");
			// execute query
			ResultSet rs = statement.executeQuery();

			response.setContentType("application/json");
			if (rs.next()) {
				// get the encrypted password from the database
				String encryptedPassword = rs.getString("password");
				boolean success = false;
				// use the same encryptor to compare the user input password with encrypted
				// password stored in DB
				success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
				if (success) {
					// get the encrypted password from the database
					Employee Emp = new Employee(rs.getString("email"), rs.getString("password"), rs.getString("fullname"));

					// set this user into the session
					request.getSession().setAttribute("employee", Emp);
					// FOR TEST PURPOSE: session will never expire unless cookie is deleted on
					// client side
					request.getSession().setMaxInactiveInterval(-1);

					responseJsonObject.addProperty("status", "success");
					responseJsonObject.addProperty("message", "Successfully logged in!");
				}	else {
					// Login fail
					responseJsonObject.addProperty("status", "fail");
					responseJsonObject.addProperty("message", "Incorrect password!");
					responseJsonObject.addProperty("email", email);
					responseJsonObject.addProperty("password", password);
				}
			} else {
				// Login fail
				responseJsonObject.addProperty("status", "fail");
				responseJsonObject.addProperty("message", "Username not found!");
				responseJsonObject.addProperty("email", email);
				responseJsonObject.addProperty("password", password);
			}

			out.write(responseJsonObject.toString());
			response.setStatus(200);

			rs.close();
			statement.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
