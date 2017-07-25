package com.ad.restauranticecream.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ad.restauranticecream.RestaurantIceCream;

public final class Prefs {

    public static SharedPreferences get(final Context context) {
        return context.getSharedPreferences("com.ad.restauranticecream",
                Context.MODE_PRIVATE);
    }

    public static String getPref(final Context context, String pref, String def) {
        SharedPreferences prefs = Prefs.get(context);
        String val = prefs.getString(pref, def);

        if (TextUtils.isNullOrEmpty(val))
            return def;
        else
            return val;
    }

    public static void putPref(final Context context, String pref, String val) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref, val);
        editor.apply();
    }


    //set URL
    public static String getUrl(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.URL, null);
        return e;
    }

    public static void putUrl(final Context context, String url) {
        Prefs.putPref(context, RestaurantIceCream.URL, url);
    }

    // last selected
    public static int getLastSelected(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.LAST_SELECTED, String.valueOf(RestaurantIceCream.VIEW_TYPE_MUSTAHIQ));
        return Integer.parseInt(e);
    }

    public static void putLastSelected(final Context context, int view_type) {
        Prefs.putPref(context, RestaurantIceCream.LAST_SELECTED, String.valueOf(view_type));
    }

    // last selected
    public static boolean getLogin(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.LOGIN, "false");
        return Boolean.parseBoolean(e);
    }

    public static void putLogin(final Context context, boolean login) {
        Prefs.putPref(context, RestaurantIceCream.LOGIN, String.valueOf(login));
    }

    public static String getIdUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.ID_USER, null);
        return e;
    }

    public static void putIdUser(final Context context, String id_user) {
        Prefs.putPref(context, RestaurantIceCream.ID_USER, id_user);
    }

    public static String getTipeUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.TIPE_USER, "3");
        return e;
    }

    public static void putTipeUser(final Context context, String tipe_user) {
        Prefs.putPref(context, RestaurantIceCream.TIPE_USER, tipe_user);
    }

    public static String getNamaUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.NAMA_USER, null);
        return e;
    }

    public static void putNamaUser(final Context context, String id_user) {
        Prefs.putPref(context, RestaurantIceCream.NAMA_USER, id_user);
    }
    public static String getAlamatUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.ALAMAT_USER, null);
        return e;
    }

    public static void putAlamatUser(final Context context, String id_user) {
        Prefs.putPref(context, RestaurantIceCream.ALAMAT_USER, id_user);
    }
    public static String getNomorIdentitasUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.NOMOR_IDENTITAS_USER, null);
        return e;
    }

    public static void putNomorIdentitasUser(final Context context, String id_user) {
        Prefs.putPref(context, RestaurantIceCream.NOMOR_IDENTITAS_USER, id_user);
    }
    public static String getNomorTelpUser(final Context context) {
        String e = Prefs.getPref(context, RestaurantIceCream.NOMOR_TELP_USER, null);
        return e;
    }

    public static void putNomorTelpUser(final Context context, String id_user) {
        Prefs.putPref(context, RestaurantIceCream.NOMOR_TELP_USER, id_user);
    }

}