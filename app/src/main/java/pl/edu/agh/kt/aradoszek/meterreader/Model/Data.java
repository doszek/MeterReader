package pl.edu.agh.kt.aradoszek.meterreader.Model;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Server.PostDataTask;

/**
 * Created by doszek on 16.10.2016.
 */
public class Data {

    //================================================================================
    // Properties
    //================================================================================
    private static Data instance = null;
    private List<Place> placesList ;
    private User user;

    //================================================================================
    // Constructors
    //================================================================================

    private Data() {
        placesList = new ArrayList<>();
    }

    //================================================================================
    // Singleton
    //================================================================================

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    //================================================================================
    // Adding values
    //================================================================================

    public void addPlace(Place place) {
        placesList.add(place);
        PostDataTask postDataTask = new PostDataTask(user, place);
        postDataTask.execute("http://178.62.107.140/api/saveLocalization");
    }

    public void addMeter(Place place, Meter meter) {
        for (Place p : placesList) {
            if (!p.getName().equals(place.getName())) {continue;}
            p.addMeter(meter);
        }
        PostDataTask postDataTask = new PostDataTask(user, place, meter);
        postDataTask.execute("http://178.62.107.140/api/saveMeter");
    }

    public void addMeasurement(Place place, Meter meter, Measurement measurement) {
        for (Place p : placesList) {
            if (!p.getName().equals(place.getName())) {continue;}
            p.addMeasurementToMeter(meter, measurement);
        }

        PostDataTask postDataTask = new PostDataTask(measurement);
        postDataTask.execute("http://178.62.107.140/api/saveRead");
    }

    public void addAllUserData(List<Place> userData) {
        placesList.clear();
        placesList.addAll(userData);
    }

    //================================================================================
    // Accessors
    //================================================================================


    public void setUser (User user) {
        this.user = user;
    }

    public User getUser () {
        return user;
    }

    public List<Place> getPlacesList() {
        return placesList;
    }

    public Place getPlace(String placeName) {
        for (Place p : placesList) {
            if (!p.getName().equals(placeName)) {continue;}
            return p;
        }
        return null;
    }
}
