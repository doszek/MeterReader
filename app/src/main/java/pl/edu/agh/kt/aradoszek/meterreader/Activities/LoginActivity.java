package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.agh.kt.aradoszek.meterreader.Data.User;
import pl.edu.agh.kt.aradoszek.meterreader.R;
import pl.edu.agh.kt.aradoszek.meterreader.Server.PostDataTask;

public class LoginActivity extends AppCompatActivity {

    //================================================================================
    // Properties
    //================================================================================

    private EditText emailEditText;
    private EditText passwordEditText;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPlacesList();
                if (!isOnline()) {
                    Toast.makeText(LoginActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = getUser();
                if (user == null) {
                    passwordEditText.setText("");
                    return;
                }
                PostDataTask postDataTask = new PostDataTask(user);
                postDataTask.execute("http://178.62.107.140/api/loginUser");
                String result = postDataTask.getResult();
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result.contains("true")) {
                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                    openPlacesList();
                } else {
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                }

            }
        });

        final TextView registerTextView = (TextView) findViewById(R.id.register_text_view);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    private User getUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            User user = new User(email, password);
            return user;
        }
        return null;
    }

    //================================================================================
    // Open another Activities
    //================================================================================

    private void openPlacesList() {
        Intent intent = new Intent(LoginActivity.this, PlacesListActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
