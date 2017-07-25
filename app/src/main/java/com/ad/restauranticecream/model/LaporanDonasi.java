package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LaporanDonasi implements Parcelable {

    public static final Creator<LaporanDonasi> CREATOR = new Creator<LaporanDonasi>() {
        @Override
        public LaporanDonasi createFromParcel(Parcel in) {
            return new LaporanDonasi(in);
        }

        @Override
        public LaporanDonasi[] newArray(int size) {
            return new LaporanDonasi[size];
        }
    };
    // Attributes
    public String id_donasi;
    public String jumlah_donasi;
    public String id_muzaki;
    public String nama_muzaki;
    public String alamat_muzaki;
    public String no_identitas_muzaki;
    public String no_telp_muzaki;
    public String status_muzaki;
    public String id_mustahiq;
    public String id_calon_mustahiq;
    public String nama_calon_mustahiq;
    public String alamat_calon_mustahiq;
    public String no_identitas_calon_mustahiq;
    public String no_telp_calon_mustahiq;
    public String status_calon_mustahiq;
    public String id_amil_zakat;
    public String nama_amil_zakat;

    // Constructor
    public LaporanDonasi(String id_donasi,
                         String jumlah_donasi,
                         String id_muzaki,
                         String nama_muzaki,
                         String alamat_muzaki,
                         String no_identitas_muzaki,
                         String no_telp_muzaki,
                         String status_muzaki,
                         String id_mustahiq,
                         String id_calon_mustahiq,
                         String nama_calon_mustahiq,
                         String alamat_calon_mustahiq,
                         String no_identitas_calon_mustahiq,
                         String no_telp_calon_mustahiq,
                         String status_calon_mustahiq,
                         String id_amil_zakat,
                         String nama_amil_zakat) {

        this.id_donasi = id_donasi;
        this.jumlah_donasi = jumlah_donasi;
        this.id_muzaki = id_muzaki;
        this.nama_muzaki = nama_muzaki;
        this.alamat_muzaki = alamat_muzaki;
        this.no_identitas_muzaki = no_identitas_muzaki;
        this.no_telp_muzaki = no_telp_muzaki;
        this.status_muzaki = status_muzaki;

        this.id_mustahiq = id_mustahiq;
        this.id_calon_mustahiq = id_calon_mustahiq;
        this.nama_calon_mustahiq = nama_calon_mustahiq;
        this.alamat_calon_mustahiq = alamat_calon_mustahiq;
        this.no_identitas_calon_mustahiq = no_identitas_calon_mustahiq;
        this.no_telp_calon_mustahiq = no_telp_calon_mustahiq;
        this.status_calon_mustahiq = status_calon_mustahiq;
        this.id_amil_zakat = id_amil_zakat;
        this.nama_amil_zakat =nama_amil_zakat;
    }

    protected LaporanDonasi(Parcel in) {
        id_donasi = in.readString();
        jumlah_donasi = in.readString();
        id_muzaki = in.readString();
        nama_muzaki = in.readString();
        alamat_muzaki = in.readString();
        no_identitas_muzaki = in.readString();
        no_telp_muzaki = in.readString();
        status_muzaki = in.readString();
        id_mustahiq = in.readString();
        id_calon_mustahiq = in.readString();
        nama_calon_mustahiq = in.readString();
        alamat_calon_mustahiq = in.readString();
        no_identitas_calon_mustahiq = in.readString();
        no_telp_calon_mustahiq = in.readString();
        status_calon_mustahiq = in.readString();
        id_amil_zakat = in.readString();
        nama_amil_zakat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_donasi);
        dest.writeString(jumlah_donasi);
        dest.writeString(id_muzaki);
        dest.writeString(nama_muzaki);
        dest.writeString(alamat_muzaki);
        dest.writeString(no_identitas_muzaki);
        dest.writeString(no_telp_muzaki);
        dest.writeString(status_muzaki);
        dest.writeString(id_mustahiq);
        dest.writeString(id_calon_mustahiq);
        dest.writeString(nama_calon_mustahiq);
        dest.writeString(alamat_calon_mustahiq);
        dest.writeString(no_identitas_calon_mustahiq);
        dest.writeString(no_telp_calon_mustahiq);
        dest.writeString(status_calon_mustahiq);
        dest.writeString(id_amil_zakat);
        dest.writeString(nama_amil_zakat);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
