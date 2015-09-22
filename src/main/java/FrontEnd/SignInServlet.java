package FrontEnd;

        import Connection.*;
        import WebAnswer.*;
        import org.eclipse.jetty.server.session.JDBCSessionManager;

        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by iHelos on 21.09.2015.
 */
public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile == null) {
            response.getWriter().println(PageGenerator.getPage("SignIn.html", pageVariables));
        }
        else
        {
            pageVariables.put("status", "error");
            pageVariables.put("description","already signed in");

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
            else if(userInput.getPassword() == password) {
                pageVariables.put("status","ok");
                pageVariables.put("name", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                accountService.addSessions(request.getSession().getId(), userInput);
            }
            else {
                pageVariables.put("status", "error");
                pageVariables.put("description", "wrong password");
            }
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
