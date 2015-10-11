package frontend;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by anna on 09.10.15.
 */
public class UserDetailsServlet extends HttpServlet {
    public static final String URL_DB = "jdbc:mysql://localhost:3306/forumdb";
    public static final String USER_DB = "root";
    public static final String PASSWORD_DB = "12345";

    public static Connection con = null;
    public static  PreparedStatement stmt = null;
    public static ResultSet rs = null;

    public static void UsDet(int curr_id,PreparedStatement stmt,ResultSet rs, @Nullable JSONObject  responseJSON , Connection con) throws IOException, SQLException {
        //int curr_id = 0;

        //////1 qvery

        //////2 qvery
        String query_userDetails = "SELECT id,about,email,isAnonymous,`name`,username \n" +
                "FROM User\n" +
                "WHERE id=" + curr_id;
        stmt = con.prepareStatement(query_userDetails);
        rs = stmt.executeQuery();
        while (rs.next()) {
            responseJSON.put("id", rs.getInt("id"));
            responseJSON.put("about", rs.getString("about"));
            responseJSON.put("email", rs.getString("email"));
            responseJSON.put("name", rs.getString("name"));
            responseJSON.put("username", rs.getString("username"));
            responseJSON.put("isAnonymous", rs.getBoolean("isAnonymous"));
        }
        //////3 qvery
        String query_subscriptions = "SELECT threatID\n" +
                "FROM Subscription\n" +
                "WHERE userID=" + curr_id;
        stmt = con.prepareStatement(query_subscriptions);
        rs = stmt.executeQuery();
        ArrayList<Integer> subscriptions_list = new ArrayList<Integer>();
        while (rs.next()) {
            subscriptions_list.add(rs.getInt("threatID"));
        }
        responseJSON.put("subscriptions", subscriptions_list);
        ////4 qvery
        String query_following = "SELECT email \n" +
                "FROM Follow \n" +
                "LEFT JOIN User\n" +
                "ON Follow.followeeID=User.id\n" +
                "WHERE followerID=" + curr_id;
        stmt = con.prepareStatement(query_following);
        rs = stmt.executeQuery();
        ArrayList<String> following_list = new ArrayList<String>();
        while (rs.next()) {
            following_list.add(rs.getString("email"));
        }
        responseJSON.put("following", following_list);
        /////5 qvery
        String query_followers = "SELECT email \n" +
                "FROM Follow \n" +
                "LEFT JOIN User\n" +
                "ON Follow.followerID=User.id\n" +
                "WHERE followeeID=" + curr_id;
        stmt = con.prepareStatement(query_following);
        rs = stmt.executeQuery();
        ArrayList<String> followers_list = new ArrayList<String>();
        while (rs.next()) {
            followers_list.add(rs.getString("email"));
        }
        responseJSON.put("followers", followers_list);


    };

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        //Map<String, Object> pageVariables = new HashMap<>();
        JSONObject result = new JSONObject();
        JSONObject responseJSON = new JSONObject();
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

            UsDet(curr_id, stmt, rs, responseJSON, con);
            result.put("response", responseJSON);
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
        //response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }




}
