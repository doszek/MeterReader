package pl.edu.agh.kt.aradoszek.meterreader.Server;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.agh.kt.aradoszek.meterreader.Model.Measurement;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Place;
import pl.edu.agh.kt.aradoszek.meterreader.Model.User;

/**
 * Created by doszek on 13.11.2016.
 */

public class PostDataTask extends AsyncTask<String, Void, String> {
    private User user = null;
    private Place place = null;
    private Meter meter =  null;
    private Measurement measurement = null;
    public PostDataTaskDelegate delegate = null;

    public interface PostDataTaskDelegate {
        void processFinish(String output);

        void processStart();
    }

    public PostDataTask(User user) {
        this.user = user;
    }

    public PostDataTask(User user, Place place) {
        this.user = user;
        this.place = place;
    }

    public PostDataTask(User user, Place place, Meter meter) {
        this.user = user;
        this.place = place;
        this.meter = meter;
    }

    public PostDataTask(Measurement measurement) {
        this.measurement = measurement;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null) {
            delegate.processStart();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            return postData(params[0]);
        } catch (IOException ex) {
            return "Network error !";
        } catch (JSONException ex) {
            return "Data Invalid !";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (delegate != null) {
            delegate.processFinish(result);
        }
    }

    private JSONObject createDataToSend (String urlPath) {
        JSONObject dataToSend;

        if (urlPath.contains("loginUser") && user != null) {
            dataToSend = DataAssistant.createUserJSONObject(user);
        } else if (urlPath.contains("createUser") && user != null) {
            dataToSend = DataAssistant.createUserJSONObject(user);
        } else if (urlPath.contains("getData") && user != null) {
            dataToSend = DataAssistant.createUserJSONObject(user);
        } else if (urlPath.contains("saveRead") && measurement != null) {
            dataToSend = DataAssistant.createMeasurementJSONObject(measurement);
        } else if (urlPath.contains("saveMeter") && meter != null) {
            dataToSend = DataAssistant.createMeterJSONObject(user, place, meter);
        } else if (urlPath.contains("saveLocalization") && place != null)  {
            dataToSend = DataAssistant.createPlaceJSONObject(user, place);
        } else {
            dataToSend = null;
        }
        return dataToSend;
    }

    private String postData(String urlPath) throws IOException, JSONException {

        StringBuilder resultBuilder = new StringBuilder();
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;

        try {
            JSONObject dataToSend = createDataToSend(urlPath);

            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);  //enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(dataToSend.toString());
            bufferedWriter.flush();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
        return resultBuilder.toString();
    }
}

