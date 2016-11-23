package pl.edu.agh.kt.aradoszek.meterreader.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doszek on 16.10.2016.
 */

public class Place implements Parcelable {

    //================================================================================
    // Properties
    //================================================================================

    private String name;
    private String description;
    private List<Meter> meters;

    //================================================================================
    // Constructors
    //================================================================================

    public Place(String name, String description, List<Meter> meters) {
        this.name = name;
        this.description = description;
        this.meters = meters;
    }
    public Place(String name, String description) {
        this.name = name;
        this.description = description;
        this.meters = new ArrayList<Meter>();
    }

    public Place (Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        meters  = new ArrayList<Meter>() ;
        parcel.readTypedList(meters,Meter.CREATOR);
    }

    //================================================================================
    // Parceler Implementation
    //================================================================================

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(meters);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Place createFromParcel(Parcel parcel) {
            return new Place(parcel);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    //================================================================================
    // Accessors
    //================================================================================
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
    }

}
