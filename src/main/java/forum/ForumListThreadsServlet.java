package forum;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import thread.ThreadDetailsServlet;
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
public class ForumListThreadsServlet extends HttpServlet {
    public ForumListThreadsServlet() {
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

        HashSet<String> related = new HashSet<>();
        if (request.getParameter("related") != null) {
            HashSet<String> curr_related = new HashSet<String>(Arrays.asList(request.getParameterValues("related")));
            related = curr_related;
        }
        try(Connection con = Main.mainConnection.getConnection()) {
            String curr_forum = request.getParameter("forum");
            if (curr_forum == null) throw new NullPointerException();
//            int forumID = UserDetailsServlet.GetID(curr_forum, "short_name", "Forum", con);
            int forumID = UserDetailsServlet.GetForumID(curr_forum, con);
            if (forumID == -1)
                throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
            ThreadListServlet.ThreadList(forumID, "forumID", request, list, con, related);
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
