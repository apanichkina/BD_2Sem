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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(queryPost);
        stmt.setInt(1, postID);

        if (stmt.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
        else {
            responseJSON.addProperty("post", postID);
            //Получаем тред этого поста
            int threadID = -1;
            String queryGetThreadID = "Select threadID From Post WHERE id = ?";
            stmt = con.prepareStatement(queryGetThreadID);
            stmt.setInt(1, postID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                //Изменям количесво постов в треде
                stmt = con.prepareStatement(queryThread);
                stmt.setInt(1, rs.getInt("threadID"));
                stmt.executeUpdate();
            }
        }
    }

    ;

    public static void PostRemoveRestoreThread(String param, int threadID, Connection con, JsonObject result, JsonObject responseJSON) throws SQLException {
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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(queryPost);
        stmt.setInt(1, threadID);
        int updateCount = stmt.executeUpdate();

        //Изменям количесво постов в треде
        stmt = con.prepareStatement(queryThread);
        stmt.setInt(1, updateCount);
        stmt.setInt(2, threadID);
        stmt.executeUpdate();
    }

    private String queryPost = "";
    private String queryThread = "";
    private String method = "";

    public PostRemoveServlet(String param) {
        method = param;
//        if (param.equals("remove")) {
//            queryPost = "UPDATE Post SET isDelited=true, delete_count=delete_count+1 WHERE id=?";
//            queryThread = "UPDATE Thread SET posts=posts-1 WHERE id=?";
//        }
//        if (param.equals("restore")) {
//            queryPost = "UPDATE Post SET isDelited=false, delete_count=delete_count-1 WHERE id=?";
//            queryThread = "UPDATE Thread SET posts=posts+1 WHERE id=?";
//        }
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
        Gson gson = new Gson();
        try (Connection con = Main.mainConnection.getConnection()) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int postID = -1;
            postID = json.get("post").getAsInt();
            //if (postID < 0) throw new java.lang.NullPointerException();
            if (postID < 0) APIErrors.ErrorMessager(3, result);
            else {
                PostRemoveRestore(method, postID, con, result, responseJSON);

//                stmt = con.prepareStatement(queryPost);
//                stmt.setInt(1, postID);
//                //if (stmt.executeUpdate() != 1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
//                if (stmt.executeUpdate() != 1) APIErrors.ErrorMessager(3, result);
//                else {
//                    responseJSON.addProperty("post", postID);
//                //Получаем тред этого поста
//                int threadID=-1;
//                String queryGetThreadID = "Select threadID From Post WHERE id = ?";
//                stmt = con.prepareStatement(queryGetThreadID);
//                stmt.setInt(1, postID);
//                stmt.executeQuery();
//                //Изменям количесво постов в треде
//                stmt = con.prepareStatement(queryThread);
//                stmt.setInt(1, threadID);
//                stmt.executeUpdate();
//                }

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
