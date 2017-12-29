package fr.centralesupelec.sio.endpoints;

import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A filter to check that all requests to the API are authenticated with a valid token.
 */
// TODO: Configure the filter to match ALL endpoints (except for the login endpoint)
// This annotation replaces the registration of the filter and its URL mappings in web.xml.
//@WebFilter(urlPatterns = {"/movies","/movies/*"})
public class AuthFilter implements Filter {

    // Pre-compile a Regex matcher for the Authorization header
    // This pattern will match the word "Bearer" followed by a space and one or more characters (the token).
    // The parenthesis will also capture the token (in a 'group' #1).
    private Pattern HEADER_PATTERN = Pattern.compile("Bearer (.+)");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do when the filter initiates.
    }

    // This method is called when a request is matched by the filter, before it is routed to the servlet.
    // The filter can inspect the request, write to the response.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // The request will always be a HttpServletRequest (casting).
        // It must contain a header with the standard 'Authorization' key.
        String auth = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (auth == null || auth.isEmpty()) {
            ResponseHelper.writeError(
                    (HttpServletResponse) servletResponse, // Cast the response
                    "Missing Authorization header",
                    HttpServletResponse.SC_UNAUTHORIZED // 401 standard HTTP code
            );
            return; // Stop here, the request will not be routed to the servlet.
        }
        // Check that the header matches the expected format.
        Matcher m = HEADER_PATTERN.matcher(auth);
        if (!m.matches()) {
            ResponseHelper.writeError(
                    (HttpServletResponse) servletResponse,
                    "Invalid Authorization header",
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }
        // We have a token captured in group #1, ask the AuthManager if it is valid.
        if (!AuthManager.checkAccessToken(m.group(1))) {
            ResponseHelper.writeError(
                    (HttpServletResponse) servletResponse,
                    "Invalid access token",
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }
        // We have passed all the tests, the request is properly authenticated.
        // The request will be continued to the servlet (or the next filter) be calling doFilter:
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Nothing to do when the server shuts down.
    }

}
