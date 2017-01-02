package pl.edu.agh.kt.aradoszek.meterreader.Model;

/**
 * Created by doszek on 23.11.2016.
 */

public class Result {


    private boolean success;
    private String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
