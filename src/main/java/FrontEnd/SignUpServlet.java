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

    public SignUpServlet(AccountService accountservice) {
        this.accountService = accountservice;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        assert accountService != null;
        assert request != null;
        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile == null) {
            assert response != null;
            response.getWriter().println(PageGenerator.getPage("SignUp.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description","already signed up");

            assert response != null;
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        assert request != null;
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null || name == "" || password == "")
        {
            pageVariables.put("status", "error");
            pageVariables.put("description", "empty field");
        }
        else
        {
            assert response != null;
            response.setStatus(HttpServletResponse.SC_OK);

            UserProfile newUser = new UserProfile(name, password, email);

            assert accountService != null;
            if(accountService.getUser(name) == null) {
                accountService.addUser(newUser.getLogin(), newUser);
                accountService.addSessions(request.getSession().getId(), newUser);
                pageVariables.put("status", "ok");
                pageVariables.put("name", name);
                pageVariables.put("password", email);
                pageVariables.put("description", "new user created");
            }
            else
            {
                pageVariables.put("status", "error");
                pageVariables.put("description", "User with this name already created");
            }
        }
        assert response != null;
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
