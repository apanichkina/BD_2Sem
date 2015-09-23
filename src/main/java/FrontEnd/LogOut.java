package FrontEnd;

import Connection.AccountService;
import WebAnswer.JsonGenerator;
import Exception.PostException;
import javafx.geometry.Pos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by olegermakov on 22.09.15.
 */


//Сервлет для разлогинивания
public class LogOut extends HttpServlet  {
    private AccountService accountService;
    public LogOut(AccountService accountService) {
        this.accountService = accountService;
    }

    //Разлогинивает
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();                            //Переменные
        try{
            accountService.deleteSessions(request.getSession().getId());                //Пытаемся удалить
        }
        catch (PostException e)
        {
            pageVariables.put("status", e.getMessage());                                //Если что-то пошло не так, то ошибку
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));
    }
}
