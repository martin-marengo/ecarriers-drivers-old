package com.ecarriers.drivers.view.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ecarriers.drivers.R;

public class AppPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
