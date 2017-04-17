package com.ecarriers.drivers.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.db.DbDataSource;
import com.ecarriers.drivers.data.db.operations.MarkAsDrivingOp;
import com.ecarriers.drivers.data.remote.SyncUtils;
import com.ecarriers.drivers.data.remote.listeners.ISyncTrips;
import com.ecarriers.drivers.data.remote.pojos.TripsResponse;
import com.ecarriers.drivers.models.Trip;
import com.ecarriers.drivers.utils.Connectivity;
import com.ecarriers.drivers.utils.Constants;
import com.ecarriers.drivers.view.adapters.TripsAdapter;
import com.ecarriers.drivers.view.adapters.listeners.ITripClick;

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

    private AsyncTask currentTask = null;

    // To be shown in setupUI
    private boolean showMessage = false;
    private String message = null;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter == null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    syncTrips();
                }
            });
        } else{
            trips = dbDataSource.getActiveTrips();
            adapter.swap(trips);
        }
    }

    private void setupToolbar(){
        if(toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }
        ActionBar tb = getSupportActionBar();
        if(tb != null) {
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
            showMessage = true;
            message = getResources().getString(R.string.msg_no_connection);
            currentTask = new SortTripsAT(trips).execute();
        }
    }

    @Override
    public void onResponse(boolean success, TripsResponse response) {
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

        currentTask = new SortTripsAT(trips).execute();
    }

    private class SortTripsAT extends AsyncTask<Void, Void, Void> {

        private ArrayList<Trip> _trips;

        public SortTripsAT(ArrayList<Trip> trips){
            this._trips = trips;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Trip.sort(this._trips);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            trips = _trips;
            setupUI();
        }
    }

    private void setupUI(){
        rvTrips.setHasFixedSize(true);

        adapter = new TripsAdapter(getApplicationContext(), trips, this);
        rvTrips.setAdapter(adapter);

        rvTrips.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvTrips.setLayoutManager(new LinearLayoutManager(this));

        if(trips.isEmpty()){
            showEmptyTrips(true);
        }

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);

        if(showMessage && message != null && !message.isEmpty()){
            showMessage(message);
        }
        showMessage = false;
        message = null;
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
        if (trip != null) {
            Intent i = new Intent(TripsActivity.this, TripActivity.class);
            i.putExtra(Constants.KEY_TRIP_ID, trip.getId());
            startActivity(i);
        }
    }

    @Override
    public void onStartTripClick(int position, Trip trip) {
        trip.setState(Trip.TripStates.STATUS_DRIVING.toString());
        boolean success = dbDataSource.updateTrip(trip);
        if(success){
            adapter.notifyItemChanged(position);
            startTripSync(trip);
        }
    }

    private void startTripSync(Trip trip){
        MarkAsDrivingOp op = new MarkAsDrivingOp();
        op.setTripId(trip.getId());

        SyncUtils syncUtils = new SyncUtils(getApplicationContext());
        syncUtils.syncMarkAsDrivingOp(op);
        if(!Connectivity.isConnected(getApplicationContext())){
            showNoConnectionMessage();
        }
    }

    private void showMessage(String message){
        Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING){
            currentTask.cancel(true);
        }
    }

    private void showNoConnectionMessage(){
        Snackbar.make(rvTrips, R.string.msg_no_connection_operations, Toast.LENGTH_LONG).show();
    }
}
