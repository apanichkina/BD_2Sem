package Main; /**
 * Created by olegermakov on 14.09.15.
 */

import Connection.AccountService;
import WebAnswer.FrontEnd;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import FrontEnd.*;
public class Main {



    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        FrontEnd frontend = new FrontEnd();
        AccountService accountService = new AccountService();

        Servlet signin = new SignInServlet(accountService);
        Servlet signup = new SignInServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/authform");
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signup");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
