package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class UnsortedState implements State {
    @Override
    public void sort(List<Movie> movies) {
        // no sorting :)
    }
}
