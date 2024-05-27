package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;
    @FXML
    public TextField yearSearchField;
    @FXML
    public TextField ratingSearchField;
    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    @FXML
    public Button watchButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allMovies = Movie.initializeMovies();
            MovieRepository movieRepository = new MovieRepository();
            movieRepository.addAllMovies(allMovies);
        } catch (DatabaseException e) {
            messageForUser(Alert.AlertType.WARNING, e.getMessage());
        }
        catch (MovieApiException e) {
            // In case of MovieApi failure load from Database
            try {
                allMovies = Movie.initializeMovieDatabase();
                messageForUser(Alert.AlertType.INFORMATION, "Fetching from database");
            } catch (DatabaseException ex) {
                messageForUser(Alert.AlertType.WARNING, e.getMessage());
            }
        }
        observableMovies.addAll(allMovies);

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        initializeCellFactory();

        // add genre filter items
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        initializeButtons();
        addNumericValidation(ratingSearchField);
        addNumericValidation(yearSearchField);
    }
    // Notifies User if something happened per Alert
    private void messageForUser(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println());
    }
    // Intitializes all Cells
    private void initializeCellFactory() {
        ClickEventHandler <Movie> addToWatchlistClicked = (movie) -> {
            try {
                WatchlistRepository watchlistRepository = new WatchlistRepository();
                watchlistRepository.addToWatchlist(new WatchlistEntity(movie.id));
            } catch (DatabaseException e) {
                messageForUser(Alert.AlertType.INFORMATION, "This movie is already in your watchlist!");
            }
        };
        movieListView.setCellFactory(movieListView -> new MovieCell(addToWatchlistClicked)); // use custom cell factory to display data
    }
    // Initializes the Buttons
    private void initializeButtons() {
        // Sort button:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                // sort observableMovies ascending
                Collections.sort(observableMovies);
                sortBtn.setText("Sort (desc)");
            } else {
                // sort observableMovies descending
                Collections.reverse(observableMovies);
                sortBtn.setText("Sort (asc)");
            }
        });

        searchBtn.setOnAction(actionEvent -> {
            String query = searchField.getText().toLowerCase();
            String genre = genreComboBox.getValue() != null ? genreComboBox.getValue().toString() : "";
            int releaseYear = yearSearchField.getText().isEmpty() ? -1 : Integer.parseInt(yearSearchField.getText());
            double ratingFrom = ratingSearchField.getText().isEmpty() ? -1 : Double.parseDouble(ratingSearchField.getText());
            filter(query, genre, releaseYear, ratingFrom);
        });

        // Watchlist button:
        watchButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("watchlist.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 890, 620);
                Stage root = (Stage) watchButton.getScene().getWindow();
                root.setScene(scene);
            } catch (IOException e) {
                messageForUser(Alert.AlertType.ERROR, "Could not switch to watchlist scene");
            }
        });
    }
    // Adds NumericValidation to certain Fields
    private void addNumericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }


    /**
     * Parses all Fields and filters the Movielist accordingly
     */

    public void filter(String query, String genre, int releaseYear, double ratingFrom) {
        observableMovies.clear();

        List<Movie> filteredMovies = new ArrayList<>();
        try {
            filteredMovies = JSONAction.parseJSON(MovieAPI.sendRequest(query, genre, releaseYear, ratingFrom));
        } catch (MovieApiException e) {
            for (Movie movie : allMovies) {
                if (movie.releaseYear != releaseYear && releaseYear != -1) continue;
                if (!movie.title.toLowerCase().contains(query)) continue;
                if (!movie.getGenres().contains(genre)) continue;
                if (movie.rating < ratingFrom && ratingFrom != -1.0) continue;
                filteredMovies.add(movie);
            }
            messageForUser(Alert.AlertType.INFORMATION, "Fetching from database");
        }
        observableMovies.addAll(filteredMovies);
    }

    /**
     * Return actor who most often was in the main cast
     *
     * @param movies all movies
     * @return actor with highest count
     */
    public String getMostPopularActor(List<Movie> movies) {
        Optional<Map.Entry<String, Long>> highestEntry = movies.stream()
                .flatMap(movie -> Arrays.stream(movie.mainCast)) // All mainCastArrays merge into one stream
                .collect(Collectors.groupingBy(e -> e, Collectors.counting())) // Group by Occurences
                .entrySet()  // Maps have to be converted to a Set to convert to Stream
                .stream()
                .max(Map.Entry.comparingByValue());

        if (highestEntry.isPresent()) return highestEntry.get().getKey();
        else return "";
    }

    /**
     * Return title length of the movie with the longest title
     *
     * @param movies all movies
     * @return title length
     */
    public int getLongestMovieTitle(List<Movie> movies) {
        Stream<Movie> streamFromList = movies.stream();

        return streamFromList
                .mapToInt(m -> m.getTitleLength())
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Return count of movies for specified director
     *
     * @param movies   all movies
     * @param director specific director
     * @return count of movies
     */
    public long countMoviesFrom(List<Movie> movies, String director) {
        Stream<Movie> streamFromList = movies.stream();

        return streamFromList
                .filter(movie -> movie.directorIsInsideArray(director))
                .count();
    }

    /**
     * Return movie list with release year between two given years
     *
     * @param movies    all movies
     * @param startYear search value greater or equal
     * @param endYear   search value less or equal
     * @return filtered movie list
     */
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        Stream<Movie> streamFromList = movies.stream();

        return streamFromList
                .filter(movie -> (movie.releaseYear >= startYear && movie.releaseYear <= endYear))
                .toList();
    }

    public ObservableList<Movie> getObservableMovies() {
        return observableMovies;
    }
}