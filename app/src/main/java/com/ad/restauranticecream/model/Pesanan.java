package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pesanan implements Parcelable {

    public static final Creator<Pesanan> CREATOR = new Creator<Pesanan>() {
        @Override
        public Pesanan createFromParcel(Parcel in) {
            return new Pesanan(in);
        }

        @Override
        public Pesanan[] newArray(int size) {
            return new Pesanan[size];
        }
    };
    // Attributes
    public String id_pesanan;
    public String id_meja;
    public String kode_pesanan;
    public String nama_pemesan;
    public String waktu_pesan;
    public String status_pesanan;
    public String catatan;
    public String nama_meja;
    public String total_harga;

    // Constructor
    public Pesanan(String id_pesanan,
                   String id_meja,
                   String kode_pesanan,
                   String nama_pemesan,
                   String waktu_pesan,
                   String status_pesanan,
                   String catatan,
                   String nama_meja,
                   String total_harga) {

        this.id_pesanan = id_pesanan;
        this.id_meja = id_meja;
        this.kode_pesanan = kode_pesanan;
        this.nama_pemesan = nama_pemesan;
        this.waktu_pesan = waktu_pesan;
        this.status_pesanan = status_pesanan;
        this.catatan = catatan;
        this.nama_meja = nama_meja;
        this.total_harga = total_harga;

    }

    protected Pesanan(Parcel in) {
        id_pesanan = in.readString();
        id_meja = in.readString();
        kode_pesanan = in.readString();
        nama_pemesan = in.readString();
        waktu_pesan = in.readString();
        status_pesanan = in.readString();
        catatan = in.readString();
        nama_meja = in.readString();
        total_harga = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pesanan);
        dest.writeString(id_meja);
        dest.writeString(kode_pesanan);
        dest.writeString(nama_pemesan);
        dest.writeString(waktu_pesan);
        dest.writeString(status_pesanan);
        dest.writeString(catatan);
        dest.writeString(nama_meja);
        dest.writeString(total_harga);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
