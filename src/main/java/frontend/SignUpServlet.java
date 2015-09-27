package frontend;

import connection.AccountService;
import connection.UserProfile;
import webanswer.JsonGenerator;
import webanswer.PageGenerator;
import org.jetbrains.annotations.NotNull;

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

    public SignUpServlet(@NotNull AccountService current_accountService) {
        this.accountService = current_accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

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

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null || name == "" || password == "")
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            pageVariables.put("status", "404");
            pageVariables.put("description", "empty field");
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_OK);

            UserProfile newUser = new UserProfile(name, password, email);

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

        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
