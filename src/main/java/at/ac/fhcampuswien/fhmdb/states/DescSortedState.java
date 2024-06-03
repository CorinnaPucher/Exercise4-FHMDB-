package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Collections;
import java.util.List;

public class DescSortedState extends State{
    public DescSortedState(SortContext context) {
        super(context);
    }

    @Override
    public void sort(List<Movie> movies) {
        Collections.sort(movies);
        Collections.reverse(movies);
        //
    }

    @Override
    public void ascButton() {
        this.context.changeState(new AscSortedState(this.context));
    }

    @Override
    public void descButton() {
    }
}
