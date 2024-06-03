package at.ac.fhcampuswien.fhmdb;

import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private static HomeController homeControllerInstance;
    private static WatchlistController watchlistControllerInstance;

    @Override
    public Object call(Class<?> aClass) {
        try {
            if (HomeController.class.isAssignableFrom(aClass)) {
                if (homeControllerInstance == null) {
                    homeControllerInstance = (HomeController) aClass.getDeclaredConstructor().newInstance();
                }
                return homeControllerInstance;
            }

            if (WatchlistController.class.isAssignableFrom(aClass)) {
                if (watchlistControllerInstance == null) {
                    watchlistControllerInstance = (WatchlistController) aClass.getDeclaredConstructor().newInstance();
                }
            }
            return watchlistControllerInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
