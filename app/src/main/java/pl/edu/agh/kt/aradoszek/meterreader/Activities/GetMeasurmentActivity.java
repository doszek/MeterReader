package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import pl.edu.agh.kt.aradoszek.meterreader.OCR.TessOCR;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class GetMeasurmentActivity extends AppCompatActivity implements View.OnClickListener {
    private TessOCR mTessOCR;
    private TextView mResult;
    private ProgressDialog mProgressDialog;
    private ImageView mImage;
    private Button mButtonGallery, mButtonCamera, mButtonRecognize;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_measurment);

        mResult = (TextView) findViewById(R.id.measurment_value);
        mImage = (ImageView) findViewById(R.id.meter_image);
        mButtonGallery = (Button) findViewById(R.id.gallery_button);
        mButtonGallery.setOnClickListener(this);
        mButtonCamera = (Button) findViewById(R.id.photo_button);
        mButtonCamera.setOnClickListener(this);
        mButtonRecognize = (Button) findViewById(R.id.recognize_button);
        mButtonRecognize.setOnClickListener(this);
        mTessOCR = new TessOCR();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.gallery_button:
                pickPhoto();
                break;
            case R.id.photo_button:
                takePhoto();
                break;
            case R.id.recognize_button:
                Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();
                performOcr(bitmap);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mTessOCR.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            String path = pathFromUri(uri);
            setPicture(path);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && data != null) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap imageBitmap = data.getParcelableExtra("data");
                mImage.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_PICK_PHOTO) {
                Uri selectedImage = data.getData();
                String path = pathFromUri(selectedImage);
                setPicture(path);
            }
        }
    }

    private String pathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photoPath = cursor.getString(columnIndex);
        cursor.close();
        return photoPath;
    }


    private void setPicture(String photoPath) {
        int targetW = mImage.getWidth();
        int targetH = mImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        bitmap = rotate(photoPath, bitmap);
        mImage.setImageBitmap(bitmap);
    }

    private Bitmap rotate(String photoPath, Bitmap bitmap){
        Bitmap rotatedBitmap = bitmap;
        try {
            ExifInterface exif = new ExifInterface(photoPath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }
        } catch (IOException e) {
            Log.e("Error", "Couldn't correct orientation: " + e.toString());
        }
        return rotatedBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }

    private void takePhoto() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//        }
        Intent takePictureIntent=new Intent(this,CameraActivity.class);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);// Activity is started with requestCode 2
    }

    private void performOcr(final Bitmap bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Recognizing...", true);
        } else {
            mProgressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {
                final String result = mTessOCR.getOCRResult(bitmap);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (result != null && !result.equals("")) {
                            mResult.setText(result);
                        }
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

}
