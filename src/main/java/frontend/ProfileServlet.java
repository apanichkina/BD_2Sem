package frontend;

import connection.AccountService;
import connection.UserProfile;
import org.jetbrains.annotations.NotNull;
import webanswer.PageGenerator;

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

    public ProfileServlet(@NotNull AccountService current_accountService) {
        this.accountService = current_accountService;
    }
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile == null) {
            assert response != null;
            response.getWriter().println(PageGenerator.getPage("SignIn.html", pageVariables));
        }
        else
        {
            pageVariables.put("name", profile.getLogin());
            pageVariables.put("email", profile.getEmail());
            assert response != null;
            response.getWriter().println(PageGenerator.getPage("ProfilePage.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
