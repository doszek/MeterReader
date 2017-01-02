package pl.edu.agh.kt.aradoszek.meterreader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Adapters.PlacesListAdapter;
import pl.edu.agh.kt.aradoszek.meterreader.Fragments.AddPlaceDialogFragment;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Data;
import pl.edu.agh.kt.aradoszek.meterreader.Model.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;
import pl.edu.agh.kt.aradoszek.meterreader.Server.DataAssistant;
import pl.edu.agh.kt.aradoszek.meterreader.Server.PostDataTask;

public class PlacesListActivity extends AppCompatActivity implements AddPlaceDialogFragment.AddPlaceDialogListener, PostDataTask.PostDataTaskDelegate {

    //================================================================================
    // Properties
    //================================================================================

    static final String EXTRA_PLACE = "pl.agh.edu.agh.kt.aradoszek.EXTRA_PLACE";
    private List<Place> placesList;
    private Data dataData;
    private PlacesListAdapter arrayAdapter;
    private ProgressDialog progressDialog;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        implementFloatingActionButton();
        dataData = Data.getInstance();
        placesList = dataData.getPlacesList();
        final ListView listView = (ListView) findViewById(R.id.places_list);
        arrayAdapter = new PlacesListAdapter(this, placesList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Place place = (Place) o;
                Toast.makeText(PlacesListActivity.this, "Selected :" + " " + place.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlacesListActivity.this, MetersListActivity.class);
                intent.putExtra(EXTRA_PLACE, place);
                startActivity(intent);
            }
        });

        PostDataTask postDataTask = new PostDataTask(dataData.getUser());
        postDataTask.delegate = this;
        postDataTask.execute("http://178.62.107.140/api/getData");
    }

    private void implementFloatingActionButton() {
        FloatingActionButton addPlaceButton =(FloatingActionButton)findViewById(R.id.add_place_button);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddPlaceDialogFragment();
                dialog.show(getSupportFragmentManager(), "AddPlaceDialogFragment");
            }
        });
    }

    //================================================================================
    // Listener
    //================================================================================

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Place place) {
        dataData.addPlace(place);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void processFinish(String output) {
        progressDialog.dismiss();
        List<Place> userData = DataAssistant.getUserDataFromString(output);
        dataData.addAllUserData(userData);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public  void processStart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();
    }

}


