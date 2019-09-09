import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "EmpLoginFilter")
public class EmpLoginFilter implements Filter {

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Allowed URLs
        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // Redirect if employee is unauthenticated
        if (httpRequest.getSession().getAttribute("employee") == null) {
            httpResponse.sendRedirect("/project3/admin/login.html");
        	//chain.doFilter(request, response); //Unlocked for testing purposes
        } else {
            chain.doFilter(request, response);
        }
    }

    // Setup your own rules here to allow accessing some resources without logging in
    // Always allow your own login related requests(html, js, servlet, etc..)
    // You might also want to allow some CSS files, etc..
    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        requestURI = requestURI.toLowerCase();

        return requestURI.endsWith("login.html") || requestURI.endsWith("api/login");
    }

    /**
     * We need to have these function because this class implements Filter.
     * But we do not need to put any code in them.
     *
     * @see Filter#init(FilterConfig)
     */

    public void init(FilterConfig fConfig) {
    }

    public void destroy() {
    }


}
