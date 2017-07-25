package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AmilRestaurant Ice Creamimplements Parcelable {

    // Attributes
    public String id_amil_zakat;
    public String nama_amil_zakat;

    // Constructor
    public AmilZakat(String id_amil_zakat, String nama_amil_zakat) {
        this.id_amil_zakat = id_amil_zakat;
        this.nama_amil_zakat = nama_amil_zakat;
    }
    public AmilZakat(Parcel in) {
        this.id_amil_zakat = in.readString();
        this.nama_amil_zakat = in.readString();
    }

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public AmilRestaurant Ice CreamcreateFromParcel(Parcel in) {
            return new AmilZakat(in);
        }
        public AmilZakat[] newArray(int size) {
            return new AmilZakat[size];
        }
    };

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_amil_zakat);
        out.writeString(nama_amil_zakat);
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
