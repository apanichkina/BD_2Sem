package WebAnswer;
/**
 * Created by olegermakov on 14.09.15.
 */

import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


public class JsonGenerator {
    /* TODO */
    public static String getJson(Map<String, Object> data)
    {
        JSONObject result = new JSONObject();
        JSONObject body = new JSONObject();
        result.put("code", data.get("code"));
        data.remove("code");
        result.put("body", body);
        int size = data.size();

        for (Map.Entry<String, Object> entry : data.entrySet())
        {
            body.put(entry.getKey(), entry.getValue());
        }

        return result.toJSONString();
    }
}
