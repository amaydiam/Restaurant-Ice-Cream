package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class PesanBaru extends SugarRecord implements Parcelable {

    public static final Creator<PesanBaru> CREATOR = new Creator<PesanBaru>() {
        @Override
        public PesanBaru createFromParcel(Parcel in) {
            return new PesanBaru(in);
        }

        @Override
        public PesanBaru[] newArray(int size) {
            return new PesanBaru[size];
        }
    };
    // Attributes
    public String id_menu;
    public String nama_menu;
    public String harga_menu;
    public String jumlah_pesanan;

    public PesanBaru() {

    }

    public PesanBaru(String id_menu, String nama_menu, String harga_menu, String jumlah_pesanan) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.jumlah_pesanan = jumlah_pesanan;
    }

    protected PesanBaru(Parcel in) {
        id_menu = in.readString();
        nama_menu = in.readString();
        harga_menu = in.readString();
        jumlah_pesanan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_menu);
        dest.writeString(nama_menu);
        dest.writeString(harga_menu);
        dest.writeString(jumlah_pesanan);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
