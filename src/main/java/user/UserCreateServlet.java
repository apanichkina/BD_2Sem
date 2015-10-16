package user;

/**
 * Created by anna on 15.10.15.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


public class UserCreateServlet  extends HttpServlet {

    private Connection con = null;

    public UserCreateServlet(Connection connect) {
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
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
            Boolean anonymous = false;
            String username = null;
            String name = null;
            String about = null;
            String email = json.get("email").getAsString();
            JsonElement new_anonymous = json.get("isAnonymous");
            if (new_anonymous != null) {
                anonymous = new_anonymous.getAsBoolean();
            }
            if (anonymous == false) {
                username = json.get("username").getAsString();
                about = json.get("about").getAsString();
                name = json.get("name").getAsString();
            }



            String query = "INSERT INTO User (email, username, about, name, isAnonymous) VALUES(?,?,?,?,?)";

            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, about);
            stmt.setString(4, name);
            stmt.setBoolean(5, anonymous);
            if (stmt.executeUpdate() != 1) throw new SQLException();

            rs = stmt.getGeneratedKeys();
            rs.next();



            responseJSON.addProperty("id",rs.getInt(1));
            responseJSON.addProperty("username",username);
            responseJSON.addProperty("name",name);
            responseJSON.addProperty("email",email);
            responseJSON.addProperty("about",about);

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
            result.addProperty("code", 5);
            result.addProperty("response", "err5");
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
