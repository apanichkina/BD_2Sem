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
public class Main {


    public static final int  STANDARTPORT = 8080;

    public static void main(String[] args) throws Exception {

        assert args != null;
        int port = STANDARTPORT ;
        if (args.length == 1) {
            String portString = args[0];
            assert portString != null;
            port = Integer.valueOf(portString);
        }

        //FrontEnd frontend = new FrontEnd();
        AccountService accountService = new AccountService();

        Servlet signin = new SignInServlet(accountService);
        Servlet signup = new SignUpServlet(accountService);
        Servlet logout = new LogOut(accountService);
        Servlet profile = new ProfileServlet(accountService);
        Servlet admin = new AdminServlet(accountService);

        /* TODO разобраться */
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
       //context.addServlet(new ServletHolder(frontend), "/authform");
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signup), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");
        context.addServlet(new ServletHolder(profile), "/");
        context.addServlet(new ServletHolder(admin), "/admin");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
