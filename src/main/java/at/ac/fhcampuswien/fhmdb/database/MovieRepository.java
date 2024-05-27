package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.MovieAPIRequestBuilder;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    Dao<MovieEntity, Long> dao;
    private static MovieRepository movieInstance;

    private MovieRepository() throws DatabaseException {
        this.dao = DatabaseManager.getDatabase().getMovieDao();
    }

    public static MovieRepository getMovieRepository() throws DatabaseException {
        if(movieInstance == null) {
            movieInstance = new MovieRepository();
        }
        return movieInstance;
    }

    /**
     * Adds all Movies in the Parameters to the Database
     * @param movies The Movies
     * @return How many
     */
    public int addAllMovies(List<Movie> movies) {
        List<MovieEntity> entities = MovieEntity.fromMovies(movies);
        for (MovieEntity movieentity : entities) {
            try{
                dao.createIfNotExists(movieentity);
            }catch (SQLException ignored){}
        }
        return entities.size();
    }
    // Removes all Movies
    public int removeAll () throws DatabaseException {
        try {
            dao.delete(dao.queryForAll());
        } catch (SQLException e) {
            throw new DatabaseException("Movies cannot be removed");
        }
        return 0;
    }
    // Gets all Movies
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't get all movies");
        }
    }

    /**
     * Gets a Movie to an apiId
     * @param apiId The apiId
     * @return The Movie with the apiId
     * @throws DatabaseException If Query for all did not work
     */
    public MovieEntity getMovie(String apiId) throws DatabaseException {
        List<MovieEntity> movieEntityList = null;
        try {
            movieEntityList = dao.queryForAll();
            for (MovieEntity movieEntity : movieEntityList) {
                if(movieEntity.apiId.equals(apiId)) return movieEntity;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Query for all did not work");
        }
        return null;
    }
}
