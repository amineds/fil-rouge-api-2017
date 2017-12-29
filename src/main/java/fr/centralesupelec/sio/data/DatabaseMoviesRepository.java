package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Movie;
import fr.centralesupelec.sio.model.People;

import java.util.List;

/**
 * A {@link MoviesRepository} backed by a database.
 */
// Example implementation of another storage
class DatabaseMoviesRepository extends MoviesRepository {

    @Override
    public List<Movie> getMovies() {
        // TODO
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public Movie getMovie(long id) {
        // TODO
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public List<People> getPeople(String speciality) {
        // TODO
        throw new UnsupportedOperationException("Not implemented!");
    }
}
