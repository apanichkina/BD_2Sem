package Connection;

/**
 * Created by iHelos on 20.09.2015.
 */
public class UserProfile {
        private String login;           //логин
        private String password;        //пароль            КАПИТАН ОЧЕВИДНОСТЬ
        private String email;           //почта

        public UserProfile(String login, String password, String email) {       //Конструктор юзера
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;                       //Вернуть логин
        }

        public String getPassword() {
            return password;                    //Вернуть пароль
        }

        public String getEmail() {
            return email;
        }   //Вернуть почту
}
