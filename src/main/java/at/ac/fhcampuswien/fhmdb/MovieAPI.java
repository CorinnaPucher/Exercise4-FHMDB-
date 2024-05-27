package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import okhttp3.*;


import java.io.IOException;
import java.util.List;

public class MovieAPI {
    /**
     * Sends GET request to the server and returns the response as a JSON String
     *
     * @return JSON String
     */
    public static String sendRequest() throws MovieApiException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://prog2.fh-campuswien.ac.at/movies")
                .addHeader("User-Agent", "http.agent")
                .build();

        Call call = client.newCall(request);

        try (Response response = call.execute()) {
            return response.body().string();
        } catch (IOException io) {
            throw new MovieApiException();
        }

    }

    /**
     * Sends GET request to the server including parameters and returns the response as a JSON String
     *
     * @param query       search value title
     * @param genre       genre
     * @param releaseYear released year (-1 = no input)
     * @param ratingFrom  movie rating (-1 = no input)
     * @return JSON String
     */
    public static String sendRequest(String query, String genre, int releaseYear, double ratingFrom) throws MovieApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://prog2.fh-campuswien.ac.at/movies?");
        if (!query.equals("")) {
            stringBuilder.append("query=" + query + "&");
        }
        if (!genre.equals("")) {
            stringBuilder.append("genre=" + genre + "&");
        }
        if (releaseYear != -1) {
            stringBuilder.append("releaseYear=" + releaseYear + "&");
        }
        if (ratingFrom != -1) {
            stringBuilder.append("ratingFrom=" + ratingFrom);
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(stringBuilder.toString())
                .addHeader("User-Agent", "http.agent")
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            return response.body().string();
        } catch (IOException io) {
            throw new MovieApiException();
        }

    }

}
