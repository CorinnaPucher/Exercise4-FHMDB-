package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public abstract class State {
    protected SortContext context;
    public State(SortContext context){
        this.context = context;
    }
    public abstract void sort(List<Movie> movies);
    public abstract void ascButton();
    public abstract void descButton();
}
