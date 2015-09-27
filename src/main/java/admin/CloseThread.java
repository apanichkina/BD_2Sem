package admin;

/**
 * Created by olegermakov on 22.09.15.
 */
public class CloseThread {
    public static void sleep(int period){
        try{
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
