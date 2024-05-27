package at.ac.fhcampuswien.fhmdb.observer;

public interface Observable {
    void attachObserver(Observer observer);
    void detachObserver(Observer observer);
    void notifyObservers(String message);
}
