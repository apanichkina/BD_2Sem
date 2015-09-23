package Connection;
import Exception.PostException;
import javafx.geometry.Pos;

import java.util.ArrayList;

/**
 * Created by olegermakov on 23.09.15.
 */
public class Permission {
    public static final void AdminPermission(String SessionID, AccountService CurrentBase) throws PostException {
        UserProfile tempUser = CurrentBase.getSessions(SessionID);
        if(tempUser == null || tempUser.getLogin()!="Admin")
            throw new PostException("401");
    }

    public static final void NotLoggedIn(String SessionID, AccountService CurrentBase) throws PostException {
        UserProfile tempUser = CurrentBase.getSessions(SessionID);
        if(tempUser == null)
            throw new PostException("402");
    }

    /*
    public static final void NicknameIsEnabled(String Name, AccountService CurrentBase) throws PostException {
        UserProfile tempUser = CurrentBase.getUser("Name");
        if(tempUser != null)
            throw new PostException("403");
    }
    */

    public static final void RequestParams(ArrayList<Object> Objects) throws PostException {
        for(Object i : Objects) {
            if (i == null)
                throw new PostException("404");
            if(i.getClass() == String.class && i == "")
                throw new PostException("111");
        }
    }
}
