package com.ecarriers.drivers.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.preferences.Preferences;
import com.ecarriers.drivers.data.remote.SyncUtils;
import com.ecarriers.drivers.data.remote.listeners.IGenericListener;
import com.ecarriers.drivers.utils.Connectivity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements IGenericListener {

    private static final int MINIMUM_DELAY = 2300;
    private long syncStartTime;
    private long syncEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String ad = Preferences.getServerAddress(getApplicationContext());
        if(ad.isEmpty()){
            Preferences.setServerAddress(getApplicationContext(), getResources().getString(R.string.production_url));
        }

        if(Connectivity.isConnected(getApplicationContext())) {
            SyncUtils syncUtils = new SyncUtils(getApplicationContext());
            syncStartTime = new Date().getTime();
            syncUtils.syncAllOperations(this);
        }else{
            runDelay(MINIMUM_DELAY);
        }
    }

    @Override
    public void onResponse(boolean success, String key) {
        syncEndTime = new Date().getTime();
        long syncDelay = syncEndTime - syncStartTime;
        if(syncDelay < MINIMUM_DELAY){
            runDelay(MINIMUM_DELAY - syncDelay);
        }else{
            startApp();
        }
    }

    private void runDelay(long time){
        new Timer().schedule(new TimerTask(){
            public void run() {
                startApp();
            }
        }, time);
    }

    private void startApp(){
        String ad = Preferences.getServerAddress(getApplicationContext());
        if(ad.isEmpty()){
            Intent i = new Intent(SplashActivity.this, PreferenceActivity.class);
            startActivity(i);
        }else{
            if(Preferences.getSessionToken(getApplicationContext()).isEmpty()){
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

            }else{
                Intent i = new Intent(SplashActivity.this, TripsActivity.class);
                startActivity(i);
            }
        }

    }
}