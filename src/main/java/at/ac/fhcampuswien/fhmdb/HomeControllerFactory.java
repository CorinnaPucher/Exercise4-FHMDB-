package at.ac.fhcampuswien.fhmdb;
import javafx.util.Callback;

public class HomeControllerFactory implements Callback<Class<?>, Object> {
    private static HomeController homeControllerInstance;

    @Override
    public Object call(Class<?> aClass) {
        if(homeControllerInstance == null){
            try{
                System.out.println("Ein HomeController wurde erstellt!!");
                homeControllerInstance =  (HomeController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return homeControllerInstance;
    }
}
