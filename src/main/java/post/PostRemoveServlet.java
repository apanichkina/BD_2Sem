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
 * Created by anna on 16.10.15.
 */
public class PostRemoveServlet extends HttpServlet {
    private String method = "";
    public PostRemoveServlet(String param) {
        method = param;
    }

    public static void PostRemoveRestore(String param, int postID, Connection con, JsonObject result, JsonObject responseJSON) throws SQLException {
        String queryPost = "";
        String queryThread = "";
        if (param.equals("remove")) {
            queryPost = "UPDATE Post SET isDelited=true, delete_count=delete_count+1 WHERE id=?";
            queryThread = "UPDATE Thread SET posts=posts-1 WHERE id=?";
        }
        if (param.equals("restore")) {
            queryPost = "UPDATE Post SET isDelited=false, delete_count=delete_count-1 WHERE id=?";
            queryThread = "UPDATE Thread SET posts=posts+1 WHERE id=?";
        }

        String queryGetThreadID = "Select threadID From Post WHERE id = ?";

        try (PreparedStatement stmt_Post = con.prepareStatement(queryPost);
             PreparedStatement stmt_ThreadID = con.prepareStatement(queryGetThreadID);
             PreparedStatement stmt_Thread = con.prepareStatement(queryThread);) {
            stmt_Post.setInt(1, postID);
            if (stmt_Post.executeUpdate() == 1) {
                responseJSON.addProperty("post", postID);
                //Получаем тред этого поста

                stmt_ThreadID.setInt(1, postID);
                ResultSet rs = stmt_ThreadID.executeQuery();
                while (rs.next()) {
                    //Изменям количесво постов в треде
                    stmt_Thread.setInt(1, rs.getInt("threadID"));
                    stmt_Thread.executeUpdate();
                }
            } else APIErrors.ErrorMessager(3, result);
        }
//        finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException se) {
//            }
    }

    public static void PostRemoveRestoreThread(String param, int threadID, Connection con, JsonObject result, JsonObject responseJSON)
            throws SQLException {
        String queryPost = "";
        String queryThread = "";
        if (param.equals("remove")) {
            queryPost = "UPDATE Post SET isDelited=true, delete_count=delete_count+1 WHERE threadID=?";
            queryThread = "UPDATE Thread SET posts=posts-? WHERE id=?";
        }
        if (param.equals("restore")) {
            queryPost = "UPDATE Post SET isDelited=false, delete_count=delete_count-1 WHERE threadID=?";
            queryThread = "UPDATE Thread SET posts=posts+? WHERE id=?";
        }
        try (PreparedStatement stmt_Post = con.prepareStatement(queryPost);
             PreparedStatement stmt_Thread = con.prepareStatement(queryThread);) {
            stmt_Post.setInt(1, threadID);
            int updateCount = stmt_Post.executeUpdate();
            //Изменям количесво постов в треде
            stmt_Thread.setInt(1, updateCount);
            stmt_Thread.setInt(2, threadID);
            stmt_Thread.executeUpdate();
        }
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        try (Connection con = Main.mainConnection.getConnection()) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int postID = -1;
            postID = json.get("post").getAsInt();
            if (postID < 0) APIErrors.ErrorMessager(3, result);
            else {
                PostRemoveRestore(method, postID, con, result, responseJSON);
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
