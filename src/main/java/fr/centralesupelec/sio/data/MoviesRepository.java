package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Movie;
import fr.centralesupelec.sio.model.MovieGenre;
import fr.centralesupelec.sio.model.People;

import java.util.Hashtable;
import java.util.List;

/**
 * A data repository to expose movie-related entities.
 */
// A clean approach is to expose only capabilities of the repository to other parts of the project.
// This is achieved by defining here a public interface with data-access methods.
// The concrete implementation is done in non-public specific classes.
// We switched to an "abstract class" instead of an interface just to add the singleton below.
public abstract class MoviesRepository {

    // Singleton pattern: Hold a unique instance of the repository in a private static field.
    private static MoviesRepository sRepository;

    /**
     * Obtain a shared instance of the repository.
     *
     * @return The shared {@link MoviesRepository} instance.
     */
    // Singleton pattern: Expose a public static method to obtain the unique instance.
    public static MoviesRepository getInstance() {
        // The unique instance is null by default, create if needed.
        if (sRepository == null) {
            // Here we choose which concrete class to use for the repository.
            // This choice is not visible to the outside, since the singleton only exposes the parent abstract class.
            //sRepository = new DummyMoviesRepository();
            sRepository = new DatabaseMoviesRepository();
        }
        return sRepository;
    }

    // Singleton pattern: Prevent instantiation of the class from the outside using a restrictive visibility modifier.
    // The only way to get a MovieRepository from the outside is MovieRepository.getInstance().
    protected MoviesRepository() {
    }

    /**
     * Query all available movies.
     *
     * @return A list of {@link Movie} entities.
     */
    // We only define the interface of a repository, concrete implementation is delegated to subclasses.
    // public abstract List<Movie> getMovies(); // no longer needed - below function do all the job

    //function below helps to get movies per parameter, implementation is done in concrete classes
    public abstract List<Movie> getMovie(String text, int offset, int limit, String[] genres, long[] directors, long[] actors);

    // Return genres of movies as an hashtable : enumCode - enumValue
    public static Hashtable getGenres() {
        Hashtable<String,MovieGenre> genres = new Hashtable();
        for (MovieGenre g : MovieGenre.values()) {
            genres.put(g.getGenreCode(),g);
        }
        return genres;
    }

    // Return list of actors in the directory
    public abstract List<People> getPeople(String speciality);
}
