package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Result;
import pl.edu.agh.kt.aradoszek.meterreader.Data.User;
import pl.edu.agh.kt.aradoszek.meterreader.R;
import pl.edu.agh.kt.aradoszek.meterreader.Server.DataAssistant;
import pl.edu.agh.kt.aradoszek.meterreader.Server.PostDataTask;

public class RegisterActivity extends AppCompatActivity implements PostDataTask.PostDataTaskDelegate {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edit_text);
        final Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isOnline()) {
                    Toast.makeText(RegisterActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = getUser();
                if (user == null) {
                    return;
                }
                PostDataTask postDataTask = new PostDataTask(user);
                postDataTask.delegate = RegisterActivity.this;
                postDataTask.execute("http://178.62.107.140/api/createUser");
            }
        });
    }

    private boolean isInsertedDataValid(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(),
                    "Passwords are not identical", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Fill in all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!DataAssistant.isEmailValid(email)) {
            Toast.makeText(getApplicationContext(),
                    "Email invalid!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 5) {
            Toast.makeText(getApplicationContext(),
                    "Password is too short!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private User getUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (isInsertedDataValid(email,password,confirmPassword)) {
            User user = new User(email, password);
            return user;
        }
        return null;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public  void processStart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating...");
        progressDialog.show();
    }

    public void processFinish(String output) {
        progressDialog.dismiss();
        Result result = DataAssistant.getResultFromString(output);
        if (result == null) {
            return;
        }

        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            finish();
        }
    }
}

