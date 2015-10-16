package thread;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import user.UserDetailsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by anna on 15.10.15.
 */
public class ThreadCreateServlet extends HttpServlet {
    private Connection con = null;
    private String query = "INSERT INTO Thread (forumID,title,userID,date,message,slug,isClosed,isDelited) VALUES (?,?,?,?,?,?,?,?)";


    public ThreadCreateServlet(Connection connect) {
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        Gson gson = new Gson();


        try {

            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String forum = json.get("forum").getAsString();
            int forumID = UserDetailsServlet.GetID(forum, "short_name", "Forum", con);
            String title = json.get("title").getAsString();
            String user = json.get("user").getAsString();
            int userID = UserDetailsServlet.GetID(user, "email", "User", con);
            String date = json.get("date").getAsString();
            String message = json.get("message").getAsString();
            String slug = json.get("slug").getAsString();
            Boolean isClosed = json.get("isClosed").getAsBoolean();
            Boolean isDelited = false;


            JsonElement new_isDelited = json.get("isDeleted");
            if (new_isDelited != null) {
                isDelited = new_isDelited.getAsBoolean();
            }

            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, forumID);
            stmt.setString(2, title);
            stmt.setInt(3, userID);
            stmt.setString(4, date);
            stmt.setString(5, message);
            stmt.setString(6, slug);
            stmt.setBoolean(7, isClosed);
            stmt.setBoolean(8, isDelited);


            if (stmt.executeUpdate() != 1) throw new SQLException();

            rs = stmt.getGeneratedKeys();
            rs.next();
            responseJSON.addProperty("id", rs.getInt(1));
            responseJSON.addProperty("forum", forum);
            responseJSON.addProperty("title", title);
            responseJSON.addProperty("isClosed", isClosed);
            responseJSON.addProperty("user", user);
            responseJSON.addProperty("date", date);
            responseJSON.addProperty("message", message);
            responseJSON.addProperty("slug", slug);
            responseJSON.addProperty("isDeleted", isDelited);


        }
        catch (com.google.gson.JsonSyntaxException jsEx) {
            result.addProperty("code", 2);
            result.addProperty("response", "err2");
        }

        catch (java.lang.NullPointerException npEx) {
            result.addProperty("code", 3);
            result.addProperty("response", "err3");
        }

        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            result.addProperty("code", 3);
            result.addProperty("response", "error3");
        }

        catch (SQLException sqlEx) {
            result.addProperty("code", 4);
            result.addProperty("response", "err4");

            sqlEx.printStackTrace();
        } finally {
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

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


    }
}
