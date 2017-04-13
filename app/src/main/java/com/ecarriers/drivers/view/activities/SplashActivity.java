package com.ecarriers.drivers.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.preferences.Preferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Preferences.setSessionToken(getApplicationContext(), "dummy");
        Preferences.setCurrentUserEmail(getApplicationContext(), "marengo.martin@gmail.com");

        new Timer().schedule(new TimerTask(){
            public void run() {
                if(Preferences.getSessionToken(getApplicationContext()).isEmpty()){
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);

                }else{
                    Intent i = new Intent(SplashActivity.this, TripsActivity.class);
                    startActivity(i);
                }
            }
        }, 1500);
    }
}