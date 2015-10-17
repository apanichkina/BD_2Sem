package frontend;

import com.google.gson.JsonArray;
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
 * Created by anna on 11.10.15.
 */
public class UserListFollowers extends HttpServlet {
//TODO добавить опциональные параметры
    private Connection con = null;
    private String field_name = "";
    private String query = "";
    private  String table_name = "";
    public UserListFollowers(Connection connect, String table, String param) {
        con = connect;
        table_name = table;
        if (param.equals("followers")) {
            field_name = "followerID";
            query = "SELECT followerID FROM Follow WHERE followeeID= ?";
        }
        if (param.equals("following")) {
            field_name = "followeeID";
            query = "SELECT followeeID FROM Follow WHERE followerID= ?";
        }

    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);

        String curr_email = request.getParameter("user");

        try {
            int curr_id = UserDetailsServlet.GetID(curr_email, "email", table_name, con);

            stmt = con.prepareStatement(query);
            stmt.setInt(1, curr_id);
            rs = stmt.executeQuery();

            JsonArray list = new JsonArray();
            while (rs.next()) {
                JsonObject responceJS = new JsonObject();
                UserDetailsServlet.UsDet(rs.getInt(field_name), responceJS, con);
                list.add(responceJS);

            }

            result.add("response", list);


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try{if (stmt != null){
                stmt.close();
            }
            } catch(SQLException se) {}
            try{if (rs != null){
                rs.close();
            }
            } catch(SQLException se) {}

        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }

}
