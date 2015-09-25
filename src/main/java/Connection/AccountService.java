package Connection;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iHelos on 20.09.2015.
 */
public class AccountService {

        private Map<String, UserProfile> users = new HashMap<>();
        private Map<String, UserProfile> sessions = new HashMap<>();

        private static final UserProfile admin = new UserProfile("Admin","pass","pass");

        public AccountService(){
            addUser(admin.getLogin(), admin);
        }

        public boolean addUser(@Nullable String userName, UserProfile userProfile) {
            if (users != null && users.containsKey(userName))
                return false;
            assert users != null;
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

        @Nullable
        public UserProfile getUser(String userName) {
            assert users != null;
            return users.get(userName);
        }

        @Nullable
        public UserProfile getSessions(@Nullable String sessionId) {
            assert sessions != null;
            return sessions.get(sessionId);
        }

        public int getRegisteredCount(){
            assert users != null;
            return users.size();
        }

        public int getLoggedCount(){
            assert sessions != null;
            return sessions.size();
        }
}
