package post;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import user.UserDetailsServlet;
import user.UserListPostServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by anna on 17.10.15.
 */
public class PostListServlet extends HttpServlet {


    public PostListServlet() {
    }
    public static void PostList(int curr_value, String row_name, HttpServletRequest request, JsonArray list, Connection con, HashSet<String> related) throws IOException, SQLException {

        String query_since = "";
        String query_order = "desc";
        String query_limit = "";
        String since = request.getParameter("since");
        if (since != null) {
            query_since = " and date > '" + since + "'";
        }
        String order = request.getParameter("order");
        if (order != null) {
            query_order = order;
        }
        String limit_input = request.getParameter("limit");
        if (limit_input != null) {
            query_limit = " limit " + limit_input;
        }
        String query_getID = "SELECT id FROM Post where "+row_name+" = ?" + query_since + " order by date "+query_order + query_limit;

        PreparedStatement stmt = con.prepareStatement(query_getID);
        stmt.setInt(1, curr_value);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            JsonObject responceJS = new JsonObject();
            PostDetailsServlet.PostDet(rs.getInt("id"), responceJS, con, related);
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
            String input_threadID = null;
            String curr_forum = request.getParameter("forum");
            if (curr_forum == null) {
                input_threadID = request.getParameter("thread");
                if (input_threadID == null) {
                    //throw new NullPointerException();
                    APIErrors.ErrorMessager(3, result); //TODO нужно ли это проверять?
                }
                else {
                    int threadID = Integer.parseInt(input_threadID);
                    PostList(threadID, "threadID", request, list, con, new HashSet<String>());
                }
            }
            else {
                int forumID = UserDetailsServlet.GetID(curr_forum, "short_name", "Forum", con);
                //if (forumID == -1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
                if (forumID == -1) APIErrors.ErrorMessager(1, result);
                else PostList(forumID, "forumID", request, list, con, new HashSet<String>());
            }
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
