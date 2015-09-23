package Connection;

import Exception.PostException;
import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iHelos on 20.09.2015.
 */
public class AccountService {

        private Map<String, UserProfile> users = new HashMap<>();                                   //храним юзеров (вместо БД) ----временно
        private Map<String, UserProfile> sessions = new HashMap<>();                                //храним сессии залогиненных

        private static final UserProfile Admin = new UserProfile("Admin","pass","pass");            //временный админ

        public AccountService(){
            try {
                addUser(Admin.getLogin(), Admin);                                                   //сразу добавляем админа    ----временно
            }
            catch (PostException e) {}
        }

        public void addUser(String userName, UserProfile userProfile) throws PostException{         //Добавляем юзера в users (регистрация)
            if (users.containsKey(userName))                                                        //Если уже существует юзер
                throw new PostException("ololo");                                                   //то кидаем ошибку
            else
            users.put(userName, userProfile);
        }

        public void addSessions(String sessionId, UserProfile userProfile) {
            sessions.put(sessionId, userProfile);                                                   //Добавили сессию
        }

        public boolean deleteSessions(String sessionId) throws PostException{                       //Удаление сессии (выход пользователя)
            if(getSessions(sessionId)!=null) {
                sessions.remove(sessionId);
                return true;
            }
            else
            {
                throw new PostException("202");
            }
        }

        public UserProfile getUser(String userName) throws PostException{                           //Получение юзера по ключу (никнейму)
            UserProfile tempUser = users.get(userName);
            if (tempUser == null)
                throw new PostException("NO USER");                                                 //Кидаем ошибку, что юзера нет
            return tempUser;
        }

        public UserProfile getSessions(String sessionId) {
            return sessions.get(sessionId);                                 //Получение пользователя по  сессии
        }

        public int getRegisteredCount(){
            return users.size();                                            //число зарегистрированных
        }

        public int getLoggedCount(){
            return sessions.size();
        }           //число залогиненных <!-- TODO исправить кол-во залогиненных (у одного юзера может быть несколько сессий  -->
}
