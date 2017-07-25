package com.ad.restauranticecream.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CalonMustahiq implements Parcelable {

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public CalonMustahiq createFromParcel(Parcel in) {
            return new CalonMustahiq(in);
        }

        public CalonMustahiq[] newArray(int size) {
            return new CalonMustahiq[size];
        }
    };
    // Attributes
    public String id_calon_mustahiq;
    public String nama_calon_mustahiq;
    public String alamat_calon_mustahiq;
    public String latitude_calon_mustahiq;
    public String longitude_calon_mustahiq;
    public String no_identitas_calon_mustahiq;
    public String no_telp_calon_mustahiq;
    public String id_user_perekomendasi;
    public String alasan_perekomendasi_calon_mustahiq;
    public String photo_1;
    public String photo_2;
    public String photo_3;
    public String caption_photo_1;
    public String caption_photo_2;
    public String caption_photo_3;
    public String nama_perekomendasi_calon_mustahiq;
    public String status_calon_mustahiq;

    // Constructor
    public CalonMustahiq(String id_calon_mustahiq, String nama_calon_mustahiq, String alamat_calon_mustahiq, String latitude_calon_mustahiq, String longitude_calon_mustahiq, String no_identitas_calon_mustahiq,
                         String no_telp_calon_mustahiq,
                         String id_user_perekomendasi,
                         String alasan_perekomendasi_calon_mustahiq,
                         String photo_1,
                         String photo_2,
                         String photo_3,
                         String caption_photo_1,
                         String caption_photo_2,
                         String caption_photo_3,
                         String nama_perekomendasi_calon_mustahiq
            , String status_calon_mustahiq) {
        this.id_calon_mustahiq = id_calon_mustahiq;
        this.nama_calon_mustahiq = nama_calon_mustahiq;
        this.alamat_calon_mustahiq = alamat_calon_mustahiq;
        this.latitude_calon_mustahiq = latitude_calon_mustahiq;
        this.longitude_calon_mustahiq = longitude_calon_mustahiq;
        this.no_identitas_calon_mustahiq = no_identitas_calon_mustahiq;
        this.no_telp_calon_mustahiq = no_telp_calon_mustahiq;
        this.id_user_perekomendasi = id_user_perekomendasi;
        this.alasan_perekomendasi_calon_mustahiq = alasan_perekomendasi_calon_mustahiq;
        this.photo_1 = photo_1;
        this.photo_2 = photo_2;
        this.photo_3 = photo_3;
        this.caption_photo_1 = caption_photo_1;
        this.caption_photo_2  =caption_photo_2;
        this.caption_photo_3 = caption_photo_3;
        this.nama_perekomendasi_calon_mustahiq = nama_perekomendasi_calon_mustahiq;
        this.status_calon_mustahiq = status_calon_mustahiq;
    }

    public CalonMustahiq(Parcel in) {
        this.id_calon_mustahiq = in.readString();
        this.nama_calon_mustahiq = in.readString();
        this.alamat_calon_mustahiq = in.readString();
        this.latitude_calon_mustahiq = in.readString();
        this.longitude_calon_mustahiq = in.readString();
        this.no_identitas_calon_mustahiq = in.readString();
        this.no_telp_calon_mustahiq = in.readString();
        this.id_user_perekomendasi = in.readString();
        this.alasan_perekomendasi_calon_mustahiq = in.readString();
        this.photo_1 = in.readString();
        this.photo_2 = in.readString();
        this.photo_3 = in.readString();
        this.caption_photo_1 = in.readString();
        this.caption_photo_2 = in.readString();
        this.caption_photo_3 = in.readString();
        this.nama_perekomendasi_calon_mustahiq = in.readString();
        this.status_calon_mustahiq = in.readString();
    }

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_calon_mustahiq);
        out.writeString(nama_calon_mustahiq);
        out.writeString(alamat_calon_mustahiq);
        out.writeString(latitude_calon_mustahiq);
        out.writeString(longitude_calon_mustahiq);
        out.writeString(no_identitas_calon_mustahiq);
        out.writeString(no_telp_calon_mustahiq);
        out.writeString(id_user_perekomendasi);
        out.writeString(alasan_perekomendasi_calon_mustahiq);
        out.writeString(photo_1);
        out.writeString(photo_2);
        out.writeString(photo_3);
        out.writeString(caption_photo_1);
        out.writeString(caption_photo_2);
        out.writeString(caption_photo_3);
        out.writeString(nama_perekomendasi_calon_mustahiq);
        out.writeString(status_calon_mustahiq);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
