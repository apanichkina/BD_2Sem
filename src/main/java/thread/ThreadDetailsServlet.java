package thread;

import com.google.gson.JsonObject;
import forum.ForumDetailsServlet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by anna on 16.10.15.
 */
public class ThreadDetailsServlet extends HttpServlet {
    private Connection con = null;

    public ThreadDetailsServlet(Connection connect) {
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;

    public static void ThreadDet(int curr_id, @Nullable JsonObject responseJSON , Connection con, HashSet<String> related) throws IOException, SQLException {


        String query_threadDetails = "SELECT Thread.* , User.email, Forum.short_name FROM Thread  \n" +
                "LEFT JOIN User ON User.id=Thread.userID \n" +
                "LEFT JOIN Forum ON Forum.id=Thread.forumID \n" +
                "WHERE Thread.id=?";
        PreparedStatement stmt = con.prepareStatement(query_threadDetails);
        stmt.setInt(1, curr_id);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {
            responseJSON.addProperty("id", curr_id);
            responseJSON.addProperty("date", rs.getString("date"));
            if (related.contains("forum")){
                JsonObject forum_relatedJSON = new JsonObject();
                ForumDetailsServlet.ForumDet(rs.getInt("forumID"), forum_relatedJSON, con, new HashSet<String>()); //TODO проверить как быстрее с join или так
                responseJSON.add("forum",forum_relatedJSON);
            }
            else responseJSON.addProperty("forum", rs.getString("short_name"));

            responseJSON.addProperty("title", rs.getString("title"));
            responseJSON.addProperty("message", rs.getString("message"));
            responseJSON.addProperty("slug", rs.getString("slug"));
            responseJSON.addProperty("isClosed", rs.getBoolean("isClosed"));
            responseJSON.addProperty("isDeleted", rs.getBoolean("isDelited"));
            responseJSON.addProperty("likes", rs.getInt("likes"));
            responseJSON.addProperty("dislikes", rs.getInt("dislikes"));
            responseJSON.addProperty("points", rs.getInt("points"));

            if (related.contains("user")) {
                JsonObject user_relatedJSON = new JsonObject();
                UserDetailsServlet.UsDet(rs.getInt("userID"), user_relatedJSON, con);
                responseJSON.add("user",user_relatedJSON);
            }
            else responseJSON.addProperty("user", rs.getString("email"));
        }
        String query_posts = "SELECT count(id) FROM Post WHERE threadID=? and isDelited=false";
        stmt = con.prepareStatement(query_posts);
        stmt.setInt(1, curr_id);
        rs = stmt.executeQuery();
        rs.next();

        int posts = rs.getInt(1);
        responseJSON.addProperty("posts", posts);
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {}
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException se) {}


    };

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);

        String input_id = request.getParameter("thread");
        int curr_id = Integer.parseInt(input_id);//TODO проперить валидность

        HashSet<String> related = new HashSet<>();
        if (request.getParameter("related") != null) {
            HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
            related = curr_related;
        }


        try {
            ThreadDet(curr_id,responseJSON, con, related);
            result.add("response", responseJSON);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {}
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {}

        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
