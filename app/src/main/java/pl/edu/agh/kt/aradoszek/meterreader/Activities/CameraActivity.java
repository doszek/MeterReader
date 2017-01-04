package pl.edu.agh.kt.aradoszek.meterreader.Activities;


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

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;

import org.opencv.core.Point;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Camera.CameraEngine;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.FocusBoxView;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.Tools;
import pl.edu.agh.kt.aradoszek.meterreader.R;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.ShutterCallback {

    static final String TAG = "MR_" + CameraActivity.class.getName();
    static final String FILTERED_IMAGE = "pl.agh.edu.agh.kt.aradoszek.FILTERED_IMAGE";
    static final String IMAGE = "pl.agh.edu.agh.kt.aradoszek.IMAGE";
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
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

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

        Intent returnPictureIntent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable(IMAGE, bitmap);
        extras.putParcelable(FILTERED_IMAGE,filter(bitmap));
        returnPictureIntent.putExtras(extras);
        setResult(REQUEST_TAKE_PHOTO , returnPictureIntent);
        finish();
    }

    private Bitmap filter(Bitmap bitmap) {
        Mat imgMat = new Mat();
        Utils.bitmapToMat(bitmap, imgMat);
        Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_RGB2GRAY);

        Mat bw = new Mat(imgMat.size(), CvType.CV_8U);
        Mat bw2 = new Mat(imgMat.size(), CvType.CV_8U);

        Imgproc.threshold(imgMat, bw, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(imgMat, bw2, 0, 255, Imgproc.THRESH_OTSU);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        final double HTHRESH = imgMat.rows() * 0.5; // bounding-box height threshold
        List<MatOfPoint> contours = new ArrayList<>();
        List<Point> digits = new ArrayList<>();    // contours of the possible digits
        Mat hierarchy = new Mat();
        Imgproc.findContours(bw2, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

        for (int i = 0; i < contours.size(); i++) {
            int height = Imgproc.boundingRect(contours.get(i)).height;
            int width = Imgproc.boundingRect(contours.get(i)).width;
            if (height > HTHRESH && width > HTHRESH / 2 ) {
                digits.addAll(contours.get(i).toList());
            }
        }

        if (digits.size() == 0) {
            Utils.matToBitmap(bw, bitmap);
            return bitmap;
        }

        // find the convexhull of the digit contours
        MatOfInt digitsHullIdx = new MatOfInt();
        MatOfPoint hullPoints = new MatOfPoint();
        hullPoints.fromList(digits);
        Imgproc.convexHull(hullPoints, digitsHullIdx);
        // convert hull index to hull points
        List<Point> digitsHullPointsList = new ArrayList<Point>();
        List<Point> points = hullPoints.toList();
        for (Integer i: digitsHullIdx.toList()) {
            digitsHullPointsList.add(points.get(i));
        }
        MatOfPoint digitsHullPoints = new MatOfPoint();
        digitsHullPoints.fromList(digitsHullPointsList);

        List<MatOfPoint> digitRegions = new ArrayList<>();
        digitRegions.add(digitsHullPoints);
        Mat digitsMask = Mat.zeros(imgMat.size(), CvType.CV_8U);
        Imgproc.drawContours(digitsMask, digitRegions, 0, new Scalar(255, 255, 255), -1);

        Imgproc.morphologyEx(digitsMask, digitsMask, Imgproc.MORPH_DILATE, kernel);

        Mat cleaned = Mat.zeros(imgMat.size(), CvType.CV_8U);
        bw.copyTo(cleaned, digitsMask);

        Bitmap filteredImage = Bitmap.createBitmap(bw.cols(), bw.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bw, filteredImage);
        return filteredImage;
    }

    @Override
    public void onShutter() {

    }
}
