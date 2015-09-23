package FrontEnd;

import Connection.*;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;
import Exception.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
        if (profile == null) {
            response.getWriter().println(PageGenerator.getPage("SignUp.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description","already signed up");

            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        ArrayList<Object> RequestedParams = new ArrayList<>();
        RequestedParams.add(name);
        RequestedParams.add(password);

        try {
            Permission.RequestParams(RequestedParams);
            response.setStatus(HttpServletResponse.SC_OK);
            UserProfile newUser = new UserProfile(name, password, email);
            accountService.addUser(newUser.getLogin(), newUser);
            accountService.addSessions(request.getSession().getId(), newUser);
            pageVariables.put("status", "ok");
            pageVariables.put("name", name);
            pageVariables.put("password", email);
            pageVariables.put("description", "new user created");
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
