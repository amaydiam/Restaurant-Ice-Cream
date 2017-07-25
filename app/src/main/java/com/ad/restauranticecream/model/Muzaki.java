package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Muzaki implements Parcelable {

    // Attributes
    public String id_muzaki;
    public String nama_muzaki;
    public String alamat_muzaki;
    public String no_identitas_muzaki;
    public String no_telp_muzaki;
    public String status_muzaki;

    // Constructor
    public Muzaki(String id_muzaki, String nama_muzaki, String alamat_muzaki, String no_identitas_muzaki, String no_telp_muzaki,
                  String status_muzaki) {
        this.id_muzaki = id_muzaki;
        this.nama_muzaki = nama_muzaki;
        this.alamat_muzaki = alamat_muzaki;
        this.no_identitas_muzaki = no_identitas_muzaki;
        this.no_telp_muzaki = no_telp_muzaki;
        this.status_muzaki = status_muzaki;
    }
    public Muzaki(Parcel in) {
        this.id_muzaki = in.readString();
        this.nama_muzaki = in.readString();
        this.alamat_muzaki = in.readString();
        this.no_identitas_muzaki = in.readString();
        this.no_telp_muzaki = in.readString();
        this.status_muzaki = in.readString();
    }

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public Muzaki createFromParcel(Parcel in) {
            return new Muzaki(in);
        }
        public Muzaki[] newArray(int size) {
            return new Muzaki[size];
        }
    };

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_muzaki);
        out.writeString(nama_muzaki);
        out.writeString(alamat_muzaki);
        out.writeString(no_identitas_muzaki);
        out.writeString(no_telp_muzaki);
        out.writeString(status_muzaki);
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
