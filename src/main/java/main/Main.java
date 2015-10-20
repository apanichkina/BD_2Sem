package main; /**
 * Created by olegermakov on 14.09.15.
 */

import connection.AccountService;
import forum.*;
import general.ClearServlet;
import general.StatusServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import frontend.*;
import org.jetbrains.annotations.NotNull;
import post.*;
import thread.*;
import user.UserCreateServlet;
import user.UserDetailsServlet;
import user.UserListPostServlet;
import user.UserUpdateServlet;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {


    public static final int  STANDARTPORT = 8080;
    public static final String URL_DB = "jdbc:mysql://localhost:3306/forumdb?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8";
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

        Servlet status = new StatusServlet(mainConnection);
        Servlet clear = new ClearServlet(mainConnection);

        Servlet user_details = new UserDetailsServlet(mainConnection, "User");
        Servlet user_listFollowers = new UserListFollowers(mainConnection, "followers");
        Servlet user_listFollowing = new UserListFollowers(mainConnection, "following");
        Servlet user_updateProfile = new UserUpdateServlet(mainConnection);
        Servlet user_unfollow = new UserUnfollow(mainConnection, "unfollow");
        Servlet user_follow = new UserUnfollow(mainConnection, "follow");
        Servlet user_create = new UserCreateServlet(mainConnection);
        Servlet user_listPosts = new UserListPostServlet(mainConnection);

        Servlet post_details = new PostDetailsServlet(mainConnection);
        Servlet post_create = new PostCreateServlet(mainConnection);
        Servlet post_remove = new PostRemoveServlet(mainConnection,"remove");
        Servlet post_restore = new PostRemoveServlet(mainConnection,"restore");
        Servlet post_update = new PostUpdateServlet(mainConnection);
        Servlet post_vote = new PostVoteServlet(mainConnection);
        Servlet post_list = new PostListServlet(mainConnection);

        Servlet forum_create = new ForumCreateServlet(mainConnection);
        Servlet forum_details = new ForumDetailsServlet(mainConnection);
        Servlet forum_listPosts = new ForumListPostsServlet(mainConnection);
        Servlet forum_listThreads = new ForumListThreadsServlet(mainConnection);
        Servlet forum_listUsers = new ForumListUsersServlet(mainConnection);

        Servlet thread_create = new ThreadCreateServlet(mainConnection);
        Servlet thread_subscribe = new ThreadSubscribeServlet(mainConnection, "subscribe");
        Servlet thread_unsubscribe = new ThreadSubscribeServlet(mainConnection, "unsubscribe");
        Servlet thread_open = new ThreadOpenServlet(mainConnection, "open");
        Servlet thread_close = new ThreadOpenServlet(mainConnection, "close");
        Servlet thread_remove = new ThreadRemoveServlet(mainConnection);
        Servlet thread_restore = new ThreadRestoreServlet(mainConnection);
        Servlet thread_details = new ThreadDetailsServlet(mainConnection);
        Servlet thread_vote = new ThreadVoteServlet(mainConnection);
        Servlet thread_update = new ThreadUpdateServlet(mainConnection);
        Servlet thread_list = new ThreadListServlet(mainConnection);
        Servlet thread_listPosts = new ThreadListPostsServlet(mainConnection);

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
        context.addServlet(new ServletHolder(thread_subscribe), "/db/api/thread/subscribe/");
        context.addServlet(new ServletHolder(thread_unsubscribe), "/db/api/thread/unsubscribe/");
        context.addServlet(new ServletHolder(thread_open), "/db/api/thread/open/");
        context.addServlet(new ServletHolder(thread_close), "/db/api/thread/close/");
        context.addServlet(new ServletHolder(thread_remove), "/db/api/thread/remove/");
        context.addServlet(new ServletHolder(thread_restore), "/db/api/thread/restore/");
        context.addServlet(new ServletHolder(thread_details), "/db/api/thread/details/");
        context.addServlet(new ServletHolder(post_remove), "/db/api/post/remove/");
        context.addServlet(new ServletHolder(post_restore), "/db/api/post/restore/");
        context.addServlet(new ServletHolder(thread_vote), "/db/api/thread/vote/");
        context.addServlet(new ServletHolder(thread_update), "/db/api/thread/update/");
        context.addServlet(new ServletHolder(post_update), "/db/api/post/update/");
        context.addServlet(new ServletHolder(post_vote), "/db/api/post/vote/");
        context.addServlet(new ServletHolder(user_listPosts), "/db/api/user/listPosts/");
        context.addServlet(new ServletHolder(post_list), "/db/api/post/list/");
        context.addServlet(new ServletHolder(forum_listPosts), "/db/api/forum/listPosts/");
        context.addServlet(new ServletHolder(thread_list), "/db/api/thread/list/");
        context.addServlet(new ServletHolder(forum_listThreads), "/db/api/forum/listThreads/");
        context.addServlet(new ServletHolder(forum_listUsers), "/db/api/forum/listUsers/");
        context.addServlet(new ServletHolder(thread_listPosts), "/db/api/thread/listPosts/");

        Server server = new Server(port);
        server.setHandler(context);



        server.start();
        server.join();
    }



}
