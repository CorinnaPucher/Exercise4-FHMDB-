package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {
    public static final String DB_URL = "jdbc:h2:file: ./src/main/java/at/ac/fhcampuswien/fhmdb/database/moviedb";
    public static final String USER = "cammona";
    public static final String PASSWORD = "tong";
    private static ConnectionSource connectionSource;
    Dao<MovieEntity, Long> movieDao;
    Dao<WatchlistEntity, Long> watchlistDao;
    private static DatabaseManager instance;

    /**
     * Constructor that creates the Database + Connection and DAOs (Only used in getDatabse())
     * @throws DatabaseException Connection Failed
     */
    private DatabaseManager() throws DatabaseException {
        try {
            createConnection();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistEntity.class);
            createTables();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    /**
     * Returns the current DatabaseManager, and creates it if it does'nt exist
     * @return The Database
     * @throws DatabaseException Connection Failed
     */
    public static DatabaseManager getDatabase() throws DatabaseException {
        if(instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    public Dao<WatchlistEntity, Long> getWatchlistDao() {
        return this.watchlistDao;
    }

    /**
     * Creates Tables for every Entity (if it does not exist)
     * @throws DatabaseException If Tables couldn't be created
     */
    private static void createTables() throws DatabaseException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Tables cannot be created :(");
        }

    }

    /**
     * Creates a Connection to the database
     * @throws DatabaseException If the connection didn't work
     */
    private static void createConnection() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Connection didn't work");
        }
    }
}
