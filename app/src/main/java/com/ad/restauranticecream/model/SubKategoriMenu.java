package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubKategoriMenu implements Parcelable {

    public static final Creator<SubKategoriMenu> CREATOR = new Creator<SubKategoriMenu>() {
        @Override
        public SubKategoriMenu createFromParcel(Parcel in) {
            return new SubKategoriMenu(in);
        }

        @Override
        public SubKategoriMenu[] newArray(int size) {
            return new SubKategoriMenu[size];
        }
    };
    // Attributes
    public String id_sub_kategori_menu;
    public String id_kategori_menu;
    public String nama_sub_kategori_menu;
    public String gambar_sub_kategori_menu;

    // Constructor
    public SubKategoriMenu(String id_sub_kategori_menu,
                           String id_kategori_menu,
                           String nama_sub_kategori_menu,
                           String gambar_sub_kategori_menu) {

        this.id_sub_kategori_menu = id_sub_kategori_menu;
        this.id_kategori_menu = id_kategori_menu;
        this.nama_sub_kategori_menu = nama_sub_kategori_menu;
        this.gambar_sub_kategori_menu = gambar_sub_kategori_menu;

    }

    protected SubKategoriMenu(Parcel in) {
        id_sub_kategori_menu = in.readString();
        id_kategori_menu = in.readString();
        nama_sub_kategori_menu = in.readString();
        gambar_sub_kategori_menu = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_sub_kategori_menu);
        dest.writeString(id_kategori_menu);
        dest.writeString(nama_sub_kategori_menu);
        dest.writeString(gambar_sub_kategori_menu);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
