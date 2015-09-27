package connection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by iHelos on 20.09.2015.
 */
public class UserProfile {
        String login;
        String password;
        String email;

        public UserProfile(String Login, String Password, String Email) {
            this.login = Login;
            this.password = Password;
            this.email = Email;
        }

        @NotNull
        public String getLogin() {
            return login;
        }

        @NotNull
        public String getPassword() {
            return password;
        }

        @Nullable
        public String getEmail() {
            return email;
        }
}
