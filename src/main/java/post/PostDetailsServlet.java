package post;

import com.google.gson.JsonObject;
import forum.ForumDetailsServlet;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thread.ThreadDetailsServlet;
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
 * Created by anna on 15.10.15.
 */
public class PostDetailsServlet extends HttpServlet {


    public static boolean PostDet(int curr_id, @Nullable JsonObject responseJSON, Connection con, HashSet<String> related) throws com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException, IOException, SQLException {

        Boolean allOK = false;
//        String query_postDetails = "SELECT Post.* , User.email, Forum.short_name FROM Post \n" +
//                "LEFT JOIN User ON User.id=Post.authorID \n" +
//                "LEFT JOIN Forum ON Forum.id=Post.forumID\n" +
//                "WHERE Post.id=?";

        String query_postDetails = "SELECT Post.* FROM Post WHERE id =?";
//        String query_postDetails = "SELECT Post.date, Post.threadID, Post.message, Post.author_email, \n" +
//                "Post.authorID, Post.forumID, Post.parentID, Post.isApproved, \n" +
//                "Post.isDelited, Post.isEdited, \n" +
//                "Post.isHighlighted, Post.isSpam, Post.likes, Post.dislikes, Post.points, \n" +
//                "Forum.short_name FROM Post inner JOIN Forum ON Forum.id=Post.forumID WHERE Post.id =?";
        try(PreparedStatement stmt = con.prepareStatement(query_postDetails);)
        {
            stmt.setInt(1, curr_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                allOK = true;
                responseJSON.addProperty("id", curr_id);
                responseJSON.addProperty("date", rs.getString("date"));
                if (related.contains("thread")) {
                    JsonObject thread_relatedJSON = new JsonObject();
                    ThreadDetailsServlet.ThreadDet(rs.getInt("threadID"), thread_relatedJSON, con, new HashSet<String>()); //TODO после создания Thread реализовать
                    responseJSON.add("thread", thread_relatedJSON);
                } else responseJSON.addProperty("thread", rs.getInt("threadID"));
                if (related.contains("forum")) {
                    JsonObject forum_relatedJSON = new JsonObject();
                    ForumDetailsServlet.ForumDet(rs.getInt("forumID"), forum_relatedJSON, con, new HashSet<String>()); //TODO проверить как быстрее с join или так
                    responseJSON.add("forum", forum_relatedJSON);
                } else responseJSON.addProperty("forum", rs.getString("forum_short_name"));

                responseJSON.addProperty("message", rs.getString("message"));

                int parent = rs.getInt("parentID");
                if (parent == 0) responseJSON.addProperty("parent", rs.getString("parentID"));
                else responseJSON.addProperty("parent", parent);

                responseJSON.addProperty("isApproved", rs.getBoolean("isApproved"));
                responseJSON.addProperty("isHighlighted", rs.getBoolean("isHighlighted"));
                responseJSON.addProperty("isEdited", rs.getBoolean("isEdited"));
                responseJSON.addProperty("isSpam", rs.getBoolean("isSpam"));
                responseJSON.addProperty("isDeleted", rs.getBoolean("isDelited"));
                responseJSON.addProperty("likes", rs.getInt("likes"));
                responseJSON.addProperty("dislikes", rs.getInt("dislikes"));
                responseJSON.addProperty("points", rs.getInt("points"));
                if (related.contains("user")) {
                    JsonObject user_relatedJSON = new JsonObject();
                    UserDetailsServlet.UsDet(rs.getInt("authorID"), user_relatedJSON, con);
                    responseJSON.add("user", user_relatedJSON);
                } else responseJSON.addProperty("user", rs.getString("author_email"));
            }

            //if (!allOK) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
        }

//        try {
//            if (rs != null) {
//                rs.close();
//            }
//        } catch (SQLException se) {
//        }
            return allOK;
    }

    ;

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);

        HashSet<String> base_related = new HashSet<>();
        base_related.add("user");
        base_related.add("forum");
        base_related.add("thread");

        try (Connection con = Main.mainConnection.getConnection()) {

            String input_id = request.getParameter("post");
            int curr_id = Integer.parseInt(input_id);


            HashSet<String> related = new HashSet<>();
            if (request.getParameter("related") != null) {
                HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
                related = curr_related;
            }

            if (base_related.containsAll(related)) {

                if (PostDet(curr_id, responseJSON, con, related)) result.add("response", responseJSON);
                else APIErrors.ErrorMessager(1, result);
            } else APIErrors.ErrorMessager(3, result);

        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(1, result);
        } catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
