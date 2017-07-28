package com.ad.restauranticecream.utils;

        import android.content.Context;
        import android.support.v4.app.FragmentActivity;

        import com.ad.restauranticecream.R;

public class ApiHelper {


    public static String getBaseUrl(Context context) {
        return Prefs.getUrl(context) + "/" + context.getString(R.string.base_url);
    }

    private static String getApiUrl(Context context) {
        return Prefs.getUrl(context) + "/" + context.getString(R.string.base_url) + "/index.php/";
    }

    // Add Donasi Baru
    public static String getDonasiBaruLink(Context context) {
        return getApiUrl(context) + "donasi/adddonasi";
    }

    //laporan donasi
    public static String getPesananModePelangganLink(Context context, String idMeja, int page, String keyword) {
        return getApiUrl(context) + "pesanan/pesanan_pelanggan/"+idMeja+"/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }
 public static String getPesananModePelayanLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "pesanan/pesanan_pelayan/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }

    public static String getPesananModeKasirLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "pesanan/pesanan_kasir/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }


    public static String getPesananDetailLink(Context context, String id) {
        return getApiUrl(context) + "detail_pesanan/detail_pesanan/" + id;
    }

    //CalonMustahiq
    public static String getMejaLink(Context context, int page) {
        return getApiUrl(context) + "meja/meja/" + page;
    }

    public static String getCalonMustahiqDetailLink(Context context, String id) {
        return getApiUrl(context) + "calon_mustahiq/detail_calon_mustahiq/" + id;
    }

    public static String getCalonMustahiqAddEditLink(Context context) {
        return getApiUrl(context) + "calon_mustahiq/addeditcalon_mustahiq/";
    }


    public static String getCalonMustahiqDeleteLink(Context context, String id) {
        return getApiUrl(context) + "calon_mustahiq/delete_calon_mustahiq/" + id;
    }


    public static String getKategoriMenuAddEditLink(Context context) {
        return getApiUrl(context) + "kategori_menu/addeditkategori_menu/";
    }


    public static String getKategoriMenuDeleteLink(Context context, String id) {
        return getApiUrl(context) + "kategori_menu/delete_kategori_menu/" + id;
    }

    public static String getSubKategoriMenuAddEditLink(Context context) {
        return getApiUrl(context) + "sub_kategori_menu/addeditsub_kategori_menu/";
    }


    public static String getSubKategoriMenuDeleteLink(Context context, String id) {
        return getApiUrl(context) + "sub_kategori_menu/delete_sub_kategori_menu/" + id;
    }


    public static String getMenuAddEditLink(Context context) {
        return getApiUrl(context) + "menu/addeditmenu/";
    }


    public static String getMenuDeleteLink(Context context, String id) {
        return getApiUrl(context) + "menu/delete_menu/" + id;
    }



    //Mustahiq
    public static String getMustahiqLink(Context context, int page) {
        return getApiUrl(context) + "mustahiq/mustahiq/" + page;
    }

    public static String getMustahiqDetailLink(Context context, String id) {
        return getApiUrl(context) + "mustahiq/detail_mustahiq/" + id;
    }

    public static String getAddRekomendasiMustahiqLink(Context context, String id) {
        return getApiUrl(context) + "mustahiq/addrekomendasi/" + id;
    }


    public static String getMustahiqAddEditLink(Context context) {
        return getApiUrl(context) + "mustahiq/addedit_mustahiq/";
    }

    public static String getPesananBaruLink(Context context) {
        return getApiUrl(context) + "pesanan/pesanan_baru/";
    }


    public static String getMustahiqDeleteLink(Context context, String id) {
        return getApiUrl(context) + "mustahiq/delete_mustahiq/" + id;
    }


    //donasi
    public static String getDonasiLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "mustahiq/donasi/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }

    public static String getDonasiByLocationLink(Context context, String lat, String Long) {
        return getApiUrl(context) + "mustahiq/donasi_by_location/" + lat + "/" + Long;
    }

    public static String getTesUrl(Context context, String val_url) {
        return "http://" + val_url + "/" + context.getString(R.string.base_url) + "/index.php/tes_url";
    }

    //donasi
    public static String getAmilRestaurantLink(Context context) {
        return getApiUrl(context) + "amil_zakat/all_amil_zakat";
    }

    public static String getLoginLink(Context context) {
        return getApiUrl(context) + "user/login";
    }

    public static String getAbsensiMejaLink(Context context) {
        return getApiUrl(context) + "absensi_meja/absen";
    }

    public static String getRegisterLink(Context context) {
        return getApiUrl(context) + "user/register";
    }

    public static String getAddRatingLink(Context context) {
        return getApiUrl(context) + "rating_mustahiq/addrating";
    }


    //Pesanan
    public static String getPesananSedangDisiapkanLink(Context context, String id) {
        return getApiUrl(context) + "pesanan/disiapkan/" + id;
    }

    public static String getPesananSedangDinikmatiLink(Context context, String id) {
        return getApiUrl(context) + "pesanan/dinikmati/" + id;
    }

    public static String getPesananDibatalkanLink(Context context, String id) {
        return getApiUrl(context) + "pesanan/dibatalkan/" + id;
    }

    public static String getPesananTelahDibayarLink(Context context, String id) {
        return getApiUrl(context) + "pesanan/dibayarkan/" + id;
    }


    public static String getKategoriMenuLink(Context context, int page) {
        return getApiUrl(context) + "kategori_menu/kategori_menu/" + page;
    }

    public static String getSubKategoriMenuLink(Context context, String id, int page) {
        return getApiUrl(context) + "sub_kategori_menu/sub_kategori_menu/" + id + "/" + page;
    }

    public static String getMenuLink(Context context, String id, int page) {
        return getApiUrl(context) + "menu/menu/" + id + "/" + page;
    }
}
