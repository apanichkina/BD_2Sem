package post;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by anna on 15.10.15.
 */
public class PostCreateServlet extends HttpServlet{
    private Connection con = null;
    private String query = "INSERT INTO Post (date,threadID,message,authorID,forumID,isApproved,isHighlighted,isEdited,isSpam,isDelited) \n" +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";

    public PostCreateServlet(Connection connect) {
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", "0");
        result.add("response", responseJSON);

        Gson gson = new Gson();

        String query_with_parent = "INSERT INTO Post (date,threadID,message,authorID,forumID,isApproved,isHighlighted,isEdited,isSpam,isDelited,parentID) \n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {

            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String date = json.get("date").getAsString();
            int threadID = json.get("thread").getAsInt();
            String message = json.get("message").getAsString();
            int authorID = UserDetailsServlet.GetID(json.get("user").getAsString(), "email", "User", con);
            int forumID = UserDetailsServlet.GetID(json.get("forum").getAsString(), "short_name", "Forum", con);
            Integer parentID = null;
            Boolean isApproved = false;
            Boolean isHighlighted = false;
            Boolean isEdited = false;
            Boolean isSpam = false;
            Boolean isDelited = false;



            JsonElement new_isApproved = json.get("isApproved");
            if (new_isApproved != null) {
                isApproved = new_isApproved.getAsBoolean();
            }
            JsonElement new_isHighlighted = json.get("isHighlighted");
            if (new_isHighlighted != null) {
                isHighlighted = new_isHighlighted.getAsBoolean();
            }
            JsonElement new_isEdited = json.get("isEdited");
            if (new_isEdited != null) {
                isEdited = new_isEdited.getAsBoolean();
            }
            JsonElement new_isSpam = json.get("isSpam");
            if (new_isSpam != null) {
                isSpam = new_isSpam.getAsBoolean();
            }
            JsonElement new_isDelited = json.get("isDelited");
            if (new_isDelited != null) {
                isDelited = new_isDelited.getAsBoolean();
            }
            JsonElement new_parentID = json.get("parent");
            if (new_parentID != null) {
                parentID = new_parentID.getAsInt();
                query = query_with_parent;
            }

            stmt = con.prepareStatement(query);
            stmt.setString(1, date);
            stmt.setInt(2, threadID);
            stmt.setString(3, message);
            stmt.setInt(4, authorID);
            stmt.setInt(5, forumID);
            stmt.setBoolean(6, isApproved);
            stmt.setBoolean(7, isHighlighted);
            stmt.setBoolean(8, isEdited);
            stmt.setBoolean(9, isSpam);
            stmt.setBoolean(10, isDelited);
            if (parentID != null) stmt.setInt(11, parentID);

            if (stmt.executeUpdate() != 1) throw new SQLException();

        }
        catch (com.google.gson.JsonSyntaxException jsEx) {
            result.addProperty("code", "2");
            result.addProperty("response", "err2");
        }
        catch (java.lang.NullPointerException npEx) {
            result.addProperty("code", "3");
            result.addProperty("response", "err3");
        }

        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            result.addProperty("code", "3");
            result.addProperty("response", "error3");
        }

        catch (SQLException sqlEx) {
            result.addProperty("code", "4");
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
