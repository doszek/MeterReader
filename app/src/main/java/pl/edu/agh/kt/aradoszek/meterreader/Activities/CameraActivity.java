package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.app.ActionBar;
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

import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.WriteFile;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Camera.CameraEngine;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.FocusBoxView;
import pl.edu.agh.kt.aradoszek.meterreader.Camera.Tools;
import pl.edu.agh.kt.aradoszek.meterreader.R;

import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.ShutterCallback {

    static final String TAG = "MR_" + CameraActivity.class.getName();
    private static final int REQUEST_TAKE_PHOTO = 1;

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    CameraEngine cameraEngine;

    public  CameraActivity()
    {
        if (!OpenCVLoader.initDebug()) {

            Log.d(TAG, "Failed to INIT \n OpenCV Failure");
        } else {
            Log.d(TAG, "OpenCV INIT Succes");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }

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
        bitmap =  processImage(bitmap);


        Intent returnPictureIntent = new Intent();
        returnPictureIntent.putExtra("data", bitmap);
        setResult( REQUEST_TAKE_PHOTO , returnPictureIntent);
        finish();
    }

    private Bitmap processImage(Bitmap bitmap) {
        Mat imgMat = new Mat();
        Utils.bitmapToMat(bitmap, imgMat);

        Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_RGB2GRAY);


        //Imgproc.GaussianBlur(imgMat, imgMat, new Size(1, 1), 0);

      // Imgproc.threshold(imgMat, imgMat, 0, 255, Imgproc.THRESH_OTSU);

       // Imgproc.adaptiveThreshold(imgMat, imgMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,51,10 );
        //Imgproc.GaussianBlur(imgMat, imgMat, new Size(21.0, 21.0), 50.0);
        Imgproc.adaptiveThreshold(imgMat, imgMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 13, -10);
        

        Utils.matToBitmap(imgMat, bitmap);

        return bitmap;

    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private static final int
            CV_MOP_CLOSE = 3,
            CV_THRESH_OTSU = 8,
            CV_THRESH_BINARY = 0,
            CV_ADAPTIVE_THRESH_GAUSSIAN_C  = 1,
            CV_ADAPTIVE_THRESH_MEAN_C = 0,
            CV_THRESH_BINARY_INV  = 1;


    public static boolean checkRatio(RotatedRect candidate) {
        double error = 0.3;
        double aspect = 6;
        int min = 15 * (int)aspect * 15;
        int max = 125 * (int)aspect * 125;

        double rmin= aspect - aspect*error;
        double rmax= aspect + aspect*error;
        double area= candidate.size.height * candidate.size.width;
        float r= (float)candidate.size.width / (float)candidate.size.height;
        if(r<1)
            r= 1/r;
        if(( area < min || area > max ) || ( r < rmin || r > rmax )){
            return false;
        }else{
            return true;
        }
    }

    public static boolean checkDensity(Mat candidate) {
        float whitePx = 0;
        float allPx = 0;
        whitePx = Core.countNonZero(candidate);
        allPx = candidate.cols() * candidate.rows();
        //System.out.println(whitePx/allPx);
        if (0.62 <= whitePx/allPx)
            return true;
        else
            return false;
    }

    public Bitmap processImage3(Bitmap bitmap) {

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img = new Mat();
        Mat imgGray = new Mat();
        Mat imgGaussianBlur = new Mat();
        Mat imgSobel = new Mat();
        Mat imgThreshold = new Mat();
        Mat imgAdaptiveThreshold = new Mat();
        Mat imgAdaptiveThreshold_forCrop = new Mat();
        Mat imgMoprhological = new Mat();
        Mat imgContours = new Mat();
        Mat imgMinAreaRect = new Mat();
        Mat imgDetectedPlateCandidate = new Mat();
        Mat imgDetectedPlateTrue = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();

        Utils.bitmapToMat(bitmap, img);

        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(imgGray, imgGaussianBlur, new Size(3, 3), 0);

        Imgproc.Sobel(imgGaussianBlur, imgSobel, -1, 1, 0);

        Imgproc.threshold(imgSobel, imgThreshold, 0, 255, CV_THRESH_OTSU + CV_THRESH_BINARY);

        Imgproc.adaptiveThreshold(imgSobel, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 75, 35);

        Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold_forCrop, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 99, 4);

        Mat element = getStructuringElement(MORPH_RECT, new Size(17, 3));
        Imgproc.morphologyEx(imgAdaptiveThreshold, imgMoprhological, CV_MOP_CLOSE, element); //или imgThreshold


        imgContours = imgMoprhological.clone();
        Imgproc.findContours(imgContours,
                contours,
                new Mat(),
                Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(imgContours, contours, -1, new Scalar(255, 0, 0));

        imgMinAreaRect = img.clone();
        if (contours.size() > 0) {
            for (MatOfPoint matOfPoint : contours) {
                MatOfPoint2f points = new MatOfPoint2f(matOfPoint.toArray());
                RotatedRect box = Imgproc.minAreaRect(points);
                if (checkRatio(box)) {
                    Imgproc.rectangle(imgMinAreaRect, box.boundingRect().tl(), box.boundingRect().br(), new Scalar(0, 0, 255));
                    imgDetectedPlateCandidate = new Mat(imgAdaptiveThreshold_forCrop, box.boundingRect());
                    if (checkDensity(imgDetectedPlateCandidate))
                        imgDetectedPlateTrue = imgDetectedPlateCandidate.clone();
                } else
                    Imgproc.rectangle(imgMinAreaRect, box.boundingRect().tl(), box.boundingRect().br(), new Scalar(0, 255, 0));
            }
        }
        Utils.matToBitmap(imgDetectedPlateTrue, bitmap);
        return bitmap;
    }

    private Bitmap filter(Bitmap bitmap) {
        Mat imgMat = new Mat();
        Utils.bitmapToMat(bitmap, imgMat);

        Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_RGB2GRAY);

        Mat bw = new Mat(imgMat.size(), CvType.CV_8U);
        Imgproc.threshold(imgMat, bw, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Mat dist = new Mat(imgMat.size(), CvType.CV_32F);
        Imgproc.distanceTransform(bw, dist, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);

        Mat dibw32f = new Mat(imgMat.size(), CvType.CV_32F);
        final double SWTHRESH = 8.0;
        Imgproc.threshold(dist, dibw32f, SWTHRESH/2.0, 255, Imgproc.THRESH_BINARY);
        Mat dibw8u = new Mat(imgMat.size(), CvType.CV_8U);
        dibw32f.convertTo(dibw8u, CvType.CV_8U);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        Mat cont = new Mat(imgMat.size(), CvType.CV_8U);
        Imgproc.morphologyEx(dibw8u, cont, Imgproc.MORPH_OPEN, kernel);

        final double HTHRESH = imgMat.rows() * 0.5; // bounding-box height threshold
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<Point> digits = new ArrayList<Point>();    // contours of the possible digits
        Imgproc.findContours(cont, contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int i = 0; i < contours.size(); i++) {
            if (Imgproc.boundingRect(contours.get(i)).height > HTHRESH) {
                digits.addAll(contours.get(i).toList());
            }
        }

        MatOfInt digitsHullIdx = new MatOfInt();
        MatOfPoint hullPoints = new MatOfPoint();
        hullPoints.fromList(digits);
        Imgproc.convexHull(hullPoints, digitsHullIdx);

        List<Point> digitsHullPointsList = new ArrayList<Point>();
        List<Point> points = hullPoints.toList();
        for (Integer i: digitsHullIdx.toList())
        {
            digitsHullPointsList.add(points.get(i));
        }
        MatOfPoint digitsHullPoints = new MatOfPoint();
        digitsHullPoints.fromList(digitsHullPointsList);

        List<MatOfPoint> digitRegions = new ArrayList<MatOfPoint>();
        digitRegions.add(digitsHullPoints);
        Mat digitsMask = Mat.zeros(imgMat.size(), CvType.CV_8U);
        Imgproc.drawContours(digitsMask, digitRegions, 0, new Scalar(255, 255, 255), -1);

        Imgproc.morphologyEx(digitsMask, digitsMask, Imgproc.MORPH_DILATE, kernel);

        Mat cleaned = Mat.zeros(imgMat.size(), CvType.CV_8U);
        dibw8u.copyTo(cleaned, digitsMask);


        Utils.matToBitmap(digitsMask, bitmap);
        return bitmap;
    }

    @Override
    public void onShutter() {

    }
}
