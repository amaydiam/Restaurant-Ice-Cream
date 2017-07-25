package com.ad.restauranticecream.utils;

import android.content.Context;

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
    public static String getLaporanDonasiLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "donasi/donasi/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }

    public static String getLaporanDonasiDetailLink(Context context, String id) {
        return getApiUrl(context) + "donasi/detail_donasi/" + id;
    }

    //CalonMustahiq
    public static String getCalonMustahiqLink(Context context, int page) {
        return getApiUrl(context) + "calon_mustahiq/calon_mustahiq/" + page;
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
    public static String getAmilZakatLink(Context context) {
        return getApiUrl(context) + "amil_zakat/all_amil_zakat";
    }

    public static String getLoginLink(Context context) {
        return getApiUrl(context) + "user/login";
    }

    public static String getRegisterLink(Context context) {
        return getApiUrl(context) + "user/register";
    }

    public static String getAddRatingLink(Context context) {
        return getApiUrl(context) + "rating_mustahiq/addrating";
    }



}
