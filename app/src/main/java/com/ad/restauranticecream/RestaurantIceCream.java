package com.ad.restauranticecream;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class RestaurantIceCream extends Application {


    public static final String LAST_SELECTED = "last_drawer_selection";
    public static final String VIEW_TYPE = "view_type";

    public static final int VIEW_TYPE_HOME = 0;
    public static final int VIEW_TYPE_HOME_PELANGGAN = 1;
    public static final int VIEW_TYPE_HOME_PELAYAN = 2;
    public static final int VIEW_TYPE_HOME_KASIR = 3;

    public static final int MY_SOCKET_TIMEOUT_MS = 60000;
    public static final int JPEG_OUTPUT_QUALITY = 70;
    public static final String CALON_MUSTAHIQ_ID = "id_calon_mustahiq";
    public static final String MUSTAHIQ_ID = "id_mustahiq";
    public static final String MUSTAHIQ_OBJECT = "mustahiq_object";
    public static final String PESANAN_OBJECT = "pesanan_object";
    public static final String PESANAN_ID = "donasi_id";
    public static final String CALON_MUSTAHIQ_OBJECT = "calon_mustahiq_object";
    public static final String URL = "url";
    public static final String KEYWORD = "keyword";

    public static final String data = "data";

    public static final String TOOLBAR_TITLE = "toolbar_title";
    public static final String IS_FINISH_LOADING_AWAL_DATA = "is_loading";
    public static final String IS_LOADING_MORE_DATA = "is_locked";

    public static final String TAG_GRID_FRAGMENT = "movie_grid_fragment";
    public static final String isSuccess = "isSuccess";
    public static final String message = "message";


    public static final String foto_bukti_pembayaran = "foto_bukti_pembayaran";

    public static final String PAGE = "PAGE";


    public static final String calon_mustahiq = "calon_mustahiq";
    public static final String id_calon_mustahiq = "id_calon_mustahiq";
    public static final String nama_calon_mustahiq = "nama_calon_mustahiq";
    public static final String alamat_calon_mustahiq = "alamat_calon_mustahiq";
    public static final String latitude_calon_mustahiq = "latitude_calon_mustahiq";
    public static final String longitude_calon_mustahiq = "longitude_calon_mustahiq";
    public static final String no_identitas_calon_mustahiq = "no_identitas_calon_mustahiq";
    public static final String no_telp_calon_mustahiq = "no_telp_calon_mustahiq";
    public static final String nama_perekomendasi_calon_mustahiq = "nama_perekomendasi_calon_mustahiq";
    public static final String status_calon_mustahiq = "status_calon_mustahiq";


    public static final String jumlah_donasi = "jumlah_donasi";

    public static final String id_muzaki = "id_muzaki";
    public static final String nama_muzaki = "nama_muzaki";
    public static final String alamat_muzaki = "alamat_muzaki";
    public static final String no_identitas_muzaki = "no_identitas_muzaki";
    public static final String no_telp_muzaki = "no_telp_muzaki";
    public static final String status_muzaki = "status_muzaki";

    public static final String donasi = "donasi";
    public static final String id_donasi = "id_donasi";
    public static final String id_amil_zakat = "id_amil_zakat";
    public static final String nama_amil_zakat = "nama_amil_zakat";
    public static final String amil_zakat = "amil_zakat";
    public static final String waktu_terakhir_donasi = "waktu_terakhir_donasi";
    public static final String LOGIN = "login";
    public static final String MODE = "tipe_user";
    public static final String ID_USER = "id_user";
    public static final String NAMA_USER = "nama";
    public static final String ALAMAT_USER = "alamat";
    public static final String NOMOR_IDENTITAS_USER = "no_identitas";
    public static final String NOMOR_TELP_USER = "no_telp";
    public static final String PHOTO = "photo";

    public static final String MASTER_PASSWORD_VALUE = "123";
    public static final String MEJA_ID = "id_meja";
    public static final String DETAIL_PESANAN = "detail_pesanan";

    public static final int MODE_HOME = 0;
    public static final int MODE_PELANGGAN = 1;
    public static final int MODE_PELAYAN = 2;
    public static final int MODE_KASIR = 3;
    public static final String KATEGORI_MENU_ID = "kategori_menu_id";
    public static final String KATEGORI_MENU_OBJECT = "kategori_menu_object";
    public static final String SUB_KATEGORI_MENU_OBJECT = "sub_kategori_menu_object";
    public static final String SUB_KATEGORI_MENU_ID = "sub_kategori_menu_id";
    public static final String ID_MEJA = "ID_MEJA";
    public static final String NAMA_MEJA = "NAMA_MEJA";
    public static final String NAMA_PELANGGAN = "NAMA_PELANGGAN";
    public static final String KODE_PESANAN = "KODE_PESANAN";
    public static final String CATATAN_PESANAN = "CATATAN_PESANAN";

    public static String mustahiq = "mustahiq";
    public static String id_mustahiq = "id_mustahiq";
    public static String status_mustahiq = "status_mustahiq";
    public static String jumlah_rating = "jumlah_rating";
    public static int PLACE_PICKER_REQUEST=22;
    public static String username="username";
    public static String password="password";
    public static String user="user";
    public static String id_user="id_user";
    public static String no_telp="no_telp";
    public static String tipe_user="tipe_user";
    public static String nama="nama";
    public static String alamat="alamat";
    public static String no_identitas="no_identitas";
    public static String id_user_perekomendasi="id_user_perekomendasi";
    public static String alasan_perekomendasi_calon_mustahiq="alasan_perekomendasi_calon_mustahiq";
    public static String photo_1="photo_1";
    public static String photo_2="photo_2";
    public static String photo_3="photo_3";
    public static String caption_photo_1="caption_photo_1";
    public static String caption_photo_2="caption_photo_2";
    public static String caption_photo_3="caption_photo_3";
    public static String rating="rating";
    public static String id_meja="id_meja";
    public static String nama_meja="nama_meja";
    public static String meja="meja";

    public static String pesanan="pesanan";
    public static String  id_pesanan = "id_pesanan";
    public static String  kode_pesanan = "kode_pesanan";
    public static String  nama_pemesan = "nama_pemesan";
    public static String  waktu_pesan = "waktu_pesan";
    public static String  status_pesanan = "status_pesanan";
    public static String  total_harga = "total_harga";
    public static String  catatan = "catatan";



     public static String  id_detail_pesanan = "id_detail_pesanan";
     public static String  id_menu = "id_menu";
     public static String  jumlah_pesanan = "jumlah_pesanan";
     public static String  harga_pesanan = "harga_pesanan";
     public static String  nama_menu = "nama_menu";
     public static String  total_harga_pesanan = "total_harga_pesanan";
    public static String detail_pesanan="detail_pesanan";
    public static String kategori_menu="kategori_menu";

    public static String id_kategori_menu="id_kategori_menu";
    public static String nama_kategori_menu="nama_kategori_menu";
    public static String gambar_kategori_menu="gambar_kategori_menu";


    public static String id_sub_kategori_menu="id_sub_kategori_menu";
    public static String nama_sub_kategori_menu="nama_sub_kategori_menu";
    public static String gambar_sub_kategori_menu="gambar_sub_kategori_menu";
    public static String sub_kategori_menu="sub_kategori_menu";

    public static String menu="menu";
    public static String harga_menu="harga_menu";
    public static String stok_menu="stok_menu";
    public static String gambar_menu="gambar_menu";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
