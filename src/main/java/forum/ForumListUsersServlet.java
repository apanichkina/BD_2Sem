package forum;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import post.PostDetailsServlet;
import thread.ThreadListServlet;
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
 * Created by anna on 18.10.15.
 */
public class ForumListUsersServlet extends HttpServlet {

    public ForumListUsersServlet() {

    }
    public static void UsersList(int curr_value, HttpServletRequest request, JsonArray list, Connection con) throws IOException, SQLException {

        String query_since = "";
        String query_order = "desc";
        String query_limit = "";
        String since_id = request.getParameter("since_id");
        if (since_id != null) {
            query_since = " and authorID >= " + since_id;
        }
        String order = request.getParameter("order");
        if (order != null) {
            query_order = order;
        }
        String limit_input = request.getParameter("limit");
        if (limit_input != null) {
            query_limit = " limit " + limit_input;
        }

        String query_getID = "SELECT distinct authorID, name FROM Post LEFT JOIN User ON User.id = Post.authorID WHERE forumID = ?";
        PreparedStatement stmt = con.prepareStatement(query_getID + query_since + " order by name "+query_order + query_limit);
        stmt.setInt(1, curr_value);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            JsonObject responceJS = new JsonObject();
            UserDetailsServlet.UsDet(rs.getInt("authorID"), responceJS, con);
            list.add(responceJS);
        }
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
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        JsonArray list = new JsonArray();
        result.add("response", list);
        try(Connection con = Main.mainConnection.getConnection()) {
            String curr_forum = request.getParameter("forum");
            if (curr_forum == null) throw new NullPointerException();

            int forumID = UserDetailsServlet.GetID(curr_forum, "short_name", "Forum", con);
            if (forumID == -1)
                throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
            UsersList(forumID, request, list, con);
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
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
