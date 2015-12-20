package forum;

import com.google.gson.JsonObject;
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
 * Created by anna on 15.10.15.
 */


public class ForumDetailsServlet extends HttpServlet {
    public ForumDetailsServlet() {

    }

    public static void ForumDet(int curr_id, @Nullable JsonObject responseJSON, Connection con, HashSet<String> related) throws IOException, SQLException {

        String query_forumDetails = "SELECT Forum.* , User.email FROM Forum LEFT JOIN User ON User.id=Forum.userID WHERE Forum.id=?";
        try (PreparedStatement stmt = con.prepareStatement(query_forumDetails);) {
            stmt.setInt(1, curr_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                responseJSON.addProperty("id", curr_id);
                responseJSON.addProperty("name", rs.getString("name"));
                responseJSON.addProperty("short_name", rs.getString("short_name"));
                if (related.contains("user")) {
                    JsonObject user_relatedJSON = new JsonObject();
                    UserDetailsServlet.UsDet(rs.getInt("userID"), user_relatedJSON, con);
                    responseJSON.add("user", user_relatedJSON);
                } else responseJSON.addProperty("user", rs.getString("email"));
            }
        }

    }
    public static void ForumDetSN(String curr_name, @Nullable JsonObject responseJSON, Connection con, HashSet<String> related) throws IOException, SQLException {

        String query_forumDetails = "SELECT Forum.* , User.email FROM Forum LEFT JOIN User ON User.id=Forum.userID WHERE Forum.short_name=?";
        try (PreparedStatement stmt = con.prepareStatement(query_forumDetails);) {
            stmt.setString(1, curr_name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                responseJSON.addProperty("id", rs.getInt("id"));
                responseJSON.addProperty("name", rs.getString("name"));
                responseJSON.addProperty("short_name", curr_name);
                if (related.contains("user")) {
                    JsonObject user_relatedJSON = new JsonObject();
                    UserDetailsServlet.UsDet(rs.getInt("userID"), user_relatedJSON, con);
                    responseJSON.add("user", user_relatedJSON);
                } else responseJSON.addProperty("user", rs.getString("email"));
            }
        }

    }
    ;

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        HashSet<String> base_related = new HashSet<>();
        base_related.add("user");

        HashSet<String> related = new HashSet<>();
        if (request.getParameter("related") != null) {
            HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
            related = curr_related;
        }

        try (Connection con = Main.mainConnection.getConnection()) {
            if (!base_related.containsAll(related)) throw new java.lang.NullPointerException();

            String curr_short_name = request.getParameter("forum");
            if (curr_short_name == null) APIErrors.ErrorMessager(3, result);
            else {

                //int curr_id = UserDetailsServlet.GetID(curr_short_name, "short_name", "Forum", con);
//            int curr_id = UserDetailsServlet.GetForumID(curr_short_name, con);
//            if (curr_id == -1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();

                //ForumDet(curr_id, responseJSON, con, related);
                ForumDetSN(curr_short_name, responseJSON, con, related);
            }

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


