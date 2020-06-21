package Manager;

import org.json.JSONException;
import org.json.JSONObject;
public class JsonManager {
    public static JSONObject toJson (String text){
        try {
            JSONObject jsonRoot = new JSONObject(text);
            return jsonRoot;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
