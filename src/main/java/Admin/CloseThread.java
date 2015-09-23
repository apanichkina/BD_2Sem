package Admin;

/**
 * Created by olegermakov on 22.09.15.
 */

//Выключение сервака
public class CloseThread {
    public static void sleep(int period){
        try{
            Thread.sleep(period);                                               //выключаем поток через время period (мс)
        } catch (InterruptedException e) {
            e.printStackTrace();                                                // <--TODO Понятия не имею зачем-->
        }
    }
}
