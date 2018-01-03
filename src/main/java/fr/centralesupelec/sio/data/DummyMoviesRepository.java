package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.endpoints.utils.ResponseHelper;
import fr.centralesupelec.sio.model.Movie;
import fr.centralesupelec.sio.model.MovieGenre;
import fr.centralesupelec.sio.model.People;
import fr.centralesupelec.sio.model.PeopleSpeciality;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A concrete {@link MoviesRepository} backed by an in-memory list of static {@link Movie} entities.
 */
// This class is not accessible outside of its package.
class DummyMoviesRepository extends MoviesRepository {

    // Hold entities in a simple list.
    private final List<Movie> mMovies;
    private final List<People> mPeople;

    DummyMoviesRepository() {
        // Define some static movies on creation

        // person n°1
        People p1 = new People();
        p1.setId(1);
        p1.setFirstName("Lary");
        p1.setLastName("Page");
        p1.setSpecialities(EnumSet.of(PeopleSpeciality.ACTOR, PeopleSpeciality.MUSICIAN));

        // person n°2
        People p2 = new People();
        p2.setId(2);
        p2.setFirstName("Jimmy");
        p2.setLastName("House");
        p2.setSpecialities(EnumSet.of(PeopleSpeciality.DIRECTOR));

        People p3 = new People();
        p3.setId(3);
        p3.setFirstName("Boy");
        p3.setLastName("Bad");
        p3.setSpecialities(EnumSet.of(PeopleSpeciality.ACTOR, PeopleSpeciality.DIRECTOR));

        // movie n°1
        Movie m1 = new Movie();
        m1.setId(1);
        m1.setTitle("Lord of the Rings: The Return of the King");
        m1.setGenres(EnumSet.of(MovieGenre.FANTASY));
        m1.setActors(Arrays.asList(p1));
        m1.setDirectors(Arrays.asList(p2));

        // movie n°1
        Movie m2 = new Movie();
        m2.setId(2);
        m2.setTitle("Star Wars VIII: The Last Jedi");
        m2.setGenres(EnumSet.of(MovieGenre.SCIENCE_FICTION));

        // movie n°3
        Movie m3 = new Movie();
        m3.setId(3);
        m3.setTitle("Kingsman 2: The Golden Circle");
        m3.setGenres(EnumSet.of(MovieGenre.COMEDY, MovieGenre.ACTION));

        //data objects
        mMovies = Arrays.asList(m1, m2, m3);
        mPeople = Arrays.asList(p1, p2, p3);
    }

    @Override
    public List<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public List<People> getPeople(String speciality) {

        PeopleSpeciality spec;
        try {
            spec = PeopleSpeciality.valueOf(speciality);
        } catch (IllegalArgumentException ex) {
            return null;
        }
        return mPeople.parallelStream()
                  .filter(people -> people.getSpecialities().contains(spec))
                  .collect(Collectors.toList());
    }

    @Override
    public Movie getMovie(long id, String text) {
        // See DummyAccountsRepository for more details and variants.
        return mMovies.parallelStream()
                .filter(movie -> movie.getId() == id)
                .findFirst()
                .orElse(null);
    }

}
