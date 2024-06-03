package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Collections;
import java.util.List;

public class AscSortedState extends State {

    public AscSortedState(SortContext context) {
        super(context);
    }

    @Override
    public void sort(List<Movie> movies) {
        Collections.sort(movies);
    }

    @Override
    public void ascButton() {
    }

    @Override
    public void descButton() {
        this.context.changeState(new DescSortedState(this.context));
    }
}
