package fr.centralesupelec.sio.endpoints;

import fr.centralesupelec.sio.data.AccountsRepository;
import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;
import fr.centralesupelec.sio.model.Account;
import fr.centralesupelec.sio.model.Token;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * An authentication servlet for the API.
 * Clients must obtain an access token with this endpoint, then they sign subsequent requests to other data endpoints with it.
 * This servlet MUST NOT be matched by the {@link AuthFilter}!
 */
// This annotation replaces the registration of the servlet and its URL mappings in web.xml.
@WebServlet(urlPatterns = "/auth/token")
public class AuthServlet extends HttpServlet {


    //this method help to hash password provided by the user
    static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString().toUpperCase();
    }

    // This method will be called in case of a POST request on the URL in the mapping.
    // All methods in a servlet receive the request (req) as a parameter.
    // They don't return anything, instead they are supposed to write to the provided response (resp).
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Clients are expected to send credentials in query parameters, or using form-data.
        // Both are accessible by the getParameter() method.
        String username = req.getParameter("username");
        if (username == null || username.isEmpty()) {
            ResponseHelper.writeError(
                    resp,
                    "Missing username",
                    resp.SC_BAD_REQUEST // HTTP standard code 400 to indicate a client error
            );
            return;
        }
        // Check if the username looks like an email (using library Apache Commons).
        if (!EmailValidator.getInstance().isValid(username)) {
            ResponseHelper.writeError(resp,"Invalid username", resp.SC_BAD_REQUEST);
            return;
        }

        String password = req.getParameter("password");
        if (password == null || password.isEmpty()) {
            ResponseHelper.writeError(resp, "Missing password", resp.SC_BAD_REQUEST);
            return;
        }

        // Check that the account exists
        Account account = AccountsRepository.getInstance().getAccount(username);
        if (account == null) {
            ResponseHelper.writeError(resp,"Invalid credentials", resp.SC_UNAUTHORIZED);
            return;
        }
        // Check the password
        try {
            if (!account.getPasswordHash().equals(sha256(password))) {
                ResponseHelper.writeError(resp,"Bad Password", resp.SC_UNAUTHORIZED);
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Generate a successful Token response
        Token token = new Token();
        token.setAccessToken(AuthManager.generateAccessToken(username));
        ResponseHelper.writeJsonResponse(resp, token);

    }

}
