import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

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

        // Redirect if customer is unauthenticated
        if (httpRequest.getSession().getAttribute("customer") == null) {
            httpResponse.sendRedirect("/project2/login.html");
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

        return requestURI.endsWith("login.html") || requestURI.contains("/res/")
                || requestURI.endsWith("api/login") || requestURI.endsWith("api/logout");
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
