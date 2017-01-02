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
    // Creating Test Data
    //================================================================================

    private List<Place> testData() {
        Meter meter1 = new Meter("123456", "In the kitchen under sink", Meter.MeterType.WATER);
        Meter meter2 = new Meter("156742", "Pawement", Meter.MeterType.GAS);
        Meter meter3 = new Meter("175343", "Salon", Meter.MeterType.ELECTRICITY);
        Meter meter4 = new Meter("197532", "Bathroom", Meter.MeterType.WATER);
        Meter meter5 = new Meter("135312", "Kitchen", Meter.MeterType.GAS);
        Meter meter6 = new Meter("187443", "Somewhere", Meter.MeterType.ELECTRICITY);

        List<Meter> meters1 = new ArrayList<Meter>();
        meters1.add(meter1);
        meters1.add(meter2);
        meters1.add(meter3);
        List<Meter> meters2 = new ArrayList<Meter>();
        meters2.add(meter4);
        meters2.add(meter5);
        meters2.add(meter6);
        List<Meter> meters3 = new ArrayList<Meter>();
        meters3.add(meter1);
        meters3.add(meter5);
        meters3.add(meter3);

        Place place1 = new Place("Floryda Residence", "Street 32", meters1);
        Place place2 = new Place("Work", "Street 12", meters2);
        Place place3 = new Place("Mommy's House", "Street 106", meters3);

        List<Place> places = new ArrayList<Place>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        return places;
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

    public void setPlacesList(List<Place> placesList) {
        this.placesList = placesList;
    }

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

    public Meter getMeter(String placeName, String meterName) {
        for (Place p : placesList) {
            if (!p.getName().equals(placeName)) {continue;}
            for (Meter m: p.getMeters()) {
                if (!m.getName().equals(meterName)) {continue;}
                return m;
            }
        }
        return null;
    }






}
