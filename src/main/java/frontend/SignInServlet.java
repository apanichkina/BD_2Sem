package frontend;

import connection.*;
        import org.jetbrains.annotations.NotNull;
        import org.jetbrains.annotations.Nullable;
        import webanswer.*;

        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;
import java.util.Objects;

/**
 * Created by iHelos on 21.09.2015.
 */
public class SignInServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public SignInServlet(@NotNull AccountService current_accountService) {
        this.accountService = current_accountService;
    }


    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        //noinspection ConstantConditions
        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile == null) {
            //noinspection ConstantConditions,resource
            response.getWriter().println(PageGenerator.getPage("SignIn.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description","already signed in");

            response.setContentType("application/json; charset=utf-8");
            //noinspection ConstantConditions,resource
            response.getWriter().println(JsonGenerator.getJson(pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        //noinspection OverlyComplexBooleanExpression
        if (name == null || name.isEmpty() || password == null || Objects.equals(password, ""))
        {
            pageVariables.put("status", "error");
            pageVariables.put("description", "empty field");
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_OK);

            UserProfile userInput = accountService.getUser(name);
            if(userInput == null)
            {
                pageVariables.put("status", "error");
                pageVariables.put("description", "no such user");
            }
            else if(userInput.getPassword().equals(password)) {
                pageVariables.put("status","ok");
                pageVariables.put("name", name);
                pageVariables.put("password", password);
                //noinspection ConstantConditions
                accountService.addSessions(request.getSession().getId(), userInput);
            }
            else {
                pageVariables.put("status", "error");
                pageVariables.put("description", "wrong password");
            }
        }
        //noinspection ConstantConditions,resource
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
