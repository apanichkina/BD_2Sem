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

public class Main {


    public static final int  STANDARTPORT = 8080;

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

        AccountService accountService = new AccountService();

        Servlet user_details = new UserDetailsServlet();
        Servlet user_listFollowers = new UserListFollowers();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(user_details), "/db/api/user/details/");
        context.addServlet(new ServletHolder(user_listFollowers), "/db/api/user/listFollowers/");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }



}
