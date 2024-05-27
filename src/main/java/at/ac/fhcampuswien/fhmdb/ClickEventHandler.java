package at.ac.fhcampuswien.fhmdb;

// Functional Interface to define Onclick of Button in Cells
@FunctionalInterface
public interface ClickEventHandler <T> {
    void onClick(T t);
}
