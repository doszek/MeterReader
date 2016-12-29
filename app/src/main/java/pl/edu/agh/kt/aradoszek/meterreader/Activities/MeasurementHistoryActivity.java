package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Adapters.MeasurementsListAdapter;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Measurement;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class MeasurementHistoryActivity extends AppCompatActivity {

    //================================================================================
    // Properties
    //================================================================================

    private Place currentPlace;
    private List<Measurement> measurementsList;
    private MeasurementsListAdapter listAdapter;
    private ListView listView;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_history);

        currentPlace = getIntent().getExtras().getParcelable(PlacesListActivity.EXTRA_PLACE);
        measurementsList = currentPlace.getMeasurements();
        listView = (ListView) findViewById(R.id.measurements_list);
        listAdapter = new MeasurementsListAdapter(this, measurementsList);

        listView.setAdapter(listAdapter);
        TextView header = (TextView) findViewById(R.id.measurement_header_text_view);
        header.setText("Last measurements in "+ currentPlace.getName());

    }

}