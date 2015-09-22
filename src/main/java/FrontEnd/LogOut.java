package FrontEnd;

import Connection.AccountService;
import WebAnswer.JsonGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegermakov on 22.09.15.
 */
public class LogOut extends HttpServlet {
    private AccountService accountService;

    public LogOut(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        if(accountService.deleteSessions(request.getSession().getId())) {
            pageVariables.put("status","ok");
        }
        else{
            pageVariables.put("status", "error");
            pageVariables.put("description", "already not logged in");
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
