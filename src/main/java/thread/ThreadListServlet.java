package thread;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import post.PostDetailsServlet;
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
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by anna on 18.10.15.
 */
public class ThreadListServlet extends HttpServlet {
    private Connection con = null;

    public ThreadListServlet(Connection connect) {
        con = connect;
    }
    public static void ThreadList(int curr_value, String row_name, HttpServletRequest request, JsonArray list, Connection con, HashSet<String> related) throws IOException, SQLException {

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


        String query_getID = "SELECT id FROM Thread where "+row_name+" = ?" + query_since + " order by date "+query_order + query_limit;

        PreparedStatement stmt = con.prepareStatement(query_getID);
        stmt.setInt(1, curr_value);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            JsonObject responceJS = new JsonObject();
            ThreadDetailsServlet.ThreadDet(rs.getInt("id"), responceJS, con, related);
            list.add(responceJS);
        }


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
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {


        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        JsonArray list = new JsonArray();
        HashSet<String> related = new HashSet<>();
        if (request.getParameter("related") != null) {
            HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
            related = curr_related;
        }

        try {
            String input_user = null;
            String curr_forum = request.getParameter("forum");
            if (curr_forum == null) {
                input_user = request.getParameter("user");
                if (input_user == null) {
                    throw new NullPointerException();
                }
                else {
                    int userID = UserDetailsServlet.GetID(input_user,"email", "User", con);
                    ThreadList(userID, "userID", request, list, con, related);
                }

            }
            else {
                int forumID = UserDetailsServlet.GetID(curr_forum, "short_name", "Forum", con);
                if (forumID == -1)
                    throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
                ThreadList(forumID, "forumID", request, list, con, related);
            }

            result.add("response", list);

        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            result.addProperty("code", 1);
            result.addProperty("response", "error1");
        } catch (java.lang.NullPointerException npEx) {
            result.addProperty("code", 3);
            result.addProperty("response", "er3");
        } catch (SQLException sqlEx) {
            result.addProperty("code", 4);
            result.addProperty("response", "error4");
            sqlEx.printStackTrace();
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);

    }


}
