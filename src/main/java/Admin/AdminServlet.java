package Admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Connection.AccountService;
import Connection.Permission;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;
import Exception.PostException;
import javafx.geometry.Pos;

/**
 * Created by olegermakov on 22.09.15.
 */
public class AdminServlet extends HttpServlet {
    private AccountService accountService;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        try{
            Permission.NotLoggedIn(request.getSession().getId(), accountService);
            Permission.AdminPermission(request.getSession().getId(), accountService);
            pageVariables.put("RegCount", accountService.getRegisteredCount());
            pageVariables.put("LogCount", accountService.getLoggedCount());
            response.getWriter().println(PageGenerator.getPage("admin.html", pageVariables));
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }

    }
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        try {
            Permission.AdminPermission(request.getSession().getId(), accountService);
            String timeString = request.getParameter("shutdown");
            if (timeString == null || timeString == "")
                timeString = "0";

            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: " + timeMS + " ms");
            CloseThread.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}