package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Class which enables us to read JSON Files into Movie Objects
 */
public class JSONAction {
    // Method which return a JSON List if you provide a JSON String
    public static List<Movie> parseJSON(String response){
        // Initialize GSON Object to convert JSON to JSONMovie object
        Gson gson = new Gson();
        List<Movie> movies = new ArrayList<>();

        // Provide the Type of Object which it should return for us to parse (JSONMovie)
        Type movieListType = new TypeToken<List<JSONMovie>>(){}.getType();

        // Parse JSON file and deserialize it into a list of Movie objects
        List<JSONMovie> jMovies = gson.fromJson(response, movieListType);
        for (JSONMovie jMovie : jMovies) {
            movies.add(new Movie(jMovie));
        }

        return movies;
    }
}
