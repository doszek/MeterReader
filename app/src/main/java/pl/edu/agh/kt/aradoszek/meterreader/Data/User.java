package pl.edu.agh.kt.aradoszek.meterreader.Data;

/**
 * Created by doszek on 11.11.2016.
 */

public class User {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
