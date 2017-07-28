package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KategoriMenu implements Parcelable {

    public static final Creator<KategoriMenu> CREATOR = new Creator<KategoriMenu>() {
        @Override
        public KategoriMenu createFromParcel(Parcel in) {
            return new KategoriMenu(in);
        }

        @Override
        public KategoriMenu[] newArray(int size) {
            return new KategoriMenu[size];
        }
    };
    // Attributes
    public String id_kategori_menu;
    public String nama_kategori_menu;
    public String gambar_kategori_menu;

    // Constructor
    public KategoriMenu(String id_kategori_menu,
                        String nama_kategori_menu,
                        String gambar_kategori_menu) {

        this.id_kategori_menu = id_kategori_menu;
        this.nama_kategori_menu = nama_kategori_menu;
        this.gambar_kategori_menu = gambar_kategori_menu;

    }

    protected KategoriMenu(Parcel in) {
        id_kategori_menu = in.readString();
        nama_kategori_menu = in.readString();
        gambar_kategori_menu = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kategori_menu);
        dest.writeString(nama_kategori_menu);
        dest.writeString(gambar_kategori_menu);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
