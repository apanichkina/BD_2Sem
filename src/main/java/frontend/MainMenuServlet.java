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
 * Created by olegermakov on 04.10.15.
 */
public class MainMenuServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public MainMenuServlet(@NotNull AccountService current_accountService) {
        this.accountService = current_accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        //noinspection ConstantConditions,resource
        response.getWriter().println(PageGenerator.getPage("index.html", "public_html" , pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
