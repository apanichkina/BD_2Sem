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
import java.sql.*;
import java.util.Formatter;

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
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        Gson gson = new Gson();



        try {

            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String date = json.get("date").getAsString();

            int threadID = json.get("thread").getAsInt();
            String message = json.get("message").getAsString();
            String user = json.get("user").getAsString();
            int authorID = UserDetailsServlet.GetID(user, "email", "User", con);
            String forum = json.get("forum").getAsString();
            int forumID = UserDetailsServlet.GetID(forum, "short_name", "Forum", con);
            Integer parentID = null;
            Boolean isApproved = false;
            Boolean isHighlighted = false;
            Boolean isEdited = false;
            Boolean isSpam = false;
            Boolean isDelited = false;
            int first_path = 0;
            String path = "";


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
            JsonElement new_isDelited = json.get("isDeleted");
            if (new_isDelited != null) {
                isDelited = new_isDelited.getAsBoolean();
            }
            JsonElement new_parentID = json.get("parent");
            if (new_parentID != null) {
                parentID = new_parentID.getAsInt();
            }





            String query_with_parent = "INSERT INTO Post (date,threadID,message,authorID,forumID,isApproved,isHighlighted,isEdited,isSpam,isDelited,parentID) \n" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,"+parentID+")";
            stmt = con.prepareStatement(query_with_parent, Statement.RETURN_GENERATED_KEYS);
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



            if (stmt.executeUpdate() != 1) throw new SQLException();

            rs = stmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            responseJSON.addProperty("id", id);
            responseJSON.addProperty("date", date);
            responseJSON.addProperty("thread", threadID);
            responseJSON.addProperty("message", message);
            responseJSON.addProperty("user", user);
            responseJSON.addProperty("forum", forum);
            responseJSON.addProperty("parent", parentID);
            responseJSON.addProperty("isApproved", isApproved);
            responseJSON.addProperty("isHighlighted", isHighlighted);
            responseJSON.addProperty("isEdited", isEdited);
            responseJSON.addProperty("isSpam", isSpam);
            responseJSON.addProperty("isDeleted", isDelited);



            if (parentID != null) {

                String query_parentPost = "SELECT path as parent_path, count_of_children as pos, first_path as parent_firstPath FROM Post WHERE id = ?";
                stmt = con.prepareStatement(query_parentPost);
                stmt.setInt(1, parentID);
                rs = stmt.executeQuery();

                rs.next();
                String parent_path = rs.getString("parent_path");
                int position = rs.getInt("pos");
                int parent_firstPath = rs.getInt("parent_firstPath");

                Formatter position_fmt = new Formatter();
                position_fmt.format("%07d", position + 1);
                path = parent_path + position_fmt;
                first_path = parent_firstPath;
            }
            else {
                first_path = id;

            }

            String query_updatePath = "UPDATE Post SET path = ?, first_path = ? WHERE id = ?";
            stmt = con.prepareStatement(query_updatePath);
            stmt.setString(1, path);
            stmt.setInt(2, first_path);
            stmt.setInt(3, id);
            stmt.executeUpdate();



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
