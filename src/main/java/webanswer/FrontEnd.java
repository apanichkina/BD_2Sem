package webanswer; /**
 * Created by olegermakov on 14.09.15.
 */

import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrontEnd extends HttpServlet {

    private String login = "";

    @Override
    public void doGet(HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);

        //noinspection ConstantConditions,resource
        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);


    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        login = request.getParameter("login");
        String password = request.getParameter("password");


        response.setContentType("text/html;charset=utf-8");

        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }


        HashMap<String, Object> pageVariables = new HashMap<>();
        //noinspection OverlyComplexBooleanExpression
        if (login==null || Objects.equals(login, "") || password == null || Objects.equals(password, "")) {
            pageVariables.put("status", "error");
        }
        else {
            pageVariables.put("status", "ok");
            pageVariables.put("login", login);
            pageVariables.put("password", password);
        }

        response.setContentType("application/json; charset=utf-8");
        //noinspection ConstantConditions,resource
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
