package fr.centralesupelec.sio.data;

import fr.centralesupelec.sio.model.Movie;
import fr.centralesupelec.sio.model.MovieGenre;
import fr.centralesupelec.sio.model.People;
import fr.centralesupelec.sio.model.PeopleSpeciality;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link MoviesRepository} backed by a database.
 */
// Example implementation of another storage
class DatabaseMoviesRepository extends MoviesRepository {

   /* @Override
    public List<Movie> getMovies() {
        throw new UnsupportedOperationException("Not implemented!");
    }*/

    // Hold entities in a simple list.
    private List<Movie> mMovies = new ArrayList<Movie>();
    private List<People> mPeople = new ArrayList<People>();

    DatabaseMoviesRepository() {
        //read here files and initiate variables mMovies
        String movieDir;
        //todo : find way to avoid setting hard the director
        movieDir = "C:\\Users\\benhamza\\AppData\\Local\\NoBackup\\Perso\\CentraleSupelec\\Java\\fil-rouge-api-2017\\src\\main\\java\\fr\\centralesupelec\\sio\\data\\rawdata\\movies.csv";
        //System.out.println("hello");
        Path moviesPath = FileSystems.getDefault().getPath(movieDir);
        try {
            List<String> moviesRecords = Files.readAllLines(moviesPath);
            moviesRecords.remove(0);//remove file header - contains columns names
            for (String movieRecord : moviesRecords) {
                String[] movieInfos =  movieRecord.split(";");

                Movie m = new Movie();
                //setting movie id - first column in the file
                m.setId(Long.parseLong(movieInfos[0]));
                //setting movie title - second column in the file
                m.setTitle(movieInfos[1]);
                //setting movie genres - third column in the file - separated by a comma
                EnumSet<MovieGenre> genresList = EnumSet.noneOf(MovieGenre.class);

                //todo : find a way to user stream instead of for.
                for (String genre : movieInfos[2].split(",")) {
                    MovieGenre mGenre;
                    // checking if the movie genres exist to avoid exception be raised
                    try { mGenre = MovieGenre.valueOf(genre.toUpperCase()); }
                    // if genre is not found do nothing
                    catch (IllegalArgumentException ex) {mGenre=null;}
                    if (mGenre != null) {
                        genresList.add(mGenre);
                    }
                }

                m.setGenres(genresList);
                //setting movie actors - fourth column in the file - separated by a comma
                long[] actors = Arrays.stream(movieInfos[3].split(","))
                        .mapToLong(Long::parseLong)
                        .toArray();
                m.setActors(actors);
                //setting movie directors - fifth column in the file - separated by a comma
                long[] directors = Arrays.stream(movieInfos[4].split(","))
                                   .mapToLong(Long::parseLong)
                                   .toArray();
                m.setDirectors(directors);
                mMovies.add(m);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //getting data for people
        String peopleDir;
        peopleDir = "C:\\Users\\benhamza\\AppData\\Local\\NoBackup\\Perso\\CentraleSupelec\\Java\\fil-rouge-api-2017\\src\\main\\java\\fr\\centralesupelec\\sio\\data\\rawdata\\people.csv";
        Path peoplePath = FileSystems.getDefault().getPath(peopleDir);

        try {
            List<String> peopleRecords = Files.readAllLines(peoplePath);
            peopleRecords.remove(0);
            for (String peopleRecord : peopleRecords) {
                String[] peopleInfos = peopleRecord.split(";");
                People p =  new People();
                //setting ID for People object
                p.setId(Long.parseLong(peopleInfos[0]));
                //setting first name
                p.setFirstName(peopleInfos[1]);
                //setting last name
                p.setLastName(peopleInfos[2]);
                //setting people specialities
                EnumSet<PeopleSpeciality> specList = EnumSet.noneOf(PeopleSpeciality.class);
                for (String spec : peopleInfos[3].split(",")) {
                    PeopleSpeciality pSpec;
                    // checking if the speciality exist to avoid exception be raised
                    try { pSpec = PeopleSpeciality.valueOf(spec.toUpperCase()); }
                    // if genre is not found do nothing
                    catch (IllegalArgumentException ex) {pSpec=null;}
                    if (pSpec != null) {
                        specList.add(pSpec);
                    }
                }
                p.setSpecialities(specList);
                mPeople.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
