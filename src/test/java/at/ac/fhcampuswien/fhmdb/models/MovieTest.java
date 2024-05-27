package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private List<Movie> createMovieList() {
        Movie theGodfather = new Movie(
                "a00b56aa-0eaf-4332-a02d-736910950128",
                "The Godfather",
                new String[] {"Drama"},
                1972,
                "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                175,
                new String[] {"Francis Ford Coppola"},
                new String[] {"Mario Puzo", "Francis Ford Coppola"},
                new String[] {"Marlon Brando", "Al Pacino", "James Caan", "Leonardo DiCaprio"},
                9.2
        );


        Movie shawshankRedemption = new Movie(
                "16f94a79-7804-4d73-bab9-6cf415b30182",
                "The Shawshank Redemption",
                new String[] {"Drama"},
                1994,
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg",
                142,
                new String[] {"Christopher Nolan"},
                new String[] {"Stephen King", "Frank Darabont"},
                new String[] {"Tim Robbins", "Morgan Freeman", "Bob Gunton", "Leonardo DiCaprio"},
                9.3
        );


        Movie theDarkKnight = new Movie(
                "8ca193d8-7879-42ed-820e-6230b52746a3",
                "The Dark Knight",
                new String[] {"Action", "Crime", "Drama"},
                2008,
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, the caped crusader must come to terms with one of the greatest psychological tests of his ability to fight injustice.",
                "https://m.media-amazon.com/images/M/MV5BMTk4ODQzNDY3Ml5BMl5BanBnXkFtZTcwODA0NTM4Nw@@._V1_FMjpg_UX1000_.jpg",
                152,
                new String[] {"Christopher Nolan"},
                new String[] {"Jonathan Nolan", "Christopher Nolan"},
                new String[] {"test", "Aaron Eckhart"},
                9.0
        );
        Movie empireStrikesBack = new Movie(
                "4bde30e9-c433-4288-b576-7a51da9e71c8",
                "Star Wars: Episode V - The Empire Strikes Back",
                new String[] {"Action", "Adventure", "Fantasy", "Science Fiction"},
                1980,
                "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader and a bounty hunter named Boba Fett all over the galaxy.",
                "https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                124,
                new String[] {"Irvin Kershner"},
                new String[] {"Leigh Brackett", "Lawrence Kasdan", "George Lucas"},
                new String[] {"Mark Hamill", "Harrison Ford", "Carrie Fisher"},
                8.7
        );
        return Arrays.asList(theGodfather, theDarkKnight, shawshankRedemption, empireStrikesBack);
    }

    @Test
    void initializeMovies_returns_not_null() {
        // If an Object is returned
        try {
            assertNotNull(Movie.initializeMovies());
        } catch (MovieApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void initializeMovies_returns_array_that_is_not_empty() {
        int expected = 0;
        int actual = 0;
        try {
            actual = Movie.initializeMovies().size();
        } catch (MovieApiException e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(expected, actual);
    }
    @Test
    void if_compareTo_sorts_list_ascending() {
        List<Movie> moviesActual = new ArrayList<>();
        moviesActual.add(new Movie("Test Titel 1"));
        moviesActual.add(new Movie("C'est moi et mon amie"));
        moviesActual.add(new Movie("Mach das"));

        Collections.sort(moviesActual);

        List<Movie> moviesExpected = new ArrayList<>();
        moviesExpected.add(new Movie("C'est moi et mon amie"));
        moviesExpected.add(new Movie("Mach das"));
        moviesExpected.add(new Movie("Test Titel 1"));

        assertEquals(moviesExpected,moviesActual);
    }
    @Test
    void if_compareTo_sorts_list_descending() {
        List<Movie> moviesActual = new ArrayList<>();
        moviesActual.add(new Movie("Test Titel 1"));
        moviesActual.add(new Movie("C'est moi et mon amie"));
        moviesActual.add(new Movie("Mach das"));

        Collections.sort(moviesActual);
        Collections.reverse(moviesActual);

        List<Movie> moviesExpected = new ArrayList<>();
        moviesExpected.add(new Movie("Test Titel 1"));
        moviesExpected.add(new Movie("Mach das"));
        moviesExpected.add(new Movie("C'est moi et mon amie"));

        assertEquals(moviesExpected,moviesActual);
    }

    @Test
    void if_query_is_filtering () {
        HomeController controller = new HomeController();
        controller.filter("godfather", "", -1, -1);
        assertEquals(controller.getObservableMovies().get(0).title, "The Godfather");
    }
    @Test
    void if_genre_is_filtering () {
        HomeController controller = new HomeController();
        controller.filter("god", "CRIME", -1, -1);
        assertEquals(controller.getObservableMovies().get(0).title, "City of God");
    }
    @Test
    void if_releaseYear_is_filtering () {
        HomeController controller = new HomeController();
        controller.filter("", "", 1997, -1);
        assertEquals(controller.getObservableMovies().get(0).title, "Life Is Beautiful");
    }
    @Test
    void if_ratingFrom_is_filtering () {
        HomeController controller = new HomeController();
        controller.filter("", "", -1, 9.3);
        assertEquals(controller.getObservableMovies().get(0).title, "The Shawshank Redemption");
    }
    @Test
    void if_we_can_find_most_occuring_actor () {
        HomeController controller = new HomeController();

        List<Movie> testList = createMovieList();
        String expected = "Leonardo DiCaprio";
        String actual = controller.getMostPopularActor(testList);

        assertEquals(expected, actual);
    }
    @Test
    void if_we_can_get_longest_movietitle () {
        HomeController controller = new HomeController();

        List<Movie> testList = createMovieList();
        int expected = 46;
        int actual = controller.getLongestMovieTitle(testList);

        assertEquals(expected, actual);
    }
    @Test
    void if_we_can_get_movies_from_range () {
        HomeController controller = new HomeController();
        List<Movie> testList = createMovieList();
        long expected = 2;
        long actual = controller.countMoviesFrom(testList, "Christopher Nolan");

        assertEquals(expected, actual);
    }
    @Test
    void if_we_can_get_movies_between_years () {
        HomeController controller = new HomeController();
        List<Movie> testList = createMovieList();

        List<Movie> expected = new ArrayList<>();
        expected.add(new Movie("The Godfather"));
        expected.add(new Movie("Star Wars: Episode V - The Empire Strikes Back"));


        List<Movie> actual = controller.getMoviesBetweenYears(testList, 1971, 1980);

        assertEquals(expected, actual);
    }
}