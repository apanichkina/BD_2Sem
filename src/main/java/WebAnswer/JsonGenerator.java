package WebAnswer; /**
 * Created by olegermakov on 14.09.15.
 */

import org.json.simple.JSONObject;

import java.util.ArrayList;


public class JsonGenerator {
    /* TODO */
    public static String getJson(ArrayList<String> data)
    {
        JSONObject result = new JSONObject();
        result.put("status", data.get(0));

        int size = data.size();

        if(size > 1) {
            result.put("login", data.get(1));
            result.put("password", data.get(2));
        }
        return result.toString();
    }
}
