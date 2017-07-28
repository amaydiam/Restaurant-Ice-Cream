package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
    // Attributes
    public String id_menu;
    public String id_sub_kategori_menu;
    public String nama_menu;
    public String harga_menu;
    public String stok_menu;
    public String gambar_menu;

    // Constructor
    public Menu(String id_menu,
                String id_sub_kategori_menu,
                String nama_menu,
                String harga_menu,
                String stok_menu,
                String gambar_menu) {

        this.id_menu = id_menu;
        this.id_sub_kategori_menu = id_sub_kategori_menu;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.stok_menu = stok_menu;
        this.gambar_menu = gambar_menu;

    }

    protected Menu(Parcel in) {
        id_menu = in.readString();
        id_sub_kategori_menu = in.readString();
        nama_menu = in.readString();
        harga_menu = in.readString();
        stok_menu = in.readString();
        gambar_menu = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_menu);
        dest.writeString(id_sub_kategori_menu);
        dest.writeString(nama_menu);
        dest.writeString(harga_menu);
        dest.writeString(stok_menu);
        dest.writeString(gambar_menu);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
