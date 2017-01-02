package pl.edu.agh.kt.aradoszek.meterreader.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doszek on 16.10.2016.
 */

public class Meter implements Parcelable {

    //================================================================================
    // Properties
    //================================================================================

    private String name;
    private String description;
    private MeterType type;
    public List<Measurement> measurements;

    //================================================================================
    // Enums
    //================================================================================

    public enum MeterType implements Parcelable {
        GAS("gas"),
        WATER("water"),
        ELECTRICITY("electricity");

        private String name;

        MeterType(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public static MeterType fromString(String name) {
            if (name == null) {
                return null;
            }
            for (MeterType m : MeterType.values()) {
                if (name.equalsIgnoreCase(m.name)) {
                    return m;
                }
            }
            return null;
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

            public MeterType createFromParcel(Parcel in) {
                return MeterType.values()[in.readInt()];
            }

            public MeterType[] newArray(int size) {
                return new MeterType[size];
            }
        };
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }
    }


    //================================================================================
    // Constructors
    //================================================================================

    public Meter(String name, String description, MeterType type, List<Measurement> measurements) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.measurements = measurements;
    }

    public Meter(String name, String description, MeterType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.measurements = new ArrayList<>();
    }

    public Meter (Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        type = parcel.readParcelable(MeterType.class.getClassLoader());
        measurements = new ArrayList<>() ;
        parcel.readTypedList(measurements,Measurement.CREATOR);
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
        dest.writeParcelable(type, flags);
        dest.writeTypedList(measurements);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Meter createFromParcel(Parcel parcel) {
            return new Meter(parcel);
        }

        public Meter[] newArray(int size) {
            return new Meter[size];
        }
    };

    //================================================================================
    // Add
    //================================================================================

    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    //================================================================================
    // Accessors
    //================================================================================

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public MeterType getType() {
        return type;
    }

    public List<Measurement> getMeasurments() {
        return measurements;
    }


}


