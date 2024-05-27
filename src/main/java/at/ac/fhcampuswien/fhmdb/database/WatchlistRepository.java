package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {
    Dao<WatchlistEntity, Long> dao;
    private static WatchlistRepository watchlistInstance;
    private List<Observer> observerList = new ArrayList<>();

    private WatchlistRepository() throws DatabaseException {
        this.dao = DatabaseManager.getDatabase().getWatchlistDao();
    }

    public static WatchlistRepository getWatchlistRepository() throws DatabaseException {
        if(watchlistInstance == null) {
            watchlistInstance = new WatchlistRepository();
        }
        return watchlistInstance;
    }

    /**
     * Adds WatchListEntity to Database
     * @param movie WatchListEntity
     */
    public int addToWatchlist(WatchlistEntity movie) {
        try{
            dao.createIfNotExists(movie);
            notifyObservers("Movie added to watchlist");
        }catch (java.sql.SQLException e){
            notifyObservers("Movie already exists in watchlist");
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
            notifyObservers("Movie removed from watchlist");
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

    @Override
    public void attachObserver(Observer observer) {
        observerList.add(observer);

    }

    @Override
    public void detachObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for(Observer observer : observerList) {
            observer.update(message);
        }
    }
}
