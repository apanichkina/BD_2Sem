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
    @NotNull
    private AccountService accountService;

    public ProfileServlet(@NotNull AccountService current_accountService) {
        this.accountService = current_accountService;
    }
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        //noinspection ConstantConditions
        UserProfile profile = accountService.getSessions(request.getSession().getId());
        if (profile == null) {
            //noinspection ConstantConditions
            response.getWriter().println(PageGenerator.getPage("SignIn.html", pageVariables));
        }
        else
        {
            pageVariables.put("name", profile.getLogin());
            pageVariables.put("email", profile.getEmail());
            //noinspection ConstantConditions
            response.getWriter().println(PageGenerator.getPage("ProfilePage.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
