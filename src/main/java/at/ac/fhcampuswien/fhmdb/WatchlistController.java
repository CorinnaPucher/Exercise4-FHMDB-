package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
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

public class WatchlistController implements Initializable {
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
            watchlistRepository = new WatchlistRepository();
            List<WatchlistEntity> watchlistEntities = watchlistRepository.getWatchlist();
            MovieRepository movieRepository = new MovieRepository();

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
                Scene scene = new Scene(fxmlLoader.load(), 890, 620);
                Stage root = (Stage) homeButton.getScene().getWindow();
                root.setScene(scene);
            } catch (IOException e) {
                messageForUser(Alert.AlertType.ERROR, "Alles kaputt");
            }
        });
    }
    // Intitializes all Cells
    private void initializeCellFactory() {
        ClickEventHandler <WatchListCell> removeFromWatchlistClicked = (watchListCell) -> {
            try {
                WatchlistRepository watchlistRepository = new WatchlistRepository();
                watchlistRepository.removeFromWatchlist(watchListCell.getItem().id);
                watchListCell.setText(null);
                watchListCell.setGraphic(null);
            } catch (DatabaseException e) {
                messageForUser(Alert.AlertType.ERROR, e.getMessage());
            }
        };
        movieListView.setCellFactory(movieListView -> new WatchListCell(removeFromWatchlistClicked)); // use custom cell factory to display data
    }
}