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

        String speciality = req.getParameter("spec");
        List<People> people = MoviesRepository.getInstance().getPeople(speciality);

        // Write to the response.
        ResponseHelper.writeJsonResponse(resp, people);

    }

}
