package pl.edu.agh.kt.aradoszek.meterreader.Activities;


//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Point;
//import android.hardware.Camera;
//import android.hardware.Sensor;
//import android.hardware.SensorManager;
//import android.media.ExifInterface;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.NavUtils;
//import android.support.v7.app.ActionBarActivity;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//
//import pl.edu.agh.kt.aradoszek.meterreader.Camera2.CameraPreview;
//import pl.edu.agh.kt.aradoszek.meterreader.Camera2.RectangleView;
//import pl.edu.agh.kt.aradoszek.meterreader.R;
//
///*
//Created by Corbin Becker. Displays the camera preview and send the results to
//the word showcase activity
// */
//public class CameraActivity extends ActionBarActivity implements Camera.PictureCallback {
//
//    private Camera camera;
//    private CameraPreview cameraPreview;
//    private RectangleView rectangleView;
//    public static String DIR;
//    private int screenHeight, screenWidth;
//    static final String TAG = "MR_" + CameraActivity.class.getName();
//    private static final int REQUEST_TAKE_PHOTO = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //begin citation: http://stackoverflow.com/questions/25723331/display-and-hide-navigationbar-and-actionbar-onclickandroid
//        if (Build.VERSION.SDK_INT < 16) {
//            //Hide status bar
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        if (Build.VERSION.SDK_INT > 16) {
//            //Hide status bar
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//
//
//        /*
//        Inspired by tutorial: http://adblogcat.com/a-camera-preview-with-a-bounding-box-like-google-goggles/
//         */
//        rectangleView = new RectangleView(this);
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        screenHeight = displaymetrics.heightPixels;
//        screenWidth = displaymetrics.widthPixels;
//        //end citation
//    }
//
//    /*
//    Begin citation: http://adblogcat.com/a-camera-preview-with-a-bounding-box-like-google-goggles/
//     */
//    public Double[] getRatio() {
//        Camera.Size s = cameraPreview.getCameraParameters().getPreviewSize();
//        double heightRatio = (double) s.height / (double) screenHeight;
//        double widthRatio = (double) s.width / (double) screenWidth;
//        return new Double[]{heightRatio, widthRatio};
//    }
//    //end citation
//
//
//    public void onCaptureButtonClick(View view) {
//        camera.takePicture(null, null, this);
//    }
//
//    public void getCamera() {
//        camera = getCameraInstance();
//        cameraPreview = new CameraPreview(this, camera);
//        FrameLayout camFrameLayoutView = (FrameLayout) findViewById(R.id.camera_preview);
//        camFrameLayoutView.addView(cameraPreview);
//        rectangleView = (RectangleView) findViewById(R.id.dragRect);
//    }
//
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//
//
//        Thread getPictureThread = new Thread(new Runnable() {
//            public void run() {
//
//                //begin citation: http://adblogcat.com/a-camera-preview-with-a-bounding-box-like-google-goggles/
//                //accounts for the ratio of the camera preview and rectangle to save the correct part of the
//                //image (I.e. only the part inside the resizable rectangle
//                Double[] ratio = getRatio();
//                int left = (int) (ratio[1] * (double) rectangleView.getTopLeftPoint().x);
//
//                int top = (int) (ratio[0] * (double) rectangleView.getTopLeftPoint().y);
//
//                int right = (int) (ratio[1] * (double) rectangleView.getBottomRightPoint().x);
//
//                int bottom = (int) (ratio[0] * (double) rectangleView.getBottomRightPoint().y);
//
//                Bitmap bitmap = cameraPreview.getImage(left, top, right, bottom);
//                //end citation
//
//                Intent returnPictureIntent = new Intent();
//                returnPictureIntent.putExtra("data", bitmap);
//                setResult(REQUEST_TAKE_PHOTO, returnPictureIntent);
//                finish();
//            }
//        });
//        getPictureThread.start();
//
//    }
//
//    //release camera if paused
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (camera != null) {
//            camera.release();
//            camera = null;
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setContentView(R.layout.rect_activity_layout);
//        if (camera == null) {
//            getCamera();
//        }
//    }
//
//
//    /**
//     * A safe way to get an instance of the Camera object.
//     * Sourced from google recommendations: http://developer.android.com/guide/topics/media/camera.html
//     */
//    public static Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        } catch (Exception e) {
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//    //end citation
//
//
//    public void onBackButtonClick(View view) {
//        //go back to main screen when arrow is clicked
//        NavUtils.navigateUpFromSameTask(CameraActivity.this);
//    }
//
//
//}


import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;


import pl.edu.agh.kt.aradoszek.meterreader.Camera.CameraEngine;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.FocusBoxView;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.Tools;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.ShutterCallback {

    static final String TAG = "MR_" + CameraActivity.class.getName();
    private static final int REQUEST_TAKE_PHOTO = 1;

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    CameraEngine cameraEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "Surface Created - starting camera");

        if (cameraEngine != null && !cameraEngine.isOn()) {
            cameraEngine.start();
        }

        if (cameraEngine != null && cameraEngine.isOn()) {
            Log.d(TAG, "Camera engine already on");
            return;
        }

        cameraEngine = CameraEngine.New(holder);
        cameraEngine.start();

        Log.d(TAG, "Camera engine started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraFrame = (SurfaceView) findViewById(R.id.camera_frame);
        shutterButton = (Button) findViewById(R.id.shutter_button);
        focusBox = (FocusBoxView) findViewById(R.id.focus_box);
        focusButton = (Button) findViewById(R.id.focus_button);

        shutterButton.setOnClickListener(this);
        focusButton.setOnClickListener(this);

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraFrame.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (cameraEngine != null && cameraEngine.isOn()) {
            cameraEngine.stop();
        }

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.removeCallback(this);
    }

    @Override
    public void onClick(View v) {
        if (v == shutterButton) {
            if (cameraEngine != null && cameraEngine.isOn()) {
                cameraEngine.takeShot(this, this, this);
            }
        }

        if (v == focusButton) {
            if (cameraEngine != null && cameraEngine.isOn()) {
                cameraEngine.requestFocus();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d(TAG, "Picture taken");
        if (data == null) {
            Log.d(TAG, "Got null data");
            return;
        }

        Bitmap bitmap = Tools.getFocusedBitmap(this, camera, data, focusBox.getBox());
        Log.d(TAG, "Got bitmap");

        ///threshold



        ///

        Intent returnPictureIntent = new Intent();
        returnPictureIntent.putExtra("data", bitmap);
        setResult( REQUEST_TAKE_PHOTO , returnPictureIntent);
        finish();
    }

//    private Bitmap processImage(Bitmap bitmap) {
//        Mat imgMat = new Mat();
//        //Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bitmap, imgMat);
//        Imgproc.medianBlur(imgMat, imgMat, 5);
//        Imgproc.adaptiveThreshold(imgMat, imgMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2 );
//
//        Utils.matToBitmap(imgMat, bitmap);
//        return bitmap;
//    }
    @Override
    public void onShutter() {

    }
}
