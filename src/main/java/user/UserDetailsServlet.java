package user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class UserDetailsServlet extends HttpServlet {

    public UserDetailsServlet() {

    }
    public static void UsDet(int curr_id, @Nullable JsonObject responseJSON , Connection con) throws IOException, SQLException {

        String query_userDetails = "SELECT * FROM User WHERE id=?";
        String query_subscriptions = "SELECT threadID FROM Subscription WHERE userID= ?";
        String query_following = "SELECT email FROM Follow LEFT JOIN User ON Follow.followeeID=User.id WHERE followerID= ?";
        String query_followers = "SELECT email FROM Follow LEFT JOIN User ON Follow.followerID=User.id WHERE followeeID= ?";
        try(PreparedStatement stmt_main = con.prepareStatement(query_userDetails);
        PreparedStatement stmt_subscription = con.prepareStatement(query_subscriptions);
            PreparedStatement stmt_following = con.prepareStatement(query_following);
            PreparedStatement stmt_followers = con.prepareStatement(query_followers);
        ) {

            stmt_main.setInt(1, curr_id);
            //TODO проверить везде сработал ли запрос посредством проверки возвращаемого значения
            ResultSet rs = stmt_main.executeQuery();
            //TODO проверить валидность данных
            while (rs.next()) {
                responseJSON.addProperty("id", rs.getInt("id"));
                responseJSON.addProperty("about", rs.getString("about"));
                responseJSON.addProperty("email", rs.getString("email"));
                responseJSON.addProperty("name", rs.getString("name"));
                responseJSON.addProperty("username", rs.getString("username"));
                responseJSON.addProperty("isAnonymous", rs.getBoolean("isAnonymous"));
            }
            //////3 qvery
            stmt_subscription.setInt(1, curr_id);
            rs = stmt_subscription.executeQuery();
            JsonArray subscriptions_list = new JsonArray();
            while (rs.next()) {
                subscriptions_list.add(rs.getInt("threadID"));
            }
            responseJSON.add("subscriptions", subscriptions_list);
            ////4 qvery
            stmt_following.setInt(1, curr_id);
            rs = stmt_following.executeQuery();
            JsonArray following_list = new JsonArray();
            while (rs.next()) {
                following_list.add(rs.getString("email"));
            }
            responseJSON.add("following", following_list);
            /////5 qvery
            stmt_followers.setInt(1, curr_id);
            rs = stmt_followers.executeQuery();
            JsonArray followers_list = new JsonArray();
            while (rs.next()) {
                followers_list.add(rs.getString("email"));
            }
            responseJSON.add("followers", followers_list);
        }

    };

    public static int GetID (String row_value, String row_name, String table_name, Connection con) throws SQLException {
        int curr_id = -1;
        String query_getID = "SELECT id FROM "+ table_name+" WHERE "+row_name+"=?";
        PreparedStatement stmt = con.prepareStatement(query_getID);
        stmt.setString(1, row_value);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            curr_id = rs.getInt("id");
        }

        try{if (stmt != null){
            stmt.close();
        }
        } catch(SQLException se) {}
        try{if (rs != null){
            rs.close();
        }
        } catch(SQLException se) {}
        return curr_id;
    };
    public static int GetUserID (String row_value, Connection con) throws SQLException {
        int curr_id = -1;
        String query_getID = "SELECT id FROM User WHERE email = ?";
        try(PreparedStatement stmt = con.prepareStatement(query_getID);) {
            stmt.setString(1, row_value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                curr_id = rs.getInt("id");
            }
        }
        return curr_id;
    };
    public static int GetForumID (String row_value, Connection con) throws SQLException {
        int curr_id = -1;
        String query_getID = "SELECT id FROM Forum WHERE short_name = ?";
        try(PreparedStatement stmt = con.prepareStatement(query_getID);) {
            stmt.setString(1, row_value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                curr_id = rs.getInt("id");
            }
        }
        return curr_id;
    };
    public static String GetName (int id,Connection con) throws SQLException {
        String name = "";
        String query_getID = "SELECT name FROM User WHERE id = ?";
        try( PreparedStatement stmt = con.prepareStatement(query_getID);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                name = rs.getString("name");
            }
        }
        return name;
    };
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {


        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);
        try(Connection con = Main.mainConnection.getConnection()) {

            String curr_email = request.getParameter("user");
            if (curr_email == null) APIErrors.ErrorMessager(3,result);
            else {
//                int curr_id = GetID(curr_email, "email", table_name, con);
                int curr_id = GetUserID(curr_email, con);
                if (curr_id == -1) APIErrors.ErrorMessager(1, result);
                else {
                    UsDet(curr_id, responseJSON, con);
                }
            }

        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(1, result);
        }
        catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3,result);
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}
