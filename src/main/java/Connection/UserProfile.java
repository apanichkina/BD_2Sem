package Connection;

import org.jetbrains.annotations.Nullable;

/**
 * Created by iHelos on 20.09.2015.
 */
public class UserProfile {
        String login;
        String password;
        String email;

        public UserProfile(String login, String password, String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        @Nullable
        public String getLogin() {
            return login;
        }

        @Nullable
        public String getPassword() {
            return password;
        }

        @Nullable
        public String getEmail() {
            return email;
        }
}
