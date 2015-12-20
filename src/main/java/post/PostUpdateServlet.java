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
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        String query_update = "UPDATE `Post` SET message = ? WHERE id= ?";
        try(Connection con = Main.mainConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(query_update);) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String new_message = json.get("message").getAsString();
            int curr_id = json.get("post").getAsInt();

            stmt.setString(1, new_message);
            stmt.setInt(2, curr_id);

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
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
