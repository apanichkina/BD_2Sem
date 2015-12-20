package thread;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import user.UserDetailsServlet;

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
 * Created by anna on 16.10.15.
 */
public class ThreadVoteServlet extends HttpServlet {

    public ThreadVoteServlet() {
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        String query_likes = "UPDATE Thread SET likes = likes+1, points=points+? WHERE id=?";
        String query_dislikes = "UPDATE Thread SET dislikes = dislikes+1, points=points+? WHERE id=?";
        try (Connection con = Main.mainConnection.getConnection();
             PreparedStatement stmt_likes = con.prepareStatement(query_likes);
             PreparedStatement stmt_dislikes = con.prepareStatement(query_dislikes);) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int threadID = json.get("thread").getAsInt();
            if (threadID < 1) APIErrors.ErrorMessager(3, result);
            else {
                int vote = json.get("vote").getAsInt();
                if (vote > 0) {
                    stmt_likes.setInt(1, vote);
                    stmt_likes.setInt(2, threadID);
                    if (stmt_likes.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
                } else {
                    stmt_dislikes.setInt(1, vote);
                    stmt_dislikes.setInt(2, threadID);
                    if (stmt_dislikes.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
                }
            }
        } catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        } catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
