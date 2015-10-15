package frontend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by anna on 14.10.15.
 */
public class UserCreate  extends HttpServlet{

        private Connection con = null;

        public UserCreate(Connection connect) {
            con = connect;
        }

        public static PreparedStatement stmt = null;
        public static ResultSet rs = null;
        @Override
        public void doPost(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response) throws ServletException, IOException {
            JsonObject result = new JsonObject();
            JsonObject responseJSON = new JsonObject();
            result.addProperty("code", "0");
            result.add("response", responseJSON);

            Gson gson = new Gson();
            try {

            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String email = json.get("email").getAsString();
            String username = json.get("username").getAsString();
            String about = json.get("about").getAsString();
            String name = json.get("name").getAsString();
            Boolean anonymous = false;

            JsonElement new_anonymous = json.get("isAnonymous");
            if (new_anonymous != null) {
                anonymous = new_anonymous.getAsBoolean();
            }

            String query = "INSERT INTO User (email, username, about, name, isAnonymous) VALUES(?,?,?,?,?)";

                    stmt = con.prepareStatement(query);
                    stmt.setString(1, email);
                    stmt.setString(2, username);
                    stmt.setString(3, about);
                    stmt.setString(4, name);
                    stmt.setBoolean(5, anonymous);
                    if (stmt.executeUpdate() != 1) throw new SQLException();
            }
            catch (com.google.gson.JsonSyntaxException jsEx) {
                result.addProperty("code", "2");
                result.addProperty("response", "err2");
            }
            catch (java.lang.NullPointerException npEx) {
                result.addProperty("code", "3");
                result.addProperty("response", "err3");
            }
            catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
                result.addProperty("code", "5");
                result.addProperty("response", "err5");
            }
            catch (SQLException sqlEx) {
                result.addProperty("code", "4");
                result.addProperty("response", "err4");

                sqlEx.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException se) {}
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException se) {}
            }

            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(result);


        }
}
