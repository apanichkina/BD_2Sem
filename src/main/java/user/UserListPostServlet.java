package user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import post.PostDetailsServlet;
import post.PostListServlet;

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
    public UserListPostServlet() {

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
            String curr_author_email = request.getParameter("user");
            //if (curr_author_email == null) throw new NullPointerException();
            if (curr_author_email == null) APIErrors.ErrorMessager(3,result);
            else {
//                int curr_authorID = UserDetailsServlet.GetID(curr_author_email, "email", "User", con);
                int curr_authorID = UserDetailsServlet.GetUserID(curr_author_email, con);
                //if (curr_authorID == -1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
                if (curr_authorID == -1) APIErrors.ErrorMessager(1, result);
                else PostListServlet.PostList(curr_authorID, "authorID", request, list, con, new HashSet<String>());
            }
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(1,result);
        } catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3,result);
        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
