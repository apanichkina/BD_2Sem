package thread;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by anna on 16.10.15.
 */
public class ThreadRemoveServlet extends HttpServlet{
    private Connection con = null;
    private String query_threadDelete = "UPDATE Thread SET isDelited=true WHERE id=?";
    private String query_postsRemove = "UPDATE Post SET isDelited=true,delete_count=delete_count+1 WHERE threadID=?";
    public ThreadRemoveServlet(Connection connect) {
        con = connect;
    }
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", 0);
        result.add("response", responseJSON);

        Gson gson = new Gson();
        try {
            JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
            int threadID = json.get("thread").getAsInt();


            stmt = con.prepareStatement(query_threadDelete);//TODO повесить триггер на изменение этого поля, чтобы все посты в теме преагировали
            stmt.setInt(1, threadID);

            if (stmt.executeUpdate() != 1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();

            stmt = con.prepareStatement(query_postsRemove);
            stmt.setInt(1, threadID);
            stmt.executeUpdate();


            responseJSON.addProperty("thread", threadID);
        }

        catch (com.google.gson.JsonSyntaxException jsEx) {
            result.addProperty("code", 2);
            result.addProperty("response", "err2");
        }
        catch (java.lang.NullPointerException npEx) {
            result.addProperty("code", 3);
            result.addProperty("response", "err3");
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            result.addProperty("code", 3);
            result.addProperty("response", "err3");
        }
        catch (SQLException sqlEx) {
            result.addProperty("code", 4);
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
