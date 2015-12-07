package post;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import thread.ThreadDetailsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by anna on 17.10.15.
 */
public class PostUpdateServlet extends HttpServlet {

    public PostUpdateServlet() {

    }
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        try(Connection con = Main.mainConnection.getConnection()) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String new_message = json.get("message").getAsString();
            int curr_id = json.get("post").getAsInt();

            String query_update = "UPDATE `Post` SET message = ? WHERE id= ?";
            stmt = con.prepareStatement(query_update);
            stmt.setString(1, new_message);
            stmt.setInt(2, curr_id);
            //if (stmt.executeUpdate() != 1) throw new java.lang.NullPointerException();
            if (stmt.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
            else PostDetailsServlet.PostDet(curr_id, responseJSON, con, new HashSet<String>());

        } catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        }
        catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(3, result);
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
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
