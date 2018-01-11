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

    private long[] directors;
    long[] actors;

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

    public long[] getDirectors() {
        return directors;
    }

    public void setDirectors(long[] directors) {
        this.directors = directors;
    }

    public long[] getActors() {
        return actors;
    }

    public void setActors(long[] actors) {
        this.actors = actors;
    }
}