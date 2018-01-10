package fr.centralesupelec.sio.endpoints;

import com.google.appengine.repackaged.com.google.api.client.util.StringUtils;
import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;
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
@WebServlet(urlPatterns = "/movies")
public class MovieServlet extends HttpServlet {

    // This method will be called in case of a GET request.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // handling the title argument - default value empty string
        String title;
        if (!Strings.isNullOrEmpty(req.getParameter("title"))) { title = req.getParameter("title");}
            else {title = "";}

        // handling the offset argument - default value 0
        int offset;
        if (!Strings.isNullOrEmpty(req.getParameter("offset"))) {offset = Integer.parseInt(req.getParameter("offset"));}
            else {offset=0;}

        // handling the limit argument - default value 5
        int limit;
        if (!Strings.isNullOrEmpty(req.getParameter("limit"))) {limit = Integer.parseInt(req.getParameter("limit"));}
            else {limit=5;}

        //handling the genres argument
        String[] genres;
        if (!Strings.isNullOrEmpty(req.getParameter("genres"))) {genres =  req.getParameter("genres").split(",");}
            else {genres = new String[0];}

        //handling the directors argument
        long[] directors;
        if (!Strings.isNullOrEmpty(req.getParameter("directors"))) {
            directors = Arrays.stream(req.getParameter("directors").split(","))
                        .mapToLong(Long::parseLong)
                        .toArray();
        } else {directors = new long[0];}

        //handling the actors argument
        long[] actors;
        if (!Strings.isNullOrEmpty(req.getParameter("actors"))) {
            actors = Arrays.stream(req.getParameter("actors").split(","))
                    .mapToLong(Long::parseLong)
                    .toArray();
        } else {actors = new long[0];}

        // Find movie from the repository.
        List<Movie> movie = MoviesRepository.getInstance().getMovie(title, offset, limit,genres,directors, actors);
        if (!movie.isEmpty()) {
            ResponseHelper.writeJsonResponse(resp, movie);
        } else {
            ResponseHelper.writeError(resp, "Movie not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
