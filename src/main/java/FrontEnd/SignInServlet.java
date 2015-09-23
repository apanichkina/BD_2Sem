package FrontEnd;

        import Connection.*;
        import WebAnswer.*;
        import Exception.*;

        import javafx.geometry.Pos;
        import org.eclipse.jetty.server.session.JDBCSessionManager;

        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

        import java.io.IOException;
        import java.util.ArrayList;
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

    //Страничка для входа
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
        ArrayList<Object> RequestedParams = new ArrayList<>();
        RequestedParams.add(name);
        RequestedParams.add(password);

        try{
            Permission.RequestParams(RequestedParams);
            response.setStatus(HttpServletResponse.SC_OK);

            UserProfile userInput = accountService.getUser(name);
            if(!userInput.getPassword().equals(password))
                throw new PostException("1010");

            pageVariables.put("status","ok");
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            accountService.addSessions(request.getSession().getId(), userInput);
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
