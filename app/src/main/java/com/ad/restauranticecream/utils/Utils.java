package com.ad.restauranticecream.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mia on 29/02/2016.
 */
public class Utils {


    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }


    public static boolean isEmailValid(Context activity, String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static String convertSecondsToHMmSs(long mili) {
        int duration = (int) (mili / 1000);
        int hours = duration / 3600;
        int minutes = (duration / 60) - (hours * 60);
        int seconds = duration - (hours * 3600) - (minutes * 60);
        String formatted;
        if (hours == 0) {
            formatted = String.format("%02d:%02d", minutes, seconds);
        } else {
            formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return formatted;
    }

    public static long converStringtoMS(String d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String x = d.replace("T", " ").substring(0, 19);
        long a = 0;
        try {
            Date date = sdf.parse(x);
            a = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static String millisToShortDHMS(long duration) {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0) {
            res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return res;
    }

    public static void HideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static String FormatWaktu(String waktu) {


        String[] x = waktu.split(" ");

        String[] separated_waktu = x[0].split("-");

        String tahun_waktu = separated_waktu[0];
        String bulan_waktu = separated_waktu[1];
        String hari = separated_waktu[2];

        //misal 12 Jul 2017
        return hari + " " + BulanIndonesia(bulan_waktu) + " " + tahun_waktu +" "+x[1].substring(0,5);
    }

    public static String simpleScheduleDate(String startDate){
        String target = startDate;
        String result = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yy", new Locale("id", "in"));
        try {
            Date res = df.parse(target);
            result = sdf.format(res);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static String FormatWakuJadwal(String waktu_mulai_jadwal, String waktu_akhir_jadwal) {

        String s_waktu_mulai_jadwal = waktu_mulai_jadwal.substring(0, 10);
        String s_waktu_akhir_jadwal = waktu_akhir_jadwal.substring(0, 10);

        String[] separated_waktu_mulai_jadwal = s_waktu_mulai_jadwal.split("-");
        String tahun_waktu_mulai_jadwal = separated_waktu_mulai_jadwal[0];
        String bulan_waktu_mulai_jadwal = separated_waktu_mulai_jadwal[1];
        String hari_waktu_mulai_jadwal = separated_waktu_mulai_jadwal[2];

        String[] separated_waktu_akhir_jadwal = s_waktu_akhir_jadwal.split("-");
        String tahun_waktu_akhir_jadwal = separated_waktu_akhir_jadwal[0];
        String bulan_waktu_akhir_jadwal = separated_waktu_akhir_jadwal[1];
        String hari_waktu_akhir_jadwal = separated_waktu_akhir_jadwal[2];

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);
        // kalau waktu mulai dan waktu akhir sama
        if (s_waktu_mulai_jadwal.equalsIgnoreCase(s_waktu_akhir_jadwal)) {
            // kalau tahun waktu mulai tahun sekarang
            if (tahun_waktu_mulai_jadwal.equals(String.valueOf(current_year)))
                //misal 12 Jul
                return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal);
                // kalau tahun waktu mulai tahun sekarang bukan tahun waktu akhir tahun sekarang
            else
                //misal 12 Jul 2017
                return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " " + tahun_waktu_mulai_jadwal.substring(2, 4) + " - " + hari_waktu_akhir_jadwal + " " + BulanIndonesia(bulan_waktu_akhir_jadwal) + " " + tahun_waktu_akhir_jadwal.substring(2, 4);
        }
        // kalau waktu mulai dan waktu akhir beda
        else {
            // kalau tahun waktu mulai tahun sekarangb
            if (tahun_waktu_mulai_jadwal.equals(String.valueOf(current_year)))
                // kalau tahun waktu mulai tahun sekarang dan tahun waktu akhir tahun sekarang
                if (tahun_waktu_akhir_jadwal.equals(String.valueOf(current_year)))
                    // kalau tahun waktu mulai tahun sekarang dan tahun waktu akhir tahun sekarang dan bulan waktu mulai sama dengan bulan waktu akhir
                    if (BulanIndonesia(bulan_waktu_mulai_jadwal).equals(BulanIndonesia(bulan_waktu_akhir_jadwal)))
                        //misal 12 - 17 Jul
                        return hari_waktu_mulai_jadwal + " - " + hari_waktu_akhir_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal);
                        // kalau tahun waktu mulai tahun sekarang dan tahun waktu akhir tahun sekarang dan bulan waktu mulai tidak sama dengan bulan waktu akhir
                    else
                        //misal 12 Jun - 17 Jul
                        return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " - " + hari_waktu_akhir_jadwal + " " + BulanIndonesia(bulan_waktu_akhir_jadwal);
                    // kalau tahun waktu mulai tahun sekarang bukan tahun waktu akhir tahun sekarang
                else {
                    // kalau tahun waktu akhir adalah 0000
                    if (s_waktu_akhir_jadwal.equals("0000-00-00"))
                        //misal 12 Jun 2017
                        return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " " + tahun_waktu_mulai_jadwal.substring(2, 4);
                    else
                        //misal 12 Jun 2016 - 17 Jul 2017
                        return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " " + tahun_waktu_mulai_jadwal.substring(2, 4) + " - " + hari_waktu_akhir_jadwal + " " + BulanIndonesia(bulan_waktu_akhir_jadwal) + " " + tahun_waktu_akhir_jadwal.substring(2, 4);
                }
                // kalau tahun waktu mulai bukan tahun sekarang
            else {
                // kalau tahun waktu akhir adalah 0000
                if (s_waktu_akhir_jadwal.equals("0000-00-00"))
                    //misal 12 Jun 2017
                    return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " " + tahun_waktu_mulai_jadwal.substring(2, 4);
                    // kalau tahun waktu akhir bukan adalah 0000
                else
                    //misal 12 Jun 2016 - 17 Jul 2017
                    return hari_waktu_mulai_jadwal + " " + BulanIndonesia(bulan_waktu_mulai_jadwal) + " " + tahun_waktu_mulai_jadwal.substring(2, 4) + " - " + hari_waktu_akhir_jadwal + " " + BulanIndonesia(bulan_waktu_akhir_jadwal) + " " + tahun_waktu_akhir_jadwal.substring(2, 4);

            }


        }
    }


    private static String BulanIndonesia(String bulan) {
        String s_bulan = "";
        switch (bulan) {
            case "01":
            case "1":
                s_bulan = "Jan";
                break;
            case "02":
            case "2":
                s_bulan = "Feb";
                break;
            case "03":
            case "3":
                s_bulan = "Mar";
                break;
            case "04":
            case "4":
                s_bulan = "Apr";
                break;
            case "05":
            case "5":
                s_bulan = "Mei";
                break;
            case "06":
            case "6":
                s_bulan = "Jun";
                break;
            case "07":
            case "7":
                s_bulan = "Jul";
                break;
            case "08":
            case "8":
                s_bulan = "Agust";
                break;
            case "09":
            case "9":
                s_bulan = "Sept";
                break;
            case "10":
                s_bulan = "Oct";
                break;
            case "11":
                s_bulan = "Nov";
                break;
            case "12":
                s_bulan = "Des";
                break;
        }
        return s_bulan;
    }

    public static String Rupiah(String s) {
        String ss = s;
        try {
            ss = String.format("%,d", BigInteger.valueOf(Long.parseLong(s)));
        } catch (Exception e) {

        }
        return "Rp."+ss.replace(",", ".");
    }

    public static String Rupiah(int s) {
        String ss = String.valueOf(s);
        try {
            ss = String.format("%,d", BigInteger.valueOf(Long.parseLong(ss)));
        } catch (Exception e) {

        }
        return "Rp."+ss.replace(",", ".");
    }
}
