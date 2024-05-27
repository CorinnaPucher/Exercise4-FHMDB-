package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.JSONAction;
import at.ac.fhcampuswien.fhmdb.JSONMovie;
import at.ac.fhcampuswien.fhmdb.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class Movie implements Comparable<Movie> {
    public String id;
    public String title;
    public String[] genres;
    public int releaseYear;
    public String description;
    public String imgUrl;
    public int lengthInMinutes;
    public String[] directors;
    public String[] writers;
    public String[] mainCast;
    public double rating;

    public Movie(JSONMovie jsonMovie) {
        this.id = jsonMovie.id;
        this.title = jsonMovie.title;
        this.genres = jsonMovie.genres;
        this.releaseYear = jsonMovie.releaseYear;
        this.description = jsonMovie.description;
        this.imgUrl = jsonMovie.imgUrl;
        this.lengthInMinutes = jsonMovie.lengthInMinutes;
        this.directors = jsonMovie.directors;
        this.writers = jsonMovie.writers;
        this.mainCast = jsonMovie.mainCast;
        this.rating = jsonMovie.rating;
    }
    public Movie(String id, String title, String[] genres, int releaseYear, String description,
                 String imgUrl, int lengthInMinutes, String[] directors, String[] writers,
                 String[] mainCast, double rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }
    public Movie(MovieEntity entityMovie) {
        this.id = entityMovie.apiId;
        this.title = entityMovie.title;
        this.genres = entityMovie.genres.split(",");
        this.releaseYear = entityMovie.releaseYear;
        this.description = entityMovie.description;
        this.imgUrl = entityMovie.imgUrl;
        this.lengthInMinutes = entityMovie.lengthInMinutes;
        this.rating = entityMovie.rating;
    }
    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String genre : genres) {
            stringBuilder.append(genre + ", ");
        }
        stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length()-1);
        return stringBuilder.toString();
    }
    public int getTitleLength() {
        return title.length();
    }

    public boolean directorIsInsideArray(String director) {
        Stream<String> streamDirectors = Stream.of(directors);
        boolean bool = streamDirectors.anyMatch(dir -> dir.equals(director));
        return bool;
    }

    public static List<Movie> initializeMovies() throws MovieApiException {
        List<Movie> movies = null;
        movies = JSONAction.parseJSON(MovieAPI.sendRequest());
        return movies;
    }

    public static List<Movie> initializeMovieDatabase() throws DatabaseException {
        List<Movie> movies = null;
        MovieRepository movieRepository = MovieRepository.getMovieRepository();
        movies = MovieEntity.toMovies(movieRepository.getAllMovies());
        return movies;
    }

    @Override
    public int compareTo(Movie o) {
        int shortestLength = Math.min(o.getTitle().length(), getTitle().length());
        for (int i = 0; i < shortestLength; i++) {
            if (Character.toLowerCase(getTitle().charAt(i)) > Character.toLowerCase(o.getTitle().charAt(i))) {
                // Defines the sorting priority (title 1 comes after title 2)
                return 1;
            } else if (Character.toLowerCase(getTitle().charAt(i)) < Character.toLowerCase(o.getTitle().charAt(i))) {
                // Defines the sorting priority (title 1 comes before title 2)
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Movie m = (Movie) obj;
        if(m.getTitle().equals(this.getTitle())) return true;
        return super.equals(obj);
    }
}
