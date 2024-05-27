package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;

public class MovieAPIRequestBuilder {
    private String currentQuery;

    public MovieAPIRequestBuilder(String base) {
        this.currentQuery = base;
    }
    public MovieAPIRequestBuilder query(String query) {
        if (!query.equals("")) {
            currentQuery += "query=" + query + "&";
        }
        return this;
    }
    public MovieAPIRequestBuilder genre(String genre) {
        if (!genre.equals("")) {
            currentQuery += "genre=" + genre + "&";
        }
        return this;
    }
    public MovieAPIRequestBuilder releaseYear(int releaseYear) {
        if (releaseYear != -1) {
            currentQuery += "releaseYear=" + releaseYear + "&";
        }
        return this;
    }
    public MovieAPIRequestBuilder ratingFrom(double ratingFrom) {
        if (ratingFrom != -1) {
            currentQuery += "ratingFrom=" + ratingFrom;
        }
        return this;
    }
    public String build() {
        return currentQuery;
    }
}
