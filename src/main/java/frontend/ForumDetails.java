package frontend;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by anna on 14.10.15.
 */
public class ForumDetails extends HttpServlet {
    private Connection con = null;
    private String table_name = "";
    public ForumDetails(Connection connect, String table) {
        table_name = table;
        con = connect;
    }

    public static PreparedStatement stmt = null;
    public static ResultSet rs = null;

    public static void PostDet(int curr_id,PreparedStatement stmt,ResultSet rs, @Nullable JsonObject responseJSON , Connection con, HashSet<String> related) throws IOException, SQLException {

        String query_forumDetails = "SELECT Forum.* , User.email FROM Forum LEFT JOIN User ON User.id=Forum.userID WHERE Forum.id=?";
        stmt = con.prepareStatement(query_forumDetails);
        stmt.setInt(1, curr_id);
        rs = stmt.executeQuery();

        while (rs.next()) {
            responseJSON.addProperty("id", curr_id);
            responseJSON.addProperty("name", rs.getString("name"));
            responseJSON.addProperty("short_name", rs.getString("short_name"));
            if (related.contains("user")) {
                JsonObject user_relatedJSON = new JsonObject();
                UserDetails.UsDet(rs.getInt("userID"),stmt,rs,user_relatedJSON,con);
                responseJSON.add("user",user_relatedJSON);
            }
            else responseJSON.addProperty("user", rs.getString("email"));
        }


    };

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = new JsonObject();
        JsonObject responseJSON = new JsonObject();
        result.addProperty("code", "0");

        HashSet<String> related= new HashSet<String>();
        related.add(request.getParameter("related"));
        String curr_short_name = request.getParameter("forum");


        try {
            int curr_id = UserDetails.GetID(curr_short_name, "short_name", table_name, con, stmt, rs);
            PostDet(curr_id, stmt, rs, responseJSON, con, related);
            result.add("response", responseJSON);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) { /*can't do anything */ }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) { /*can't do anything */ }

        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result);
    }
}

