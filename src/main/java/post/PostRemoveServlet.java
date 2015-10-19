package post;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.APIErrors;
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
public class PostRemoveServlet extends HttpServlet {
    private Connection con = null;
    private String query = "";
    public PostRemoveServlet(Connection connect,String param) {
        con = connect;
        if (param.equals("remove")) {
            query = "UPDATE Post SET isDelited=true, delete_count=delete_count+1 WHERE id=?";
        }
        if (param.equals("restore")) {
            query = "UPDATE Post SET isDelited=false, delete_count=delete_count-1 WHERE id=?";
        }
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
            int postID = -1;
            postID = json.get("post").getAsInt();
            if (postID < 0) throw new java.lang.NullPointerException();

            stmt = con.prepareStatement(query);
            stmt.setInt(1, postID);

            if (stmt.executeUpdate() != 1) throw new com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException();
            responseJSON.addProperty("post", postID);
        }

        catch (com.google.gson.JsonSyntaxException jsEx) {
            APIErrors.ErrorMessager(2, result);
        }
        catch (java.lang.NullPointerException npEx) {
            APIErrors.ErrorMessager(3, result);
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException icvEx) {
            APIErrors.ErrorMessager(3, result);
        }
        catch (SQLException sqlEx) {
            APIErrors.ErrorMessager(4, result);
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
