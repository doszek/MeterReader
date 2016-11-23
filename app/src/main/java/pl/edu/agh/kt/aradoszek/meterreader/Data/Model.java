package pl.edu.agh.kt.aradoszek.meterreader.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doszek on 16.10.2016.
 */
public class Model {

    //================================================================================
    // Properties
    //================================================================================

    private static Model instance = null;
    private List<Place> placesList;

    //================================================================================
    // Constructors
    //================================================================================

    private Model() {
        placesList = testData();
    }

    //================================================================================
    // Singleton
    //================================================================================

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    //================================================================================
    // Creating Test Data
    //================================================================================

    private List<Place> testData() {
        Meter meter1 = new Meter("Water meter", "In the kitchen under sink", Meter.MeterType.WATER);
        Meter meter2 = new Meter("Gas meter", "Pawement", Meter.MeterType.GAS);
        Meter meter3 = new Meter("Electricity meter", "Salon", Meter.MeterType.ELECTRICITY);
        Meter meter4 = new Meter("Water meter", "Bathroom", Meter.MeterType.WATER);
        Meter meter5 = new Meter("Gas meter", "Kitchen", Meter.MeterType.GAS);
        Meter meter6 = new Meter("Power meter", "Somewhere", Meter.MeterType.ELECTRICITY);

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
    // Places List Methods
    //================================================================================

    public void removePlaceAtIndex(int index) {
        if (index < placesList.size()) {
            placesList.remove(index);
        }
    }

    public void addPlace(Place newPlace) {
        placesList.add(newPlace);
    }

    //================================================================================
    // Accessors
    //================================================================================

    public List<Place> getPlacesList() {
        return placesList;
    }

    public void setPlacesList(List<Place> placesList) {
        this.placesList = placesList;
    }

    public void updateMetersInPlace(Place place, List<Meter> meters) {
        if (placesList.contains(place)) {
            int index = placesList.indexOf(place);
            placesList.get(index).setMeters(meters);
            //send to server
        }
    }

    public void addMeterTo(Place place, Meter meter){
        //create json to sent to server
        //
    }

    public void addMeasurment(Place place, Meter meter, Measurment measurment) {
        //create json to sent to server
    }

    public List<Measurment> getAllMeasurments() {
        List<Measurment> allMeasurments = new ArrayList<Measurment>();
        for (Place place: placesList) {
            for (Meter meter: place.getMeters()) {
                allMeasurments.addAll(meter.getMeasurments());
            }
        }
        return allMeasurments;
    }

    public void getDataForUser(User user)  {
        //get from server
    }

    public void refreshData() {

    }

}
