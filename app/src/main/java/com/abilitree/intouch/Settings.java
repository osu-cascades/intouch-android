package com.abilitree.intouch;

import android.content.Context;
import android.preference.PreferenceManager;

public class Settings {

    private static String USERNAME = "USERNAME";
    private static String PASSWORD = "PASSWORD";
    private static String LOGGED_IN = "LOGGED_IN";
    private static String USER_TYPE = "USER_TYPE";

    public static void setLoginSettings(Context context, String username, String password, String usertype) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(USERNAME, username)
            .putString(PASSWORD, password)
            .putBoolean(LOGGED_IN, true)
            .putString(USER_TYPE, usertype)
            .apply();
}

    public static void clearLoginSettings(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(USERNAME, null)
                .putString(PASSWORD, null)
                .putBoolean(LOGGED_IN, false)
                .putString(USER_TYPE, null)
                .apply();

    }

    public static String getUsername(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USERNAME, null);
    }

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PASSWORD, null);
    }

    public static boolean getLoginStatus(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOGGED_IN, false);
    }

    public static String getUserType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_TYPE, "client");

    }

}

