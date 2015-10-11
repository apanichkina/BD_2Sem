package main; /**
 * Created by olegermakov on 14.09.15.
 */

import admin.AdminServlet;
import connection.AccountService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import frontend.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.sql.Statement;

public class Main {


    public static final int  STANDARTPORT = 8080;

    public static final String url = "jdbc:mysql://localhost:3306/forumdb";
    public static final String user = "root";
    public static final String password = "12345";

    public static Connection con = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;
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






        //FrontEnd frontend = new FrontEnd();
        AccountService accountService = new AccountService();
        Servlet mainmenu = new MainMenuServlet(accountService);
        Servlet signin = new SignInServlet(accountService);
        Servlet signup = new SignUpServlet(accountService);
        Servlet logout = new LogOut(accountService);
        Servlet profile = new ProfileServlet(accountService);
        Servlet admin = new AdminServlet(accountService);
        Servlet user_details = new UserDetailsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
       //context.addServlet(new ServletHolder(frontend), "/authform");
        context.addServlet(new ServletHolder(mainmenu), "/");
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signup), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");
        context.addServlet(new ServletHolder(profile), "/api/v1/auth/profile");
        context.addServlet(new ServletHolder(admin), "/api/v1/auth/admin");

        context.addServlet(new ServletHolder(user_details), "/api/ud/");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }



}
