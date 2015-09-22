package Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iHelos on 20.09.2015.
 */
public class AccountService {

        private Map<String, UserProfile> users = new HashMap<>();
        private Map<String, UserProfile> sessions = new HashMap<>();

        public boolean addUser(String userName, UserProfile userProfile) {
            if (users.containsKey(userName))
                return false;
            users.put(userName, userProfile);
            return true;
        }

        public void addSessions(String sessionId, UserProfile userProfile) {
            sessions.put(sessionId, userProfile);
        }

        public UserProfile getUser(String userName) {
            return users.get(userName);
        }

        public UserProfile getSessions(String sessionId) {
            return sessions.get(sessionId);
        }
}
