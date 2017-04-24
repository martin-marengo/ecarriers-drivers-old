package com.ecarriers.drivers.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.preferences.Preferences;
import com.ecarriers.drivers.data.remote.EcarriersAPI;
import com.ecarriers.drivers.view.fragments.AppPreferencesFragment;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.action_settings));
            setSupportActionBar(toolbar);
        }
        ActionBar tb = getSupportActionBar();
        if(tb != null) {
            tb.setDisplayHomeAsUpEnabled(true);
            tb.setDisplayShowHomeEnabled(true);
            tb.setDisplayShowTitleEnabled(true);
        }

        getFragmentManager().beginTransaction().
                replace(R.id.content_frame, new AppPreferencesFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //HOME
        if (id == android.R.id.home) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void exit(){
        // Reset API instance cos the server IP could been changed.
        EcarriersAPI.Factory.resetInstance(getApplicationContext());

        if(Preferences.getServerAddress(getApplicationContext()).isEmpty()){
            finish();
        } else {
            Intent i = new Intent(PreferenceActivity.this, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
