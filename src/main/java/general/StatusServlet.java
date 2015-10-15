package general;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by anna on 15.10.15.
 */
public class StatusServlet extends HttpServlet {
    private Statement stmt = null;
    private ResultSet rs = null;
    private Connection con = null;
    public StatusServlet(Connection connect) {
        con = connect;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {


        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", "0");
        result.add("response", responseJSON);

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT count(*) as status FROM User");
            while (rs.next()) {
                responseJSON.addProperty("user", rs.getInt("status"));
            }
            rs = stmt.executeQuery("SELECT count(*) as status FROM Thread");
            while (rs.next()) {
                responseJSON.addProperty("thread", rs.getInt("status"));
            }
            rs = stmt.executeQuery("SELECT count(*) as status FROM Forum");
            while (rs.next()) {
                responseJSON.addProperty("forum", rs.getInt("status"));
            }
            rs = stmt.executeQuery("SELECT count(*) as status FROM Post");
            while (rs.next()) {
                responseJSON.addProperty("post", rs.getInt("status"));
            }


        } catch (SQLException sqlEx) {
            result.addProperty("code", "4");
            result.addProperty("response", "err4");
            sqlEx.printStackTrace();
        } finally {
            try{if (stmt != null){
                stmt.close();
            }
            } catch(SQLException se) {}
            try{if (rs != null){
                rs.close();
            }
            } catch(SQLException se) {}


        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
        //response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }




}

