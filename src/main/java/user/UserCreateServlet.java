package user;

/**
 * Created by anna on 15.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


public class UserCreateServlet  extends HttpServlet {
    public UserCreateServlet(){};
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();

        Boolean anonymous = false;
        String username = null;
        String name = null;
        String about = null;
        String query = "INSERT INTO User (email, username, about, name, isAnonymous) VALUES(?,?,?,?,?)";

        try (Connection con = Main.mainConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);)  {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String email = json.get("email").getAsString();
            JsonElement new_anonymous = json.get("isAnonymous");
            if (new_anonymous != null) {
                anonymous = new_anonymous.getAsBoolean();
            }
            if (anonymous == false) {
                username = json.get("username").getAsString();
                about = json.get("about").getAsString();
                name = json.get("name").getAsString();
            }

            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, about);
            stmt.setString(4, name);
            stmt.setBoolean(5, anonymous);
//            if (stmt.executeUpdate() != 1)
//                throw new SQLException();
            stmt.executeUpdate();
//            int curr_id = UserDetailsServlet.GetID(email, "email", "User", con);
            int curr_id = UserDetailsServlet.GetUserID(email, con);
            if (curr_id == -1)  APIErrors.ErrorMessager(5, result);
            else {
                UserDetailsServlet.UsDet(curr_id, responseJSON, con);
            }
        }
        catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        }
        catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(5, result);
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


    }
}
