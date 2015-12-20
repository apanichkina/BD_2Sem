package user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
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
 * Created by anna on 15.10.15.
 */
public class UserUpdateServlet extends HttpServlet {
    public UserUpdateServlet() {}
    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        try(Connection con = Main.mainConnection.getConnection()) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String new_about = json.get("about").getAsString();
            String new_name = json.get("name").getAsString();
            String curr_email = json.get("user").getAsString();

//            int curr_id = UserDetailsServlet.GetID(curr_email, "email", "User", con);
            int curr_id = UserDetailsServlet.GetUserID(curr_email, con);

            if (curr_id == -1)  APIErrors.ErrorMessager(1, result);
            else {
                String query_updateProfile = "UPDATE `User` SET about = ?, `name`= ? WHERE id= ?";
                stmt = con.prepareStatement(query_updateProfile);
                stmt.setString(1, new_about);
                stmt.setString(2, new_name);
                stmt.setInt(3, curr_id);
                stmt.executeUpdate();

                UserDetailsServlet.UsDet(curr_id, responseJSON, con);

                String query_updateForumAuthors = "UPDATE Forum_Authors SET postAuthorName = ? WHERE postAuthorID= ?";
                stmt = con.prepareStatement(query_updateForumAuthors);
                stmt.setString(1, new_name);
                stmt.setInt(2, curr_id);
                stmt.executeUpdate();
            }

        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(1, result);
        }
        catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        }
        catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3,result);
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4,result);
            sqlEx.printStackTrace();
        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
            }
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
