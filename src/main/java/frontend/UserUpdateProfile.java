package frontend;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by anna on 12.10.15.
 */
public class UserUpdateProfile extends HttpServlet {
    private Connection con = null;
    private String table_name = "";
    public UserUpdateProfile(Connection connect, String table) {
        con = connect;
        table_name = table;
    }


    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
        String new_about = json.get("about").getAsString();
        String new_name = json.get("name").getAsString();
        String curr_email = json.get("user").getAsString();

        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", "0");
        result.add("response", responseJSON);
        try {
            int curr_id = UserDetails.GetID(curr_email, "email", table_name, con);
            String query_updateProfile = "UPDATE `User` SET about = ?, `name`= ? WHERE id= ?";
            stmt = con.prepareStatement(query_updateProfile);
            stmt.setString(1, new_about);
            stmt.setString(2, new_name);
            stmt.setInt(3, curr_id);
            stmt.executeUpdate();


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
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