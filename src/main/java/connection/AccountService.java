package connection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by iHelos on 20.09.2015.
 */
public class AccountService {

        @NotNull
        private Map<String, UserProfile> users = new HashMap<>();
        @NotNull
        private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();

        private static final UserProfile ADMIN = new UserProfile("admin","pass","pass");

        public AccountService(){
            addUser(ADMIN.getLogin(), ADMIN);
        }

        public boolean addUser(@NotNull String userName, UserProfile userProfile) {
            if (users.containsKey(userName))
                return false;
            users.put(userName, userProfile);
            return true;
        }

        public void addSessions(@NotNull String sessionId, UserProfile userProfile) {
            sessions.put(sessionId, userProfile);
        }

        public boolean deleteSessions(@NotNull String sessionId) {
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
            return users.get(userName);
        }

        @Nullable
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
