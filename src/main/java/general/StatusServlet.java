package general;

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
import java.util.ResourceBundle;

/**
 * Created by anna on 15.10.15.
 */
public class StatusServlet extends HttpServlet {

    public StatusServlet() {
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {


        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        try (Connection con = Main.mainConnection.getConnection();
             PreparedStatement stmt_user = con.prepareStatement("SELECT count(*) as status FROM User");
             PreparedStatement stmt_thread = con.prepareStatement("SELECT count(*) as status FROM Thread");
             PreparedStatement stmt_forum = con.prepareStatement("SELECT count(*) as status FROM Forum");
             PreparedStatement stmt_post = con.prepareStatement("SELECT count(*) as status FROM Post");
        ) {
            ResultSet rs = stmt_user.executeQuery();
            while (rs.next()) {
                responseJSON.addProperty("user", rs.getInt("status"));
            }
            rs = stmt_thread.executeQuery();
            while (rs.next()) {
                responseJSON.addProperty("thread", rs.getInt("status"));
            }
            rs = stmt_forum.executeQuery();
            while (rs.next()) {
                responseJSON.addProperty("forum", rs.getInt("status"));
            }
            rs = stmt_post.executeQuery();
            while (rs.next()) {
                responseJSON.addProperty("post", rs.getInt("status"));
            }

        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
//        finally {
//            try{if (rs != null){
//                rs.close();
//            }
//            } catch(SQLException se) {}
//        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);

    }
}

