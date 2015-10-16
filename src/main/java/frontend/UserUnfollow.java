package frontend;

import org.jetbrains.annotations.NotNull;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.*;

import com.google.gson.Gson;


import com.google.gson.JsonObject;
import user.UserDetailsServlet;

/**
 * Created by anna on 12.10.15.
 */
public class UserUnfollow extends HttpServlet {

    private Connection con = null;
    private String query = "";
    private String table_name = "";
    public UserUnfollow(Connection connect, String table, String param) {
        con = connect;
        table_name = table;
        if (param.equals("follow")) {
            query = "INSERT INTO Follow (followerID,followeeID) VALUES(?,?);";
        }
        if (param.equals("unfollow")) {
            query = "DELETE FROM Follow WHERE followerID= ? and followeeID= ?";
        }
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
        String follower = json.get("follower").getAsString();
        String followee = json.get("followee").getAsString();

        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        try {

            //con = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
            int follower_id = UserDetailsServlet.GetID(follower, "email", table_name, con);
            int followee_id = UserDetailsServlet.GetID(followee, "email", table_name, con);


            stmt = con.prepareStatement(query);
            stmt.setInt(1, follower_id);
            stmt.setInt(2, followee_id);
            stmt.executeUpdate();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            //try {if (con != null) {con.close();}} catch (SQLException se) { /*can't do anything */ }
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
