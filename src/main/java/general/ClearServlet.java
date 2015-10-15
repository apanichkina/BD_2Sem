package general;

import com.google.gson.JsonObject;
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
    private Connection con = null;

    public ClearServlet(Connection connect) {
        con = connect;
    }
    private Statement stmt = null;
    private ResultSet rs = null;
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        result.addProperty("code", "0");
        result.addProperty("response", "ОК");
        try{
            stmt = con.createStatement();
            stmt.executeUpdate("SET foreign_key_checks = 0;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`Follow`;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`Subscription`;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`User`;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`Post`;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`Forum`;");
            stmt.executeUpdate("TRUNCATE `forumdb`.`Thread`;");
            stmt.executeUpdate("SET foreign_key_checks = 1;");
        }
        catch (SQLException sqlEx) {
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
            } catch(SQLException se)  {}
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);


}}
