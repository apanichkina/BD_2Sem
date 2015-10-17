package user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import post.PostDetailsServlet;

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
public class UserListPostServlet extends HttpServlet {

    private Connection con = null;

    public UserListPostServlet(Connection connect) {
        con = connect;
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
            int limit = Integer.parseInt(limit_input);//TODO проперить валидность
            query_limit = " limit " + limit;
        }


        String query_getID = "SELECT id FROM Post where "+row_name+" = ?" + query_since + " order by date "+query_order + query_limit;
        System.out.print(query_getID);
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
        try {
            String curr_author_email = request.getParameter("user");
            if (curr_author_email == null) throw new NullPointerException();

            int curr_authorID = UserDetailsServlet.GetID(curr_author_email, "email", "User", con);
            if (curr_authorID == -1)
                throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();

            PostList(curr_authorID,"authorID", request, list, con, new HashSet<String>());
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
