package FrontEnd;

import Connection.AccountService;
import Connection.UserProfile;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;

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
public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile != null) {
            response.getWriter().println(PageGenerator.getPage("SignUp.html", pageVariables));
        }
        else
        {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if (name == null || name == "" || password == null || password == "")
        {
            pageVariables.put("status", "error");
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_OK);

            UserProfile userInput = accountService.getUser(name);
            if(userInput.getPassword() == password) {
                pageVariables.put("status","ok");
                pageVariables.put("name", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                accountService.addSessions(request.getSession().getId(), userInput);
            }
            else {
                pageVariables.put("status", "error");
                pageVariables.put("description", "wrong answer");
            }
        }

        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
