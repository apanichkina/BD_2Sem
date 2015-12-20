package post;

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
 * Created by anna on 17.10.15.
 */
public class PostVoteServlet extends HttpServlet {
    public PostVoteServlet() {

    }
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        PreparedStatement stmt = null;
        try(Connection con = Main.mainConnection.getConnection()) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int postID = 0;
            postID = json.get("post").getAsInt();
            if (postID < 1) APIErrors.ErrorMessager(3, result);
            else {
                int vote = json.get("vote").getAsInt();

                String param = null;
                if (vote > 0) param = "likes";
                else param = "dislikes";

                String query = "UPDATE Post SET " + param + "=" + param + "+1, points=points+" + vote + " WHERE id=?";

                stmt = con.prepareStatement(query);
                stmt.setInt(1, postID);
                if (stmt.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
            }
        }

        catch (com.google.gson.JsonSyntaxException jsEx) {
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
