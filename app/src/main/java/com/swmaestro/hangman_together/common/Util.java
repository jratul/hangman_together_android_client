package com.swmaestro.hangman_together.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class Util {

    public static String getPreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removePreferences(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String strNow = calendar.get(Calendar.YEAR) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" +
                calendar.get(Calendar.SECOND);

        return strNow;
    }
}
