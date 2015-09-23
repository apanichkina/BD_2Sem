package FrontEnd;

import Connection.AccountService;
import Connection.Permission;
import Connection.UserProfile;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;
import Exception.*;

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
public class ProfileServlet extends HttpServlet {
    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getSessions(request.getSession().getId());

        try{
            Permission.NotLoggedIn(request.getSession().getId(), accountService);
            pageVariables.put("name", profile.getLogin());
            pageVariables.put("email", profile.getEmail());
            response.getWriter().println(PageGenerator.getPage("ProfilePage.html", pageVariables));
        }
        catch(PostException e)
        {
            pageVariables.put("code", e.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
