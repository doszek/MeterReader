package pl.edu.agh.kt.aradoszek.meterreader.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by doszek on 16.11.2016.
 */

public class Measurment implements Parcelable {

    //================================================================================
    //Properties
    //================================================================================
    private float value;
    private String createdAt;

    //================================================================================
    // Constructors
    //================================================================================

    public Measurment(float value, Date date) {
        this.value = value;
        this.createdAt = createdAt;
    }

    public Measurment(Parcel parcel) {
        value = parcel.readFloat();
        createdAt = parcel.readString();
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
        dest.writeFloat(value);
        dest.writeString(createdAt);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Measurment createFromParcel(Parcel parcel) {
            return new Measurment(parcel);
        }

        public Measurment[] newArray(int size) {
            return new Measurment[size];
        }
    };

    //================================================================================
    // Accessors
    //================================================================================

    public float getValue() {
        return value;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
