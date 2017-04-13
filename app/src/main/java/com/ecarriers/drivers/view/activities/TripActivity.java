package com.ecarriers.drivers.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.db.DbDataSource;
import com.ecarriers.drivers.models.ShipmentPublication;
import com.ecarriers.drivers.models.Trip;
import com.ecarriers.drivers.utils.Constants;
import com.ecarriers.drivers.utils.DateUtils;
import com.ecarriers.drivers.view.adapters.ShipmentPublicationAdapter;
import com.ecarriers.drivers.view.adapters.listeners.IShipmentPublicationClick;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripActivity extends AppCompatActivity implements IShipmentPublicationClick {

    private Trip trip;
    private ShipmentPublicationAdapter shipmentPublicationAdapter;

    @BindView(R.id.tv_trip_state) TextView tvTripState;
    @BindView(R.id.tv_origin) TextView tvOrigin;
    @BindView(R.id.tv_destination) TextView tvDestination;
    @BindView(R.id.layout_date) LinearLayout layoutDepartureDate;
    @BindView(R.id.tv_departure_date)TextView tvDepartureDate;
    @BindView(R.id.btn_start_trip) Button btnStartTrip;
    @BindView(R.id.btn_report_location) Button btnReportLocation;
    @BindView(R.id.btn_finish_trip) Button btnFinishTrip;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_shipment_publications) RecyclerView rvShipmentPublications;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        setupTrip();

        ButterKnife.bind(this);

        setupToolbar();

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_menu, menu);
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

    private void setupTrip(){
        long tripId = -1;
        if (getIntent().hasExtra(Constants.KEY_TRIP_ID)) {
            tripId = getIntent().getLongExtra(Constants.KEY_TRIP_ID, -1);
        }

        if (tripId != -1){
            DbDataSource dbDataSource = new DbDataSource(getApplicationContext());
            trip = dbDataSource.getActiveTrip(tripId);
        }
    }

    private void setupToolbar(){
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.trip_actvuty_title));
            setSupportActionBar(toolbar);
        }

        ActionBar tb = getSupportActionBar();
        if (tb != null) {
            tb.setDisplayShowTitleEnabled(true);
            tb.setDisplayHomeAsUpEnabled(true);
            tb.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupUI(){
        if (trip != null){

            String visualState = "";
            if(trip.getState().equals(Trip.TripStates.STATUS_HAVE_TO_FETCH_ITEMS.toString())){
                visualState = getResources().getString(R.string.text_trip_pending);
            }
            if(trip.getState().equals(Trip.TripStates.STATUS_DRIVING.toString())){
                visualState = getResources().getString(R.string.text_trip_driving);
            }
            if(trip.getState().equals(Trip.TripStates.STATUS_FINISHED.toString())){
                visualState = getResources().getString(R.string.text_trip_finished);
            }
            tvTripState.setText(visualState);

            tvOrigin.setText(trip.getOrigin());
            tvDestination.setText(trip.getDestination());

            // TODO: borrar esto
            trip.setDepartureDate("2017-04-13 15:00");

            String visualDepDate = DateUtils.apiToVisual(trip.getDepartureDate());
            if (visualDepDate != null && !visualDepDate.equals("")){
                visualDepDate += getResources().getString(R.string.suffix_hour);
                layoutDepartureDate.setVisibility(View.VISIBLE);
                tvDepartureDate.setText(visualDepDate);
            } else{
                layoutDepartureDate.setVisibility(View.GONE);
            }

            btnStartTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStartTripClick();
                }
            });

            btnFinishTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFinishTripClick();
                }
            });

            btnReportLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onReportLocationClick();
                }
            });

            toggleButtons();

            setupRecyclerView();
        }
    }

    private void onStartTripClick(){

    }

    private void onFinishTripClick(){

    }

    private void onReportLocationClick(){

    }

    private void toggleButtons(){
        if (trip.getState().equals(Trip.TripStates.STATUS_HAVE_TO_FETCH_ITEMS.toString())){
            btnReportLocation.setVisibility(View.GONE);
            btnReportLocation.setEnabled(false);

            btnFinishTrip.setVisibility(View.GONE);
            btnFinishTrip.setEnabled(false);

            btnStartTrip.setVisibility(View.VISIBLE);
            btnStartTrip.setEnabled(true);
        }

        if (trip.getState().equals(Trip.TripStates.STATUS_DRIVING.toString())){
            btnReportLocation.setVisibility(View.VISIBLE);
            btnReportLocation.setEnabled(true);

            btnFinishTrip.setVisibility(View.VISIBLE);
            btnFinishTrip.setEnabled(true);

            btnStartTrip.setVisibility(View.GONE);
            btnStartTrip.setEnabled(false);
        }

        if (trip.getState().equals(Trip.TripStates.STATUS_FINISHED.toString())){
            btnReportLocation.setVisibility(View.GONE);
            btnReportLocation.setEnabled(false);

            btnFinishTrip.setVisibility(View.GONE);
            btnFinishTrip.setEnabled(false);

            btnStartTrip.setVisibility(View.GONE);
            btnStartTrip.setEnabled(false);
        }
    }

    private void setupRecyclerView(){
        rvShipmentPublications.setHasFixedSize(true);

        shipmentPublicationAdapter =
                new ShipmentPublicationAdapter(getApplicationContext(), trip.getShipmentPublications(), this);
        rvShipmentPublications.setAdapter(shipmentPublicationAdapter);

        rvShipmentPublications.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onShipmentPublicationClick(int position, ShipmentPublication item) {

    }

    private void exit(){
        finish();
    }
}
