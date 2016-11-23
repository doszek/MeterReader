package pl.edu.agh.kt.aradoszek.meterreader.Data;

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
    private List<Measurment> measurments;

    //================================================================================
    // Enums
    //================================================================================

    public enum MeterType implements Parcelable {
        GAS, WATER, ELECTRICITY;

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

    public Meter(String name, String description, MeterType type, List<Measurment> measurments) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.measurments = measurments;
    }

    public Meter(String name, String description, MeterType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.measurments = new ArrayList<Measurment>();
    }

    public Meter (Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        type = parcel.readParcelable(MeterType.class.getClassLoader());
        measurments = new ArrayList<Measurment>() ;
        parcel.readTypedList(measurments,Measurment.CREATOR);
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
        dest.writeTypedList(measurments);
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

    public List<Measurment> getMeasurments() {
        return measurments;
    }
}


