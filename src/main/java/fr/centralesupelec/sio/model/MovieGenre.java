package fr.centralesupelec.sio.model;

/**
 * An enum for movie genres.
 */
public enum MovieGenre {

    ADVENTURE ("ADV"),
    ACTION ("ACT"),
    ANIMATION ("ANI"),
    COMEDY ("COM"),
    CRIME ("CRI"),
    FANTASY ("FAN"),
    THRILLER ("THR"),
    SCIENCE_FICTION ("SCI");

    private String genreCode;

    MovieGenre(String code) {
        this.genreCode = code;
    }

    public String getGenreCode() {
        return genreCode;
    }

    public void setGenreCode(String genreCode) {
        this.genreCode = genreCode;
    }
}