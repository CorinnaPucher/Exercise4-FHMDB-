package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class UnsortedState extends State {
    public UnsortedState(SortContext context) {
        super(context);
    }

    @Override
    public void sort(List<Movie> movies) {
        // no sorting :)
    }

    @Override
    public void ascButton() {
        this.context.changeState(new AscSortedState(this.context));
    }

    @Override
    public void descButton() {
        this.context.changeState(new DescSortedState(this.context));
    }
}
