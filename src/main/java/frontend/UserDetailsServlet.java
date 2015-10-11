package frontend;

import connection.AccountService;
import connection.UserProfile;
import org.jetbrains.annotations.NotNull;
import webanswer.JsonGenerator;
import webanswer.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anna on 09.10.15.
 */
public class UserDetailsServlet extends HttpServlet {
    public static final String url = "jdbc:mysql://localhost:3306/forumdb";
    public static final String user = "root";
    public static final String password = "12345";

    public static Connection con = null;
    public static  PreparedStatement stmt = null;
    public static ResultSet rs = null;


    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("status", "0");
        String curr_email = request.getParameter("email");
        int curr_id = 0;

        String qvery_getID ="SELECT id FROM User WHERE email=?";

        try {

            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            //////1 qvery
            stmt = con.prepareStatement(qvery_getID);

            stmt.setString(1, curr_email);

            //выполняем запрос
            rs = stmt.executeQuery();
            while (rs.next()) {
                curr_id = rs.getInt("id");
                //pageVariables.put("iddd",curr_id );
            }
            String qvery_userDetails = "SELECT about,email,isAnonymous,`name`,username \n" +
                    "FROM User\n" +
                    "WHERE id="+curr_id;

            //////2 qvery

            stmt = con.prepareStatement(qvery_userDetails);

            //выполняем запрос
            rs = stmt.executeQuery();

            while (rs.next()) {
                pageVariables.put("about",rs.getString("about") );
                pageVariables.put("email",rs.getString("email") );
                pageVariables.put("name",rs.getString("name") );
                pageVariables.put("username",rs.getString("username") );
                pageVariables.put("isAnonymous",rs.getBoolean("isAnonymous"));
            }
            //////3 qvery
            String qvery_subscriptions = "SELECT threatID\n" +
                    "FROM Subscription\n" +
                    "WHERE userID="+curr_id;
            stmt = con.prepareStatement(qvery_subscriptions);
            rs = stmt.executeQuery();
            ArrayList<Integer> subscriptions_list= new ArrayList<Integer>();
            while (rs.next()) {
                subscriptions_list.add(rs.getInt("threatID"));
            }
            pageVariables.put("subscriptions", subscriptions_list );
            ////4 qvery
            String qvery_following = "SELECT email \n" +
                    "FROM Follow \n" +
                    "LEFT JOIN User\n" +
                    "ON Follow.followeeID=User.id\n" +
                    "WHERE followerID="+curr_id;
            stmt = con.prepareStatement(qvery_following);
            rs = stmt.executeQuery();
            ArrayList<String> following_list= new ArrayList<String>();
            while (rs.next()) {
                following_list.add(rs.getString("email"));
            }
            pageVariables.put("following", following_list );
            /////5 qvery
            String qvery_followers = "SELECT email \n" +
                    "FROM Follow \n" +
                    "LEFT JOIN User\n" +
                    "ON Follow.followerID=User.id\n" +
                    "WHERE followeeID="+curr_id;
            stmt = con.prepareStatement(qvery_following);
            rs = stmt.executeQuery();
            ArrayList<String> followers_list= new ArrayList<String>();
            while (rs.next()) {
                followers_list.add(rs.getString("email"));
            }
            pageVariables.put("followers", followers_list );
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try{if (rs != null){
                rs.close();
                }
            } catch(SQLException se) { /*can't do anything */ }


        }
        System.out.println(curr_email);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }




}
