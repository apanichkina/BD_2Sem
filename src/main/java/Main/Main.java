package Main; /**
 * Created by olegermakov on 14.09.15.
 */

import Admin.AdminServlet;
import Connection.AccountService;
import WebAnswer.FrontEnd;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import FrontEnd.*;
public class Main {

    private final static String APIpath = "/api/v1/auth/";

    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        FrontEnd frontend = new FrontEnd();
        AccountService accountService = new AccountService();

        Servlet signin = new SignInServlet(accountService);
        Servlet signup = new SignUpServlet(accountService);
        Servlet logout = new LogOut(accountService);
        Servlet profile = new ProfileServlet(accountService);
        Servlet admin = new AdminServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(frontend), APIpath + "authform");
        context.addServlet(new ServletHolder(signin), APIpath + "signin");
        context.addServlet(new ServletHolder(signup), APIpath + "signup");
        context.addServlet(new ServletHolder(logout), APIpath +  "logout");
        context.addServlet(new ServletHolder(profile), APIpath +  "");
        context.addServlet(new ServletHolder(admin), APIpath + "admin");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
