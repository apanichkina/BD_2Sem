package post;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    private Connection con = null;

    public PostListServlet(Connection connect) {
        con = connect;
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
            String input_threadID = null;
            String curr_forum = request.getParameter("forum");
            if (curr_forum == null) {
                input_threadID = request.getParameter("thread");
                if (input_threadID == null) {
                    throw new NullPointerException();
                }
                else {
                    int threadID = Integer.parseInt(input_threadID);
                    UserListPostServlet.PostList(threadID,"threadID", request, list, con, new HashSet<String>());
                }

            }
            else {
                int forumID = UserDetailsServlet.GetID(curr_forum, "short_name", "Forum", con);
                if (forumID == -1)
                    throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
                UserListPostServlet.PostList(forumID,"forumID", request, list, con, new HashSet<String>());
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
