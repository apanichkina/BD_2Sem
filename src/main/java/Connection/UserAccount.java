package Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iHelos on 20.09.2015.
 */
public class UserAccount {

        private Map<String, GameUser> users = new HashMap<>();
        private Map<String, GameUser> sessions = new HashMap<>();

        public boolean addUser(String userName, GameUser userProfile) {
            if (users.containsKey(userName))
                return false;
            users.put(userName, userProfile);
            return true;
        }

        public void addSessions(String sessionId, GameUser userProfile) {
            sessions.put(sessionId, userProfile);
        }

        public GameUser getUser(String userName) {
            return users.get(userName);
        }

        public GameUser getSessions(String sessionId) {
            return sessions.get(sessionId);
        }
}
