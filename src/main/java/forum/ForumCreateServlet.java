package forum;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import frontend.UserDetails;
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
 * Created by anna on 15.10.15.
 */
public class ForumCreateServlet extends HttpServlet {
    private Connection con = null;
    private String query = "INSERT INTO Forum (name,short_name,userID) VALUES (?,?,?)";



    public ForumCreateServlet(Connection connect) {
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;
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
            String name = json.get("name").getAsString();
            String short_name = json.get("short_name").getAsString();
            int userID = UserDetails.GetID(json.get("user").getAsString(), "email", "User", con);


            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, short_name);
            stmt.setInt(3, userID);




            if (stmt.executeUpdate() != 1) throw new SQLException();
            rs = stmt.executeQuery("select last_insert_id() as last_id from Forum");
            int last_id = 0;
            while (rs.next()){
                last_id = rs.getInt("last_id");
            }
            System.out.println(last_id);

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
            result.addProperty("response", "error3");
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
            } catch (SQLException se) {}
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
