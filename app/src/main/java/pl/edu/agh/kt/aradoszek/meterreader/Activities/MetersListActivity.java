package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Adapters.MetersListAdapter;
import pl.edu.agh.kt.aradoszek.meterreader.Fragments.AddMeterDialogFragment;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class MetersListActivity extends AppCompatActivity implements AddMeterDialogFragment.AddMeterDialogListener {

    //================================================================================
    // Properties
    //================================================================================

    static final String EXTRA_METER = "pl.agh.edu.agh.kt.aradoszek.EXTRA_METER";
    private Place currentPlace;
    private List<Meter> metersList;
    private MetersListAdapter listAdapter;
    private ListView listView;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meters_list);
        implementFloatingActionButton();

        currentPlace = getIntent().getExtras().getParcelable(PlacesListActivity.EXTRA_PLACE);
        metersList = currentPlace.getMeters();
        listView = (ListView) findViewById(R.id.meters_list);
        listAdapter = new MetersListAdapter(this, metersList);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Meter meter = (Meter) o;
                Toast.makeText(MetersListActivity.this, "Selected :" + " " + meter.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MetersListActivity.this, GetMeasurmentActivity.class);
                intent.putExtra(EXTRA_METER, meter);
                startActivity(intent);
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

    //================================================================================
    // Listener
    //================================================================================

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Meter meter) {
        metersList.add(meter);
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
