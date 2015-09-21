/**
 * Created by olegermakov on 14.09.15.
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {



    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        FrontEnd frontend = new FrontEnd();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/authform");

        Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
