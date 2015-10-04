package admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import connection.AccountService;
import webanswer.JsonGenerator;
import webanswer.PageGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by olegermakov on 22.09.15.
 */
public class AdminServlet extends HttpServlet {
    @NotNull
    private  AccountService accountService;

    public AdminServlet(@NotNull AccountService accountservice) {
        this.accountService = accountservice;
    }
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException{
        Map<String, Object> pageVariables = new HashMap<>();


        //noinspection ConstantConditions
        if(accountService.getSessions(request.getSession().getId()) != null && Objects.equals(accountService.getSessions(request.getSession().getId()).getLogin(), "admin")) {

            response.setContentType("text/html;charset=utf-8");

            pageVariables.put("RegCount", accountService.getRegisteredCount());
            pageVariables.put("LogCount", accountService.getLoggedCount());
            response.setStatus(HttpServletResponse.SC_OK);
            //noinspection ConstantConditions
            response.getWriter().println(PageGenerator.getPage("Admin.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description", "no permission");

            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException, NullPointerException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();


        if(accountService.getSessions(request.getSession().getId()) != null && Objects.equals(accountService.getSessions(request.getSession().getId()).getLogin(), "admin")) {
            String timeString = request.getParameter("shutdown");
            if (timeString == null || timeString.isEmpty()) {
                timeString = "1";
            }
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: " + timeMS + " ms");
            CloseThread.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description", "no permission");
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
    }
}