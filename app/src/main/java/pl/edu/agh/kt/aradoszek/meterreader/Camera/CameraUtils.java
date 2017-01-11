package pl.edu.agh.kt.aradoszek.meterreader.Camera;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by doszek on 16.11.2016.
 */

public class CameraUtils {

    static final String TAG = "DBG_ " + CameraUtils.class.getName();

    public static Camera getCamera() {
        try {
            return Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Cannot getCamera()");
            return null;
        }
    }
}
