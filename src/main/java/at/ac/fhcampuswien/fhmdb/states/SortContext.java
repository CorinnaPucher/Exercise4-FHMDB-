package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private State state;

    public SortContext() {
        this.state = new UnsortedState(this);
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void sort(List<Movie> movies) {
        state.sort(movies);
    }

    public void ascButtonClicked() {
        state.ascButton();
    }

    public void descButtonClicked() {
        state.descButton();
    }
}
