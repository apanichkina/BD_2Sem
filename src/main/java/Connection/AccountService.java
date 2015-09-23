package Connection;

import Exception.PostException;
import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iHelos on 20.09.2015.
 */
public class AccountService {

        private Map<String, UserProfile> users = new HashMap<>();
        private Map<String, UserProfile> sessions = new HashMap<>();

        private static final UserProfile Admin = new UserProfile("Admin","pass","pass");

        public AccountService(){
            try {
                addUser(Admin.getLogin(), Admin);
            }
            catch (PostException e) {}
        }

        public void addUser(String userName, UserProfile userProfile) throws PostException{
            if (users.containsKey(userName))
                throw new PostException("ololo");
            else
            users.put(userName, userProfile);
        }

        public void addSessions(String sessionId, UserProfile userProfile) {
            sessions.put(sessionId, userProfile);
        }

        public boolean deleteSessions(String sessionId) throws PostException{
            if(getSessions(sessionId)!=null) {
                sessions.remove(sessionId);
                return true;
            }
            else
            {
                throw new PostException("202");
            }
        }

        public UserProfile getUser(String userName) throws PostException{
            UserProfile tempUser = users.get(userName);
            if (tempUser == null)
                throw new PostException("NO USER");
            return tempUser;
        }

        public UserProfile getSessions(String sessionId) {
            return sessions.get(sessionId);
        }

        public int getRegisteredCount(){
            return users.size();
        }

        public int getLoggedCount(){
            return sessions.size();
        }
}
