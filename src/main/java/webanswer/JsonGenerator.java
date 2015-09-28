package webanswer;
/**
 * Created by olegermakov on 14.09.15.
 */

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.Map;


public class JsonGenerator {
    /* TODO */

    @NotNull
    public static String getJson(Map<String, Object> data) {
        JSONObject result = new JSONObject();
        JSONObject body = new JSONObject();
        result.put("code", data.get("status"));
        data.remove("code");
        result.put("body", body);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            body.put(entry.getKey(), entry.getValue());
        }

        return result.toJSONString();
    }
}