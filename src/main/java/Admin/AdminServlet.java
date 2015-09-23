package Admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Connection.AccountService;
import Connection.Permission;
import WebAnswer.JsonGenerator;
import WebAnswer.PageGenerator;
import Exception.PostException;

/**
 * Created by olegermakov on 22.09.15.
 */

//Сервлет для администрирования
public class AdminServlet extends HttpServlet {
    private AccountService accountService;                                                         //Аккаунт сервис

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }   //Конструкция сервера

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();                                        //Переменные
        try{
          //  Permission.NotLoggedIn(request.getSession().getId(), accountService);                 //Проверка на Залогинненость (см. PostException.java)
            Permission.AdminPermission(request.getSession().getId(), accountService);               //Проверка на Администрацию (см. PostException.java)
            pageVariables.put("RegCount", accountService.getRegisteredCount());                     //Количество зареганных
            pageVariables.put("LogCount", accountService.getLoggedCount());                         //Количество залогиненных
            response.getWriter().println(PageGenerator.getPage("admin.html", pageVariables));
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());                                              //Возврат ошибки
            response.getWriter().println(JsonGenerator.getJson(pageVariables));                     //Печатаем Json (см. JsonGenerator)
        }

    }
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();                                        //Переменные

        try {
            Permission.AdminPermission(request.getSession().getId(), accountService);               //Проверка на Админность (см. PostException.java)
            String timeString = request.getParameter("shutdown");                                   //Получили время мс (string), через которое сервак отключится
            if (timeString == null || timeString == "")                                             //Проверили пустую строку
                timeString = "0";                                                                   //Если пустая, то сразу вырубаем

            int timeMS = Integer.valueOf(timeString);                                               //Перевели в число
            System.out.print("Server will be down after: " + timeMS + " ms ");                      //Вывод в консоль
            CloseThread.sleep(timeMS);                                                              //Вырубаем серв (см. CloseThread.java)
            System.out.print("\nShutdown");
            System.exit(0);
        }
        catch (PostException e)
        {
            pageVariables.put("code", e.getMessage());                                              //Кладем код ошибки
        }
        response.getWriter().println(JsonGenerator.getJson(pageVariables));                         //Печатаем Json (см. JsonGenerator)
    }
}