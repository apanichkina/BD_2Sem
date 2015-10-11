package frontend;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by anna on 11.10.15.
 */
public class UserListFollowers extends HttpServlet {
    public static final String URL_DB = "jdbc:mysql://localhost:3306/forumdb";
    public static final String USER_DB = "root";
    public static final String PASSWORD_DB = "12345";

    public static Connection con = null;
    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        JSONObject result = new JSONObject();
        result.put("code", "0");
        String curr_email = request.getParameter("email");

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
            int curr_id = 0;
            String query_getID = "SELECT id FROM User WHERE email=?";
            stmt = con.prepareStatement(query_getID);
            stmt.setString(1, curr_email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                curr_id = rs.getInt("id");
            }

            String query_followers = "SELECT followerID \n" +
                    "FROM Follow \n" +
                    "WHERE followeeID="+curr_id;
            stmt = con.prepareStatement(query_followers);
            rs = stmt.executeQuery();
            ArrayList<Integer> followers_list = new ArrayList<Integer>();
            LinkedList  list = new LinkedList();
            while (rs.next()) {
                JSONObject responceJS = new JSONObject();
                UserDetailsServlet.UsDet(rs.getInt("followerID"), stmt, rs, responceJS, con);
                list.push(responceJS);
            }

            result.put("response", list);


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try {if (con != null) {
                con.close();
            }
            }catch(SQLException se) { /*can't do anything */ }
            try{if (stmt != null){
                stmt.close();
            }
            } catch(SQLException se) { /*can't do anything */ }
            try{if (rs != null){
                rs.close();
            }
            } catch(SQLException se) { /*can't do anything */ }


        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }

}
