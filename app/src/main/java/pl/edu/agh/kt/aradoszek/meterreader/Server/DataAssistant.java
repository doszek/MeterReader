package pl.edu.agh.kt.aradoszek.meterreader.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Measurement;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Place;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Result;
import pl.edu.agh.kt.aradoszek.meterreader.Data.User;

/**
 * Created by doszek on 16.11.2016.
 */

public class DataAssistant {

    public static JSONObject createUserJSONObject(User user) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", user.getEmail());
            object.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createPlaceJSONObject(User user, Place place) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", user.getEmail());
            object.put("localizationName", place.getName());
            object.put("description", place.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createMeterJSONObject(User user, Place place, Meter meter) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", user.getEmail());
            object.put("localizationName", place.getName());
            object.put("meterName", meter.getName());
            object.put("description", meter.getDescription());
            object.put("type", meter.getType().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject createMeasurementJSONObject(Measurement measurement) {
        JSONObject object = new JSONObject();
        try {
            object.put("meterName", measurement.getMeterName());
            object.put("state", measurement.getValue());
            object.put("timestamp", measurement.getDate());
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

    public static Result getResultFromString(String resultString) {
        Result result = null;
        try {
            JSONObject jsonObject = new JSONObject(resultString);
            boolean success =  jsonObject.getBoolean("success");
            String message = jsonObject.getString("message");
            result = new Result(success,message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Place> getUserDataFromString (String dataString) {
        List<Place> placesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(dataString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                Place place = getPlaceFromJson(jsonobject);
                placesList.add(place);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placesList;

    }

    private static Place getPlaceFromJson(JSONObject object) {
        List<Meter> metersList = new ArrayList<>();
        Place place = null;
        try {
            String localizationName = object.getString("localizationName");
            String description = object.getString("description");
            JSONArray metersArray = object.getJSONArray("meters");
            for (int i = 0; i < metersArray.length(); i++) {
                JSONObject meterObject = metersArray.getJSONObject(i);
                Meter meter = getMeterFromJson(meterObject);
                metersList.add(meter);
            }
            place = new Place(localizationName, description, metersList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    private static Meter getMeterFromJson(JSONObject object) {
        List<Measurement> measurementsList = new ArrayList<>();
        Meter meter = null;
        try {
            String meterName = object.getString("meterName");
            String description = object.getString("description");
            Meter.MeterType type = Meter.MeterType.fromString(object.getString("type"));
            JSONArray readsArray = object.getJSONArray("reads");
            for (int i = 0; i < readsArray.length(); i++) {
                JSONObject measurementObject = readsArray.getJSONObject(i);
                Measurement measurement = getMeasurementFromJson(measurementObject, meterName);
                measurementsList.add(measurement);
            }
            meter = new Meter(meterName, description, type, measurementsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return meter;
    }

    private static Measurement getMeasurementFromJson(JSONObject object, String meterName) {
        Measurement measurement = null;
        try {
            Double value =  object.getDouble("state");
            String timestamp = object.getString("timestamp");
            measurement = new Measurement(value, timestamp, meterName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return measurement;
    }



    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return dateFormat.format(calendar.getTime());
    }

}
