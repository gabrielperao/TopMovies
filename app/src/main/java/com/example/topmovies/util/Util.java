package com.example.topmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Util {

    public static float round(float x) {
        return (float)((int)x * 10 + (int)(x * 10) % 10) / 10;
    }

    public static String getRuntimeStrFormat(int runtime) {
        int hours = runtime / 60;
        int minutes = runtime % 60;
        return hours + "h" + minutes + "min";
    }

    public static String getYearFromDateStr(String dateStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            Date date = formatter.parse(dateStr);
            if (date == null) return "-";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            return "-";
        }
    }

    public static boolean hasNoConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        int type = activeNetwork.getType();
        return (type != ConnectivityManager.TYPE_WIFI && type != ConnectivityManager.TYPE_MOBILE);
    }
}
