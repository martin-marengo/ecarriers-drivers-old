package com.ecarriers.drivers.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    // Preferences keys

    private static final String COMMON_PREF = "APPDATA";

    private static final String CURRENT_TRIP_ID = "current_trip_id";
    private static final String SESSION_TOKEN = "session_token";
    private static final String CURRENT_USER_EMAIL = "current_user_email";

    // Data getters and setters

    public static void setSessionToken(Context context, String token){
        SharedPreferences.Editor editor = getEditor(context).putString(SESSION_TOKEN, token);
        editor.apply();
    }
    public static String getSessionToken(Context context){
        return getSharedPreferences(context).getString(SESSION_TOKEN, "");
    }

    public static void setCurrentTripId(Context context, long id){
        SharedPreferences.Editor editor = getEditor(context).putLong(CURRENT_TRIP_ID, id);
        editor.apply();
    }
    public static long getCurrentTripId(Context context){
        return getSharedPreferences(context).getLong(CURRENT_TRIP_ID, -1);
    }

    public static void setCurrentUserEmail(Context context, String email){
        SharedPreferences.Editor editor = getEditor(context).putString(CURRENT_USER_EMAIL, email);
        editor.apply();
    }
    public static String getCurrentUserEmail(Context context){
        return getSharedPreferences(context).getString(CURRENT_USER_EMAIL, "");
    }

    // SharedPreferences getters

    private static SharedPreferences.Editor getEditor(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        return editor;
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(COMMON_PREF, Context.MODE_PRIVATE);
    }
}
