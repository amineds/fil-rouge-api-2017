package fr.centralesupelec.sio.endpoints.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.centralesupelec.sio.model.Error;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A helper class to assist writing JSON to {@link HttpServletResponse}.
 */
public class ResponseHelper {

    private static final String JSON_CONTENT_TYPE = "application/json";

    /**
     * Serialize an object to JSON using Gson, and write it to the response.
     * @param resp: The {@link HttpServletResponse} to write to.
     * @param response: The object to serialize.
     * @throws IOException if the response cannot be written.
     */
    // Configure the Gson builder - onetime for all
    static GsonBuilder gsonBuilder = new GsonBuilder();

    public static void writeJsonResponse(HttpServletResponse resp, Object response) throws IOException {
        // Send a Content-Type header to inform the client of the format of the response.
        resp.setContentType(JSON_CONTENT_TYPE);
        // Configure the Gson library
        // TODO: Don't recreate a new Gson instance for each response, reuse (singleton?)
        Gson gson = gsonBuilder
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        // Write the object to the response
        gson.toJson(response, resp.getWriter());
    }

    /**
     * Write a JSON error to the response. The structure will match the {@link Error} class.
     * @param resp: The {@link HttpServletResponse} to write to.
     * @param reason: The error message.
     * @param status: The HTTP status of the response.
     * @throws IOException if the response cannot be written
     */
    public static void writeError(HttpServletResponse resp, String reason, int status) throws IOException {
        resp.setStatus(status);
        Error error = new Error();
        error.setError(reason);
        writeJsonResponse(resp, error);
    }

}