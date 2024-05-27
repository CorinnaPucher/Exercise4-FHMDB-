package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;
@DatabaseTable
public class MovieEntity {
    @DatabaseField(generatedId = true)
    public long id;
    @DatabaseField(unique = true) // No Duplicates
    public String apiId;
    @DatabaseField
    public String title;
    @DatabaseField
    public String genres;
    @DatabaseField
    public int releaseYear;
    @DatabaseField
    public String description;
    @DatabaseField
    public String imgUrl;
    @DatabaseField
    public int lengthInMinutes;
    @DatabaseField
    public double rating;

    public MovieEntity() {}

    public MovieEntity(Movie movie) {
        this.apiId = movie.id;
        this.title = movie.title;
        this.genres = genresToString(movie.genres);
        this.releaseYear = movie.releaseYear;
        this.description = movie.description;
        this.imgUrl = movie.imgUrl;
        this.lengthInMinutes = movie.lengthInMinutes;
        this.rating = movie.rating;
    }

    /**
     * Converts Genres String Array to single String with ',' delimiter
     * @param genres Input String array
     * @return String with ',' delimiter
     */
    public static String genresToString (String[] genres) {
        return String.join(",", genres);
    }

    /**
     * Converts Movies -> MovieEntities
     * @param movies The Movies
     * @return The MovieEntities
     */
    public static List<MovieEntity> fromMovies (List<Movie> movies) {
        List<MovieEntity> movieEntityList = new ArrayList<>();
        for (Movie movie : movies) {
            MovieEntity movieEntity = new MovieEntity(movie);
            movieEntityList.add(movieEntity);
        }
        return movieEntityList;
    }
    /**
     * Converts MovieEntities -> Movies
     * @param movieEntities The MovieEntities
     * @return The Movies
     */
    public static List<Movie> toMovies (List<MovieEntity> movieEntities) {
        List<Movie> movieList = new ArrayList<>();
        for (MovieEntity movieEntity : movieEntities) {
            Movie movie = new Movie(movieEntity);
            movieList.add(movie);
        }
        return movieList;
    }
}
