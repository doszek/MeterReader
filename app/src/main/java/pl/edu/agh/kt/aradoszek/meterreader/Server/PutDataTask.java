package pl.edu.agh.kt.aradoszek.meterreader.Server;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by doszek on 13.11.2016.
 */

public class PutDataTask extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Updating data...");
//        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return putData(params[0]);
        } catch (IOException ex) {
            return "Network error !";
        } catch (JSONException ex) {
            return "Data invalid !";
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

//        mResult.setText(result);
//
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
    }

    private String putData(String urlPath) throws IOException, JSONException {

        BufferedWriter bufferedWriter = null;
        String result = null;

        try {
            //Create data to update
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("password", "Think twice code once ! HI !");
            dataToSend.put("email", "feel good - UPDATED !");


            //Initialize and config request, then connect to server
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);  //enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            //Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(dataToSend.toString());
            bufferedWriter.flush();

            //Check update successful or not
            if (urlConnection.getResponseCode() == 200) {
                return "Update successfully !";
            } else {
                return "Update failed !";
            }
        } finally {
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }
}
