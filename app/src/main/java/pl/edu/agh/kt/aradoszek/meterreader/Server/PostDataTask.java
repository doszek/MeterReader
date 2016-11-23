package pl.edu.agh.kt.aradoszek.meterreader.Server;

import android.app.ProgressDialog;
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

import pl.edu.agh.kt.aradoszek.meterreader.Data.User;

/**
 * Created by doszek on 13.11.2016.
 */

public class PostDataTask extends AsyncTask<String, Void, String> {
    private User user;
    private String result = "";

    public String getResult() {
        return result;
    }



    public PostDataTask(User user) {
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
    }

    private String postData(String urlPath) throws IOException, JSONException {

        StringBuilder resultBuilder = new StringBuilder();
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;

        try {
            //Create data to send to server
            JSONObject userToSend = DataAssistant.createUserJSONObject(user);

            //Initialize and config request, then connect to server.
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);  //enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            //Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(userToSend.toString());
            bufferedWriter.flush();

            //Read data response from server
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

        result = resultBuilder.toString();
        return result;

    }
}

