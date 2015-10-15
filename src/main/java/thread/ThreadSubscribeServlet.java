package thread;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import user.UserDetailsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by anna on 15.10.15.
 */
public class ThreadSubscribeServlet extends HttpServlet{
    private Connection con = null;
    private String query = "";
    private Boolean isSubscribe = null;
    public ThreadSubscribeServlet(Connection connect,String param) {
        con = connect;
        if (param.equals("subscribe")) {
            query = "INSERT INTO Subscription (userID,threadID) VALUES (?,?)";
            isSubscribe = true;
        }
        if (param.equals("unsubscribe")) {
            query = "DELETE FROM Subscription WHERE userID=? AND threadID=?";
            isSubscribe = false;
        }
    }
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", "0");
        result.add("response", responseJSON);

        Gson gson = new Gson();
        try {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            String user = json.get("user").getAsString();
            int userID = UserDetailsServlet.GetID(user, "email", "User", con);
            int threadID = json.get("thread").getAsInt();


            stmt = con.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setInt(2, threadID);

            if (stmt.executeUpdate() != 1)
                if (isSubscribe) throw new SQLException();
                    else  throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();

            responseJSON.addProperty("thread", threadID);
            responseJSON.addProperty("user", user);
        }

        catch (com.google.gson.JsonSyntaxException jsEx) {
            result.addProperty("code", "2");
            result.addProperty("response", "err2");
        }
        catch (java.lang.NullPointerException npEx) {
            result.addProperty("code", "3");
            result.addProperty("response", "err3");
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            result.addProperty("code", "3");
            result.addProperty("response", "err3");
        }
        catch (SQLException sqlEx) {
            result.addProperty("code", "4");
            result.addProperty("response", "err4");

            sqlEx.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se)  {}
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {}
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


    }
}
