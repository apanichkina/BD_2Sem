package post;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import user.UserDetailsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Formatter;

/**
 * Created by anna on 15.10.15.
 */
public class PostCreateServlet extends HttpServlet {

    public PostCreateServlet() {
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        Gson gson = new Gson();
        Integer parentID = null;
        Boolean isApproved = false;
        Boolean isHighlighted = false;
        Boolean isEdited = false;
        Boolean isSpam = false;
        Boolean isDelited = false;
        int first_path = 0;
        String path = "";

        String query_with_parent = "INSERT INTO Post (date,threadID,message,authorID,forumID,isApproved,isHighlighted,isEdited,isSpam,isDelited,author_email,forum_short_name,parentID) \n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String query_parentPost = "SELECT path as parent_path, count_of_children as pos, first_path as parent_firstPath FROM Post WHERE id = ?";
        String query_updatePath = "UPDATE Post SET path = ?, first_path = ? WHERE id = ?";
        String query_updatePostsCount = "UPDATE Thread SET posts = posts + 1 WHERE id = ?";
        String queryInsertForumAuthors = "Insert ignore into Forum_Authors (forumID, postAuthorID, postAuthorName) values (?,?,?)";

        try (Connection con = Main.mainConnection.getConnection();
             PreparedStatement stmt_main = con.prepareStatement(query_with_parent, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt_parentPost = con.prepareStatement(query_parentPost);
             PreparedStatement stmt_updatePath = con.prepareStatement(query_updatePath);
             PreparedStatement stmt_updatePostsCount = con.prepareStatement(query_updatePostsCount);
             PreparedStatement stmt_InsertForumAuthors = con.prepareStatement(queryInsertForumAuthors)) {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String date = json.get("date").getAsString();
            int threadID = json.get("thread").getAsInt();
            String message = json.get("message").getAsString();
            String user = json.get("user").getAsString();
            int authorID = UserDetailsServlet.GetID(user, "email", "User", con);
            if (authorID != -1) {
                String forum = json.get("forum").getAsString();
//                int forumID = UserDetailsServlet.GetID(forum, "short_name", "Forum", con);
                int forumID = UserDetailsServlet.GetForumID(forum, con);
                if (forumID != -1) {

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
                    if (new_parentID != null && !new_parentID.isJsonNull()) {
                        parentID = new_parentID.getAsInt();
                        stmt_main.setInt(13, parentID);
                    } else {
                        stmt_main.setNull(13, Types.INTEGER);
                    }

                    stmt_main.setString(1, date);
                    stmt_main.setInt(2, threadID);
                    stmt_main.setString(3, message);
                    stmt_main.setInt(4, authorID);
                    stmt_main.setInt(5, forumID);
                    stmt_main.setBoolean(6, isApproved);
                    stmt_main.setBoolean(7, isHighlighted);
                    stmt_main.setBoolean(8, isEdited);
                    stmt_main.setBoolean(9, isSpam);
                    stmt_main.setBoolean(10, isDelited);
                    stmt_main.setString(11, user);
                    stmt_main.setString(12, forum);

                    if (stmt_main.executeUpdate() == 1) {

                        ResultSet rs = stmt_main.getGeneratedKeys();
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
                            stmt_parentPost.setInt(1, parentID);
                            rs = stmt_parentPost.executeQuery();

                            rs.next();
                            String parent_path = rs.getString("parent_path");
                            int position = rs.getInt("pos");
                            int parent_firstPath = rs.getInt("parent_firstPath");

                            Formatter position_fmt = new Formatter();
                            position_fmt.format("%07d", position + 1);
                            path = parent_path + position_fmt;
                            first_path = parent_firstPath;
                        } else {
                            first_path = id;
                        }
                        stmt_updatePath.setString(1, path);
                        stmt_updatePath.setInt(2, first_path);
                        stmt_updatePath.setInt(3, id);
                        stmt_updatePath.executeUpdate();

                        stmt_updatePostsCount.setInt(1, threadID);
                        stmt_updatePostsCount.executeUpdate();

                        stmt_InsertForumAuthors.setInt(1, forumID);
                        stmt_InsertForumAuthors.setInt(2, authorID);
                        stmt_InsertForumAuthors.setString(3, UserDetailsServlet.GetName(authorID, con));
                        stmt_InsertForumAuthors.executeUpdate();

                    } else APIErrors.ErrorMessager(4, result);
                } else APIErrors.ErrorMessager(3, result);
            } else APIErrors.ErrorMessager(3, result);

        } catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        } catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(3, result);
        } catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
//        finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException se) {}
//        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
