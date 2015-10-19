package main;

import com.google.gson.JsonObject;

/**
 * Created by anna on 19.10.15.
 */
public class APIErrors {
    public static void ErrorMessager(int code, JsonObject result) {
        String errorMsg = "";
        switch (code) {
            case 1: {
                errorMsg = "Запрашиваемый объект не найден";
                break;
            }
            case 2: {
                errorMsg = "Невалидный запрос";
                break;
            }
            case 3: {
                errorMsg = "Некорректный запрос";
                break;
            }
            case 4: {
                errorMsg = "Неизвестная ошибка";
                break;
            }
            case 5: {
                errorMsg = "Такой юзер уже существует";
                break;
            }
            default: break;
        }
        result.addProperty("code", code);
        result.addProperty("response", errorMsg);
    }
}
