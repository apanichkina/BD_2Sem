package frontend;

import org.jetbrains.annotations.NotNull;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import java.io.BufferedReader;
import org.json.*;
import com.google.gson.JsonElement;


import com.google.gson.JsonObject;

/**
 * Created by anna on 12.10.15.
 */
public class UserUnfollow extends HttpServlet {

    private Connection con = null;
    private String query = "";

    public UserUnfollow(Connection connect, String param) {
        con = connect;
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
        result.addProperty("code", "0");
        result.add("response", responseJSON);

        try {

            //con = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
            int follower_id = UserDetails.GetID(follower, con, stmt, rs);
            int followee_id = UserDetails.GetID(followee, con, stmt, rs);


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
            } catch (SQLException se) { /*can't do anything */ }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) { /*can't do anything */ }
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


    }
}
