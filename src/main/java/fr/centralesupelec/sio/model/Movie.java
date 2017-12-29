package fr.centralesupelec.sio.model;

import java.util.EnumSet;
import java.util.List;

/**
 * An entity class for a movie.
 */
public class Movie {

    private long id;
    private String title;
    // MovieGenre is an enum, combinations of enums are best handled by EnumSet.
    private EnumSet<MovieGenre> genres;

    private List<People> directors;
    private List<People> actors;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EnumSet<MovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(EnumSet<MovieGenre> genres) {
        this.genres = genres;
    }

    public List<People> getDirectors() {
        return directors;
    }

    public void setDirectors(List<People> directors) {
        this.directors = directors;
    }

    public List<People> getActors() {
        return actors;
    }

    public void setActors(List<People> actors) {
        this.actors = actors;
    }
}