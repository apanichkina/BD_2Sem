package main; /**
 * Created by olegermakov on 14.09.15.
 */

import connection.AccountService;
import forum.ForumCreateServlet;
import general.ClearServlet;
import general.StatusServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import frontend.*;
import org.jetbrains.annotations.NotNull;
import post.PostCreateServlet;
import post.PostDetailsServlet;
import thread.ThreadCreateServlet;
import user.UserCreateServlet;

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

        Servlet user_details = new UserDetails(mainConnection, "User");
        Servlet user_listFollowers = new UserListFollowers(mainConnection, "User", "followers");
        Servlet user_listFollowing = new UserListFollowers(mainConnection, "User", "following");
        Servlet user_updateProfile = new UserUpdateProfile(mainConnection, "User");
        Servlet user_unfollow = new UserUnfollow(mainConnection, "User", "unfollow");
        Servlet user_follow = new UserUnfollow(mainConnection, "User", "follow");
        Servlet post_details = new PostDetailsServlet(mainConnection);
        Servlet forum_details = new ForumDetails(mainConnection, "Forum");
        Servlet user_create = new UserCreateServlet(mainConnection);
        Servlet post_create = new PostCreateServlet(mainConnection);
        Servlet status = new StatusServlet(mainConnection);
        Servlet clear = new ClearServlet(mainConnection);
        Servlet thread_create = new ThreadCreateServlet(mainConnection);
        Servlet forum_create = new ForumCreateServlet(mainConnection);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(user_details), "/db/api/user/details/");
        context.addServlet(new ServletHolder(user_listFollowers), "/db/api/user/listFollowers/");
        context.addServlet(new ServletHolder(user_listFollowing), "/db/api/user/listFollowing/");
        context.addServlet(new ServletHolder(user_updateProfile), "/db/api/user/updateProfile/");
        context.addServlet(new ServletHolder(user_unfollow), "/db/api/user/unfollow/");
        context.addServlet(new ServletHolder(user_follow), "/db/api/user/follow/");
        context.addServlet(new ServletHolder(post_details), "/db/api/post/details/");
        context.addServlet(new ServletHolder(forum_details), "/db/api/forum/details/");
        context.addServlet(new ServletHolder(user_create), "/db/api/user/create/");
        context.addServlet(new ServletHolder(post_create), "/db/api/post/create/");
        context.addServlet(new ServletHolder(status), "/db/api/status/");
        context.addServlet(new ServletHolder(clear), "/db/api/clear/");
        context.addServlet(new ServletHolder(thread_create), "/db/api/thread/create/");
        context.addServlet(new ServletHolder(forum_create), "/db/api/forum/create/");

        Server server = new Server(port);
        server.setHandler(context);



        server.start();
        server.join();
    }



}
