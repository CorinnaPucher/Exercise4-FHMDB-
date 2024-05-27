package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.WatchListCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WatchlistController implements Initializable, Observer {
    @FXML
    public JFXListView movieListView;
    private WatchlistRepository watchlistRepository;

    public List<Movie> allMovies = new ArrayList<>();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    @FXML
    public Button homeButton;

    // Messages the User per Alert
    private void messageForUser(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Adds all WatchListEntities to observableMovies
        try {
            WatchlistRepository.getWatchlistRepository().attachObserver(this);
            watchlistRepository = WatchlistRepository.getWatchlistRepository();
            List<WatchlistEntity> watchlistEntities = watchlistRepository.getWatchlist();
            MovieRepository movieRepository = MovieRepository.getMovieRepository();

            for (WatchlistEntity entity: watchlistEntities) {
                MovieEntity foundMovie = movieRepository.getMovie(entity.apiId);
                if(foundMovie != null){
                    observableMovies.add(new Movie(foundMovie));
                }
            }
        } catch (DatabaseException e) {
            messageForUser(Alert.AlertType.WARNING, e.getMessage());
        }

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        initializeCellFactory();
        homeButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
                fxmlLoader.setControllerFactory(new HomeControllerFactory());
                Scene scene = new Scene(fxmlLoader.load(), 890, 620);
                Stage root = (Stage) homeButton.getScene().getWindow();
                WatchlistRepository.getWatchlistRepository().detachObserver(this);
                root.setScene(scene);
            } catch (IOException e) {
                messageForUser(Alert.AlertType.ERROR, "Alles kaputt");
            } catch (DatabaseException e) {
                messageForUser(Alert.AlertType.INFORMATION, e.getMessage());
            }
        });
    }
    // Intitializes all Cells
    private void initializeCellFactory() {
        ClickEventHandler <WatchListCell> removeFromWatchlistClicked = (watchListCell) -> {
            try {
                WatchlistRepository watchlistRepository = WatchlistRepository.getWatchlistRepository();
                watchlistRepository.removeFromWatchlist(watchListCell.getItem().id);
                watchListCell.setText(null);
                watchListCell.setGraphic(null);
            } catch (DatabaseException e) {
                messageForUser(Alert.AlertType.ERROR, e.getMessage());
            }
        };
        movieListView.setCellFactory(movieListView -> new WatchListCell(removeFromWatchlistClicked)); // use custom cell factory to display data
    }

    @Override
    public void update(String message) {
        messageForUser(Alert.AlertType.INFORMATION, message);
    }
}