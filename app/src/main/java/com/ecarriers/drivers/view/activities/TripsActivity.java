package com.ecarriers.drivers.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.db.DbDataSource;
import com.ecarriers.drivers.interfaces.ISyncTrips;
import com.ecarriers.drivers.interfaces.ITripClick;
import com.ecarriers.drivers.models.Trip;
import com.ecarriers.drivers.data.remote.SyncUtils;
import com.ecarriers.drivers.data.remote.pojos.TripsResponse;
import com.ecarriers.drivers.utils.Connectivity;
import com.ecarriers.drivers.view.adapters.TripsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripsActivity extends AppCompatActivity implements ISyncTrips, ITripClick {

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout = null;
    @BindView(R.id.rv_trips) RecyclerView rvTrips = null;
    @BindView(R.id.toolbar) Toolbar toolbar = null;
    @BindView(R.id.tv_empty) TextView tvEmpty = null;

    private ArrayList<Trip> trips = null;

    private TripsAdapter adapter = null;
    private DbDataSource dbDataSource = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        ButterKnife.bind(this);

        setupToolbar();
        dbDataSource = new DbDataSource(getApplicationContext());

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncTrips();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                syncTrips();
            }
        });
    }

    private void setupToolbar(){
        if(toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }
        ActionBar tb = getSupportActionBar();
        if(tb != null) {
            tb.setDisplayHomeAsUpEnabled(true);
            //tb.setDisplayShowHomeEnabled(true);
            tb.setDisplayShowTitleEnabled(true);
        }
    }

    private void syncTrips(){
        if(Connectivity.isConnected(getApplicationContext())){
            SyncUtils syncUtils = new SyncUtils(getApplicationContext());
            syncUtils.getActiveTrips(this);
        }else{
            trips = dbDataSource.getActiveTrips();
            if (trips == null){
                trips = new ArrayList<>();
            }
            boolean showMessage = true;
            String message = getResources().getString(R.string.msg_no_connection);
            setupUI(showMessage, message);
        }
    }

    @Override
    public void onResponse(boolean success, TripsResponse response) {
        boolean showMessage = false;
        String message = "";

        if (success && response != null && response.getTrips() != null){
            if (dbDataSource.saveActiveTrips(response.getTrips())){
                trips = dbDataSource.getActiveTrips();
            }else{
                trips = response.getTrips();
            }
        }else{
            showMessage = true;
            message = getResources().getString(R.string.msg_get_trips_error);
            trips = new ArrayList<>();
        }

        setupUI(showMessage, message);
    }

    private void setupUI(boolean showMessage, String message){
        rvTrips.setHasFixedSize(true);

        adapter = new TripsAdapter(getApplicationContext(), trips, this);
        rvTrips.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rvTrips.addItemDecoration(itemDecoration);

        rvTrips.setLayoutManager(new LinearLayoutManager(this));

        if(trips.isEmpty()){
            showEmptyTrips(true);
        }

        swipeRefreshLayout.setRefreshing(false);

        if(showMessage && message != null && !message.isEmpty()){
            showMessage(message);
        }
    }

    private void showEmptyTrips(boolean show){
        if (show){
            rvTrips.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else{
            rvTrips.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTripClick(int position, Trip trip) {
        Snackbar.make(swipeRefreshLayout, "Click: " + String.valueOf(position), Snackbar.LENGTH_LONG).show();
    }

    private void showMessage(String message){
        Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
