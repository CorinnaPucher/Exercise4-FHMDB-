package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Collections;
import java.util.List;

public class DescSortedState implements State{
    @Override
    public void sort(List<Movie> movies) {
        Collections.sort(movies);
        Collections.reverse(movies);
        //
    }
}
