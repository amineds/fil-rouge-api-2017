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
import java.util.Arrays;
import java.util.List;

/**
 * A data servlet to access a single {@link Movie} entity.
 */
// The following pattern will match for instance /movies/123.
@WebServlet(urlPatterns = "/movie/*")
public class MovieServlet extends HttpServlet {

    // This method will be called in case of a GET request.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException ex) {
            ResponseHelper.writeError(resp, "Invalid id", HttpServletResponse.SC_NOT_FOUND);
            return;
        }*/

        String text = req.getParameter("title");
        int offset = Integer.parseInt(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String[] genres =  req.getParameter("genres").split(",");
        long[] directors = Arrays.stream(req.getParameter("directors").split(","))
                         .mapToLong(Long::parseLong)
                         .toArray();

        // Find movie from the repository.
        List<Movie> movie = MoviesRepository.getInstance().getMovie(text, offset, limit,genres,directors);
        if (!movie.isEmpty()) {
            ResponseHelper.writeJsonResponse(resp, movie);
        } else {
            ResponseHelper.writeJsonResponse(resp, directors);
            //ResponseHelper.writeError(resp, "Movie not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
