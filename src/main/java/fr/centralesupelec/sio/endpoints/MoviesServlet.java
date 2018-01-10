package fr.centralesupelec.sio.endpoints;

import fr.centralesupelec.sio.data.MoviesRepository;
import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;
import fr.centralesupelec.sio.model.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A servlet to access the list of {@link Movie}s.
 */
// The following pattern will exactly match /movies only.
@WebServlet(urlPatterns = "/moviesssss")
public class MoviesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get movies from the repository.
        // TODO: Add pagination parameters
        List<Movie> movies = MoviesRepository.getInstance().getMovies();

        // Write to the response.
        ResponseHelper.writeJsonResponse(resp, movies);

    }

}
