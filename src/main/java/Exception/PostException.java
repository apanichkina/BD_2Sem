package Exception;

/**
 * Created by olegermakov on 23.09.15.
 */
public class PostException extends Exception {      //Объявление моей собственной ошибки
    public PostException() { super(); }             //Супер вызывает конструктор класса-родителя, то есть Exception
    public PostException(String message) { super(message); }    //Аналогично, только добавляю сообщение (передаем коды ошибок через message)
}
