import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Models.Customer;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/api/me")
public class MeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Customer me = (Customer) request.getSession().getAttribute("customer");
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String js = gson.toJson(me);
			response.getWriter().print(js);
		}
		catch (Exception e)
		{
			return;
		}
		
		
	}
}
