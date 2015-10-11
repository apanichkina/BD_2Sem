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
    public static String getJson(@NotNull Map<String, Object> data) {
        JSONObject result = new JSONObject();
        JSONObject body = new JSONObject();
        //noinspection unchecked
        result.put("code", data.get("status"));
        data.remove("status");
        //noinspection unchecked
        result.put("responce", body);


        for (Map.Entry<String, Object> entry : data.entrySet()) {
            //noinspection unchecked
            body.put(entry.getKey(), entry.getValue());
        }

        //noinspection ConstantConditions
        return result.toJSONString();
    }
}