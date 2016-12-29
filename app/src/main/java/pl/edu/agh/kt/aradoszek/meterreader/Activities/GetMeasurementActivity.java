package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Measurement;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Model;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Place;
import pl.edu.agh.kt.aradoszek.meterreader.OCR.TessOCR;
import pl.edu.agh.kt.aradoszek.meterreader.R;
import pl.edu.agh.kt.aradoszek.meterreader.Server.DataAssistant;

public class GetMeasurementActivity extends AppCompatActivity implements View.OnClickListener {
    private TessOCR tessOCR;
    private TextView valueTextView;
    private ProgressDialog progressDialog;
    private ImageView photoImageView;
    private Button cameraButton, saveButton;
    private Meter currentMeter;
    private Place currentPlace;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_ADD_MEASURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_measurement);

        currentMeter = getIntent().getExtras().getParcelable(MetersListActivity.EXTRA_METER);
        currentPlace = getIntent().getExtras().getParcelable(PlacesListActivity.EXTRA_PLACE);

        valueTextView = (TextView) findViewById(R.id.measurement_value);
        photoImageView = (ImageView) findViewById(R.id.meter_image);
        cameraButton = (Button) findViewById(R.id.photo_button);
        saveButton = (Button) findViewById(R.id.save_button);
        TextView header = (TextView) findViewById(R.id.measurement_header_text_view);

        tessOCR = new TessOCR();

        header.setText("Add measurement from " + currentMeter.getName());
        cameraButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable("image");
            if (bitmap != null) {
                photoImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Drawable drawable = photoImageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            outState.putParcelable("image", bitmap);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save_button:
                addMeasure();
                break;
            case R.id.photo_button:
                takePhoto();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        tessOCR.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
        if (requestCode == REQUEST_TAKE_PHOTO) {
            Bitmap imageBitmap = data.getParcelableExtra("data");
            photoImageView.setImageBitmap(imageBitmap);
            performOcr(imageBitmap);
        }
    }

    private void addMeasure() {
        String text = valueTextView.getText().toString();
        if (text != null) {

            double value = Double.parseDouble(text);
            String date = DataAssistant.getCurrentDate();
            Measurement measurement = new Measurement(value, date, currentMeter.getName());
            Model.getInstance().addMeasurement(currentPlace, currentMeter, measurement);
            Toast.makeText(GetMeasurementActivity.this, "Measurement added!", Toast.LENGTH_SHORT).show();

            Intent returnMeterIntent = new Intent();
            setResult(REQUEST_ADD_MEASURE, returnMeterIntent);

            finish();
        } else {
            Toast.makeText(GetMeasurementActivity.this, "You must enter a value!", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);// Activity is started with requestCode 2
    }

    private void performOcr(final Bitmap bitmap) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", "Recognizing...");
        } else {
            progressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {
                final String result = tessOCR.getOCRResult(bitmap);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (result != null && !result.equals("")) {
                            valueTextView.setText(result);
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
