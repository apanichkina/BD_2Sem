package Admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Connection.AccountService;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;

/**
 * Created by olegermakov on 22.09.15.
 */
public class AdminServlet extends HttpServlet {
    private AccountService accountService;

    public AdminServlet(AccountService accountservice) {
        this.accountService = accountservice;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        assert request != null;
        assert accountService != null;
        if(accountService.getSessions(request.getSession().getId()) != null && accountService.getSessions(request.getSession().getId()).getLogin()=="Admin") {
            assert response != null;
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

            pageVariables.put("RegCount", accountService.getRegisteredCount());
            pageVariables.put("LogCount", accountService.getLoggedCount());
            response.getWriter().println(PageGenerator.getPage("Admin.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description", "no permission");
            assert response != null;
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
    }
    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        assert response != null;
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        assert request != null;
        assert accountService != null;
        if(accountService.getSessions(request.getSession().getId()) != null && accountService.getSessions(request.getSession().getId()).getLogin()=="Admin") {
            String timeString = request.getParameter("shutdown");
            if (timeString == null || Objects.equals(timeString, ""))
                timeString = "1";
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