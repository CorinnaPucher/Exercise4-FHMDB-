package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    Dao<WatchlistEntity, Long> dao;

    public WatchlistRepository() throws DatabaseException {
        this.dao = DatabaseManager.getDatabase().getWatchlistDao();
    }

    /**
     * Adds WatchListEntity to Database
     * @param movie WatchListEntity
     */
    public int addToWatchlist(WatchlistEntity movie) throws DatabaseException {
        try{
            dao.createIfNotExists(movie);
        }catch (java.sql.SQLException e){
            throw new DatabaseException("Movie already exists");
        }
        return 0;
    }
    /**
     * Removes WatchListEntity to Database
     * @param apiID The apiId of the WatchListEntity
     */
    public int removeFromWatchlist (String apiID) throws DatabaseException {
        DeleteBuilder<WatchlistEntity, Long> deleteBuilder = dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("apiId", apiID);
            deleteBuilder.delete();
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't remove from watchlist");
        }
        return 0;
    }
    // Gets all WatchListEntities
    public List<WatchlistEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't get watchlist");
        }
    }
}
