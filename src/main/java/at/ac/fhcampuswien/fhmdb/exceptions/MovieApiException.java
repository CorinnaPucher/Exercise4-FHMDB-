package at.ac.fhcampuswien.fhmdb.exceptions;

public class MovieApiException extends Exception{
    public MovieApiException() {
        super("MovieAPI nix working :(");
    }
    public MovieApiException(String message) {
        super(message);
    }
}
