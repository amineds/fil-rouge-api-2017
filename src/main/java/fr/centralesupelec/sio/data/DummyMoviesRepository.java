package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Movie;
import fr.centralesupelec.sio.model.MovieGenre;
import fr.centralesupelec.sio.model.People;
import fr.centralesupelec.sio.model.PeopleSpeciality;

import java.util.*;
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
        m1.setGenres(EnumSet.of(MovieGenre.FANTASY,MovieGenre.ACTION));
        m1.setActors(new long[]{1});
        m1.setDirectors(new long[]{2});

        // movie n°2
        Movie m2 = new Movie();
        m2.setId(2);
        m2.setTitle("Star Wars VIII: The Last Jedi");
        m2.setGenres(EnumSet.of(MovieGenre.SCIENCE_FICTION,MovieGenre.COMEDY,MovieGenre.THRILLER));
        m2.setActors(new long[]{1});
        m2.setDirectors(new long[]{3});

        // movie n°3
        Movie m3 = new Movie();
        m3.setId(3);
        m3.setTitle("Kingsman 2: The Golden Circle");
        m3.setGenres(EnumSet.of(MovieGenre.COMEDY, MovieGenre.ACTION));
        m3.setActors(new long[]{3});
        m3.setDirectors(new long[]{3});

        //data objects
        mMovies = Arrays.asList(m1, m2, m3);
        mPeople = Arrays.asList(p1, p2, p3);
    }

    /*@Override
    public List<Movie> getMovies() {
        return mMovies;
    }*/

    @Override
    public List<People> getPeople(String speciality) {

        PeopleSpeciality spec;
        try {
            spec = PeopleSpeciality.valueOf(speciality.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
        return mPeople.parallelStream()
                  .filter(people -> people.getSpecialities().contains(spec))
                  .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMovie(String text, int offset, int limit, String[] genres, long[] directors, long[] actors) {

        // if the user indicated no movie genre, we should consider all of them in the filter
        EnumSet<MovieGenre> genresList = EnumSet.noneOf(MovieGenre.class);
        if (genres.length != 0) {
             // here we handle the genres filled in
            for (String genre : genres) {
                MovieGenre mGenre;
                // checking if the movie genres exist to avoid exception be raised
                try { mGenre = MovieGenre.valueOf(genre.toUpperCase()); }
                // if genre is not found do nothing
                catch (IllegalArgumentException ex) {mGenre=null;}
                if (mGenre != null) {
                    genresList.add(mGenre);
                }
            }
        }
        //sorting both arrays to ease binay search below
        Arrays.sort(directors);
        Arrays.sort(actors);

        //check if a variable has a value or not, if not set the test as true
        //heavy as method as check is done for every member of the stream
        //should fine a convenient method to check variable and avoid doing filtering if not appropriate
        return mMovies.parallelStream()
                //filtering on title
                .filter(movie -> movie.getTitle().toLowerCase().contains(text.toLowerCase()))
                //filtering on movie genre
                .filter(movie -> movie.getGenres().stream().anyMatch(movieGenre -> {
                    if (genresList.isEmpty()) {return true;}
                    return genresList.contains(movieGenre);
                }))
                //filtering on directors
                .filter(movie -> { if (directors.length == 0) {return true;}
                    return Arrays.stream(movie.getDirectors()).anyMatch(id -> Arrays.binarySearch(directors,id) >= 0);
                })
                .filter(movie -> { if (actors.length == 0) {return true;}
                    return Arrays.stream(movie.getActors()).anyMatch(id -> Arrays.binarySearch(actors, id) >= 0);
                })
                .skip(offset).limit(limit)
                .collect(Collectors.toList());
    }

    public Movie getMovie(long id, String text) {
        // See DummyAccountsRepository for more details and variants.
        return mMovies.parallelStream()
                .filter(movie -> movie.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
