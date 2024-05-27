package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private State state;

    public SortContext(State state) {
        this.state = state;
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void sort(List<Movie> movies) {
        state.sort(movies);
    }
}
