package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailPesanan implements Parcelable {


    // Attributes
    public String   id_detail_pesanan;
    public String  id_pesanan;
    public String  id_menu;
    public String  jumlah_pesanan;
    public String  harga_pesanan;
    public String  nama_menu;
    public String  total_harga_pesanan;

    public DetailPesanan(String id_detail_pesanan, String id_pesanan, String id_menu, String jumlah_pesanan, String harga_pesanan, String nama_menu, String total_harga_pesanan) {
        this.id_detail_pesanan = id_detail_pesanan;
        this.id_pesanan = id_pesanan;
        this.id_menu = id_menu;
        this.jumlah_pesanan = jumlah_pesanan;
        this.harga_pesanan = harga_pesanan;
        this.nama_menu = nama_menu;
        this.total_harga_pesanan = total_harga_pesanan;
    }

    protected DetailPesanan(Parcel in) {
        id_detail_pesanan = in.readString();
        id_pesanan = in.readString();
        id_menu = in.readString();
        jumlah_pesanan = in.readString();
        harga_pesanan = in.readString();
        nama_menu = in.readString();
        total_harga_pesanan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_detail_pesanan);
        dest.writeString(id_pesanan);
        dest.writeString(id_menu);
        dest.writeString(jumlah_pesanan);
        dest.writeString(harga_pesanan);
        dest.writeString(nama_menu);
        dest.writeString(total_harga_pesanan);
    }

    public static final Creator<DetailPesanan> CREATOR = new Creator<DetailPesanan>() {
        @Override
        public DetailPesanan createFromParcel(Parcel in) {
            return new DetailPesanan(in);
        }

        @Override
        public DetailPesanan[] newArray(int size) {
            return new DetailPesanan[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
