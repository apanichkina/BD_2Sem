package thread;

import com.google.gson.JsonObject;
import forum.ForumDetailsServlet;
import main.APIErrors;
import main.Main;
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

    public ThreadDetailsServlet() {
    }

    public static boolean ThreadDet(int curr_id, @Nullable JsonObject responseJSON, Connection con, HashSet<String> related) throws IOException, SQLException {
        Boolean allOK = false;
        String query_threadDetails = "SELECT Thread.* FROM Thread WHERE Thread.id=?";

        try (PreparedStatement stmt = con.prepareStatement(query_threadDetails)) {
            stmt.setInt(1, curr_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                allOK = true;
                responseJSON.addProperty("id", curr_id);
                responseJSON.addProperty("date", rs.getString("date"));
                responseJSON.addProperty("posts", rs.getInt("posts"));
                if (related.contains("forum")) {
                    JsonObject forum_relatedJSON = new JsonObject();
                    //ForumDetailsServlet.ForumDet(rs.getInt("forumID"), forum_relatedJSON, con, new HashSet<String>());
                    ForumDetailsServlet.ForumDetSN(rs.getString("forum_short_name"), forum_relatedJSON, con, new HashSet<String>());
                    responseJSON.add("forum", forum_relatedJSON);
                } else responseJSON.addProperty("forum", rs.getString("forum_short_name"));
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
                    responseJSON.add("user", user_relatedJSON);
                } else responseJSON.addProperty("user", rs.getString("user_email"));
            }
        }

//        try {
//            if (stmt != null) {
//                stmt.close();
//            }
//        } catch (SQLException se) {
//        }
//        try {
//            if (rs != null) {
//                rs.close();
//            }
//        } catch (SQLException se) {
//        }
        return allOK;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        HashSet<String> base_related = new HashSet<>();
        base_related.add("user");
        base_related.add("forum");
        try (Connection con = Main.mainConnection.getConnection()) {
            String input_id = request.getParameter("thread");
            int curr_id = Integer.parseInt(input_id);

            HashSet<String> related = new HashSet<>();
            if (request.getParameter("related") != null) {
                HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
                related = curr_related;
            }
            if (!base_related.containsAll(related)) APIErrors.ErrorMessager(3, result);
            else {
                if (!ThreadDet(curr_id, responseJSON, con, related)) APIErrors.ErrorMessager(3, result);
            }
        } catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        } catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
//        finally {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//            } catch (SQLException se) {
//            }
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException se) {
//            }
//
//        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
