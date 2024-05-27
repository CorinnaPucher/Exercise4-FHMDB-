package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface State {
    void sort(List<Movie> movies);
}
