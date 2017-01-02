package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Adapters.MetersListAdapter;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Data;
import pl.edu.agh.kt.aradoszek.meterreader.Fragments.AddMeterDialogFragment;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class MetersListActivity extends AppCompatActivity implements AddMeterDialogFragment.AddMeterDialogListener {

    //================================================================================
    // Properties
    //================================================================================

    static final String EXTRA_METER = "pl.agh.edu.agh.kt.aradoszek.EXTRA_METER";
    private Place currentPlace;
    private List<Meter> metersList;
    private Button historyButton;
    private MetersListAdapter listAdapter;
    private ListView listView;
    private static final int REQUEST_ADD_MEASURE = 2;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meters_list);
        implementFloatingActionButton();

        currentPlace = getIntent().getExtras().getParcelable(PlacesListActivity.EXTRA_PLACE);
        currentPlace = Data.getInstance().getPlace(currentPlace.getName());
        metersList = currentPlace.getMeters();
        listView = (ListView) findViewById(R.id.meters_list);
        historyButton = (Button) findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MetersListActivity.this, MeasurementHistoryActivity.class);
                intent.putExtra(PlacesListActivity.EXTRA_PLACE, currentPlace);
                startActivity(intent);
            }
        });


        TextView header = (TextView) findViewById(R.id.meters_header_text_view);
        header.setText("Meters in "+ currentPlace.getName());

        listAdapter = new MetersListAdapter(this, metersList);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Meter meter = (Meter) o;
                Toast.makeText(MetersListActivity.this, "Selected :" + " " + meter.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MetersListActivity.this, GetMeasurementActivity.class);
                intent.putExtra(EXTRA_METER, meter);
                intent.putExtra(PlacesListActivity.EXTRA_PLACE, currentPlace);
                startActivityForResult(intent, REQUEST_ADD_MEASURE);
            }
        });
    }


    private void implementFloatingActionButton() {
        FloatingActionButton addMeterButton = (FloatingActionButton) findViewById(R.id.add_meter_button);
        addMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddMeterDialogFragment();
                dialog.show(getSupportFragmentManager(), "AddMeterDialogFragment");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }

        if (requestCode == REQUEST_ADD_MEASURE) {
                refreshData();
        }
    }

    private void refreshData() {
        currentPlace = Data.getInstance().getPlace(currentPlace.getName());
        metersList = currentPlace.getMeters();
    }

    //================================================================================
    // Listener
    //================================================================================

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Meter meter) {
        Data.getInstance().addMeter(currentPlace, meter);
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
