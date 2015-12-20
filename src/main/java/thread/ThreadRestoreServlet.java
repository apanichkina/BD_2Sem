package thread;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import post.PostRemoveServlet;

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
public class ThreadRestoreServlet extends HttpServlet {

    public ThreadRestoreServlet() {
    }
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        String query = "UPDATE Thread SET isDelited=false WHERE id=?";

        try (Connection con = Main.mainConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(query);) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int threadID = json.get("thread").getAsInt();
            stmt.setInt(1, threadID);
            if (stmt.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
            else {
                PostRemoveServlet.PostRemoveRestoreThread("restore", threadID, con, result, responseJSON);
                responseJSON.addProperty("thread", threadID);
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
