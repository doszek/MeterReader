package pl.edu.agh.kt.aradoszek.meterreader.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by doszek on 03.12.2016.
 */

public class Measurement implements Parcelable, Comparable<Measurement> {

    //================================================================================
    //Properties
    //================================================================================
    private double value;
    private String date;
    private String meterName;

    //================================================================================
    // Constructors
    //================================================================================

    public Measurement(double value, String createdAt, String fromMeter) {
        this.value = value;
        this.date = createdAt;
        this.meterName = fromMeter;
    }

    public Measurement(Parcel parcel) {
        value = parcel.readDouble();
        date = parcel.readString();
        meterName = parcel.readString();
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
        dest.writeDouble(value);
        dest.writeString(date);
        dest.writeString(meterName);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Measurement createFromParcel(Parcel parcel) {
            return new Measurement(parcel);
        }

        public Measurement[] newArray(int size) {
            return new Measurement[size];
        }
    };

    //================================================================================
    // Accessors
    //================================================================================

    public double getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public String getMeterName() {
        return meterName;
    }

    @Override
    public int compareTo(Measurement m) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        try {
            Date date1 = dateFormat.parse(getDate());
            Date date2 = dateFormat.parse(m.getDate());
            return date2.compareTo(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}


