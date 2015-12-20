package general;

import com.google.gson.JsonObject;
import main.APIErrors;
import main.Main;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by anna on 15.10.15.
 */
public class ClearServlet extends HttpServlet {

    public ClearServlet() {

    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        result.addProperty("code", 0);
        result.addProperty("response", "ОК");
        try(Connection con = Main.mainConnection.getConnection();
        PreparedStatement stmt_Fkey0 = con.prepareStatement("SET foreign_key_checks = 0;");
        PreparedStatement stmt_Follow = con.prepareStatement("TRUNCATE `forumdb`.`Follow`;");
        PreparedStatement stmt_Subscription = con.prepareStatement("TRUNCATE `forumdb`.`Subscription`;");
        PreparedStatement stmt_User = con.prepareStatement("TRUNCATE `forumdb`.`User`;");
        PreparedStatement stmt_Post = con.prepareStatement("TRUNCATE `forumdb`.`Post`;");
        PreparedStatement stmt_Forum = con.prepareStatement("TRUNCATE `forumdb`.`Forum`;");
        PreparedStatement stmt_Thread=con.prepareStatement("TRUNCATE `forumdb`.`Thread`;");
        PreparedStatement stmt_ForumAuthors = con.prepareStatement("TRUNCATE `forumdb`.`Forum_Authors`;");
        PreparedStatement stmt_Fkey1 = con.prepareStatement("SET foreign_key_checks = 1;");
        ) {
            stmt_Fkey0.executeUpdate();
            stmt_Follow.executeUpdate();
            stmt_Subscription.executeUpdate();
            stmt_User.executeUpdate();
            stmt_Post.executeUpdate();
            stmt_Forum.executeUpdate();
            stmt_Thread.executeUpdate();
            stmt_ForumAuthors.executeUpdate();
            stmt_Fkey1.executeUpdate();
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
            sqlEx.printStackTrace();
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


}}
