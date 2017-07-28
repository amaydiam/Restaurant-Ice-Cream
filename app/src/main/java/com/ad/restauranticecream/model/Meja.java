package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Meja implements Parcelable {

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public Meja createFromParcel(Parcel in) {
            return new Meja(in);
        }

        public Meja[] newArray(int size) {
            return new Meja[size];
        }
    };
    // Attributes
    public String id_meja;
    public String nama_meja;

    // Constructor
    public Meja(String id_meja, String nama_meja) {
        this.id_meja = id_meja;
        this.nama_meja = nama_meja;
    }

    public Meja(Parcel in) {
        this.id_meja = in.readString();
        this.nama_meja = in.readString();

    }

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_meja);
        out.writeString(nama_meja);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
