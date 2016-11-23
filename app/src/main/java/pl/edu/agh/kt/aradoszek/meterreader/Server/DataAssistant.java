package pl.edu.agh.kt.aradoszek.meterreader.Server;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.agh.kt.aradoszek.meterreader.Data.User;

/**
 * Created by doszek on 16.11.2016.
 */

public class DataAssistant {
    public static JSONObject createUserJSONObject(User user)
    {
        JSONObject object = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("password", user.getPassword());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}
