package at.ac.fhcampuswien.fhmdb;
import javafx.util.Callback;

public class WatchListControllerFactory implements Callback<Class<?>, Object> {
    private static WatchlistController watchListControllerInstance;

    @Override
    public Object call(Class<?> aClass) {
        if(watchListControllerInstance == null){
            try{
                System.out.println("Ein WatchListController wurde erstellt!!");
                watchListControllerInstance =  (WatchlistController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return watchListControllerInstance;
    }
}
