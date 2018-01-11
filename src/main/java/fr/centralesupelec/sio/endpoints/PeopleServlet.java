package fr.centralesupelec.sio.endpoints;
import fr.centralesupelec.sio.model.People;

import fr.centralesupelec.sio.data.MoviesRepository;
import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = "/people")
public class PeopleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //people are filtered according to their speciality : ACTOR, PRODUCER, MUSICIAN...
        //this paramater is mandatory
        String speciality = req.getParameter("spec");

        if (speciality != null) {
            List<People> people = MoviesRepository.getInstance().getPeople(speciality);
            if (people != null) {
                // Write to the response.
                ResponseHelper.writeJsonResponse(resp, people);
            } else {
                ResponseHelper.writeError(resp, "People not found", HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            ResponseHelper.writeError(resp, "People not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
