package main; /**
 * Created by olegermakov on 14.09.15.
 */

import connection.AccountService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import frontend.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {


    public static final int  STANDARTPORT = 8080;
    public static final String URL_DB = "jdbc:mysql://localhost:3306/forumdb";
    public static final String USER_DB = "root";
    public static final String PASSWORD_DB = "12345";

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(@NotNull String[] args) throws Exception {

        int port = STANDARTPORT ;
        if (args.length == 1) {
            String portString = args[0];
            if (portString != null) {
                //noinspection ConstantConditions
                port = Integer.valueOf(portString);
            }
        }

        Connection mainConnection = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);

        AccountService accountService = new AccountService();

        Servlet user_details = new UserDetails(mainConnection);
        Servlet user_listFollowers = new UserListFollowers(mainConnection, "followers");
        Servlet user_listFollowing = new UserListFollowers(mainConnection, "following");
        Servlet user_updateProfile = new UserUpdateProfile(mainConnection);
        Servlet user_unfollow = new UserUnfollow(mainConnection, "unfollow");
        Servlet user_follow = new UserUnfollow(mainConnection, "follow");
        Servlet post_details = new PostDetails(mainConnection);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(user_details), "/db/api/user/details/");
        context.addServlet(new ServletHolder(user_listFollowers), "/db/api/user/listFollowers/");
        context.addServlet(new ServletHolder(user_listFollowing), "/db/api/user/listFollowing/");
        context.addServlet(new ServletHolder(user_updateProfile), "/db/api/user/updateProfile/");
        context.addServlet(new ServletHolder(user_unfollow), "/db/api/user/unfollow/");
        context.addServlet(new ServletHolder(user_follow), "/db/api/user/follow/");
        context.addServlet(new ServletHolder(post_details), "/db/api/post/details/");

        Server server = new Server(port);
        server.setHandler(context);



        server.start();
        server.join();
    }



}
