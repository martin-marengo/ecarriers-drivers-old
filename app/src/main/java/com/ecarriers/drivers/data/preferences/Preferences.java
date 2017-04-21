package com.ecarriers.drivers.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ecarriers.drivers.R;

public class Preferences {

    // Preferences keys

    private static final String COMMON_PREF = "APPDATA";

    private static final String SESSION_TOKEN = "session_token";
    private static final String CURRENT_USER_EMAIL = "current_user_email";
    private static final String OPERATIONS_QUEUE = "operations_queue";

    // Data getters and setters

    // It is only IP:port (port if needed)
    // It is saved on preferences screen automatically
    public static String getServerAddress(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getResources().getString(R.string.key_server_address_preference), "");
    }

    public static void setSessionToken(Context context, String token){
        SharedPreferences.Editor editor = getEditor(context).putString(SESSION_TOKEN, token);
        editor.apply();
    }
    public static String getSessionToken(Context context){
        return getSharedPreferences(context).getString(SESSION_TOKEN, "");
    }

    public static void setCurrentUserEmail(Context context, String email){
        SharedPreferences.Editor editor = getEditor(context).putString(CURRENT_USER_EMAIL, email);
        editor.apply();
    }
    public static String getCurrentUserEmail(Context context){
        return getSharedPreferences(context).getString(CURRENT_USER_EMAIL, "");
    }

    public static void setOperationsQueue(Context context, String queue){
        SharedPreferences.Editor editor = getEditor(context).putString(OPERATIONS_QUEUE, queue);
        editor.apply();
    }
    public static String getOperationsQueue(Context context){
        return getSharedPreferences(context).getString(OPERATIONS_QUEUE, "");
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
