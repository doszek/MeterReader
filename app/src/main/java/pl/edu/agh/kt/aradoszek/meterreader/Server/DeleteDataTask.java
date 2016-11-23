package pl.edu.agh.kt.aradoszek.meterreader.Server;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.agh.kt.aradoszek.meterreader.Activities.RegisterActivity;

/**
 * Created by doszek on 13.11.2016.
 */

public class DeleteDataTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return deleteData(params[0]);
        } catch (IOException ex) {
            return "Network error !";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    private String deleteData(String urlPath) throws IOException {

        String result =  null;

        //Initialize and config request, then connect to server.
        URL url = new URL(urlPath);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(10000 /* milliseconds */);
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json");// set header
        urlConnection.connect();

        //Check update successful or not
        if (urlConnection.getResponseCode() == 204) {
            result = "Delete successfully !";
        } else {
            result = "Delete failed !";
        }
        return result;
    }
}
