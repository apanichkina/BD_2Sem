package Connection;

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
            addUser(Admin.getLogin(), Admin);
        }

        public boolean addUser(String userName, UserProfile userProfile) {
            if (users.containsKey(userName))
                return false;
            users.put(userName, userProfile);
            return true;
        }

        public void addSessions(String sessionId, UserProfile userProfile) {
            sessions.put(sessionId, userProfile);
        }

        public boolean deleteSessions(String sessionId) {
            if(getSessions(sessionId)!=null) {
                sessions.remove(sessionId);
                return true;
            }
            else
            {
                return false;
            }
        }

        public UserProfile getUser(String userName) {
            return users.get(userName);
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
