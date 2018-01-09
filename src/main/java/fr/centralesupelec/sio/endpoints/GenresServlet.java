package fr.centralesupelec.sio.endpoints;

import fr.centralesupelec.sio.data.MoviesRepository;
import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;
import fr.centralesupelec.sio.model.MovieGenre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Hashtable;

@WebServlet(urlPatterns = "/genres")
public class GenresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get movies from the repository.
        Hashtable genres = MoviesRepository.getInstance().getGenres();

        // Write to the response.
        ResponseHelper.writeJsonResponse(resp, genres);
    }
}
