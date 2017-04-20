package com.ecarriers.drivers.view.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.db.DbDataSource;
import com.ecarriers.drivers.data.db.operations.MarkAsBeingShippedOp;
import com.ecarriers.drivers.data.db.operations.MarkAsDeliveredOp;
import com.ecarriers.drivers.data.db.operations.MarkAsDrivingOp;
import com.ecarriers.drivers.data.db.operations.MarkAsFinishedOp;
import com.ecarriers.drivers.data.remote.SyncUtils;
import com.ecarriers.drivers.data.remote.listeners.IGenericListener;
import com.ecarriers.drivers.geolocation.Geolocation;
import com.ecarriers.drivers.geolocation.GeolocationUtils;
import com.ecarriers.drivers.geolocation.ILocationListener;
import com.ecarriers.drivers.models.Item;
import com.ecarriers.drivers.models.ShipmentPublication;
import com.ecarriers.drivers.models.Trip;
import com.ecarriers.drivers.models.TripLocation;
import com.ecarriers.drivers.utils.Connectivity;
import com.ecarriers.drivers.utils.Constants;
import com.ecarriers.drivers.utils.DateUtils;
import com.ecarriers.drivers.view.adapters.ShipmentPublicationAdapter;
import com.ecarriers.drivers.view.adapters.listeners.IShipmentPublicationClick;

import org.apache.commons.lang3.text.WordUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ecarriers.drivers.geolocation.Geolocation.geolocation;

public class TripActivity extends AppCompatActivity implements
        IShipmentPublicationClick, ILocationListener, IGenericListener {

    private Trip trip;
    private ShipmentPublicationAdapter shipmentPublicationAdapter;

    private DbDataSource dbDataSource = null;

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
    @BindView(R.id.main_scrollview) NestedScrollView mainScrollView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        dbDataSource = new DbDataSource(getApplicationContext());

        setupTrip();

        ButterKnife.bind(this);

        setupToolbar();

        if(GeolocationUtils.locationPermissionGranted(getApplicationContext())){
            // Get instance and start listening location updates
            geolocation = Geolocation.getInstance(getApplicationContext(), this);
        }

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
            toolbar.setTitle(getResources().getString(R.string.trip_activity_title));
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

            setTvStateText();

            tvOrigin.setText(trip.getOrigin());
            tvDestination.setText(trip.getDestination());

            String visualDepDate = DateUtils.apiToLongDate(trip.getDepartureDate());
            if (visualDepDate != null && !visualDepDate.equals("")){
                visualDepDate = WordUtils.capitalizeFully(visualDepDate);
                layoutDepartureDate.setVisibility(View.VISIBLE);
                tvDepartureDate.setText(visualDepDate);
            } else{
                layoutDepartureDate.setVisibility(View.GONE);
            }

            toggleButtons();

            setupRecyclerView();
        }
    }

    private void setTvStateText(){
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
    }

    @OnClick(R.id.btn_start_trip)
    void onStartTripClick(View view){
        trip.setState(Trip.TripStates.STATUS_DRIVING.toString());
        boolean success = dbDataSource.updateTrip(trip);
        if(success){
            toggleButtons();
            setTvStateText();
            startTripSync(trip);
        }
    }

    private void startTripSync(Trip trip){
        MarkAsDrivingOp op = new MarkAsDrivingOp();
        op.setTripId(trip.getId());

        SyncUtils syncUtils = new SyncUtils(getApplicationContext());
        syncUtils.syncMarkAsDrivingOp(op);
        if(!Connectivity.isConnected(getApplicationContext())){
            showNoConnectionMessageOnOperation();
        }
    }

    @OnClick(R.id.btn_finish_trip)
    void onFinishTripClick(View view){
        if(trip.canFinish()) {
            showConfirmFinishTripDialog();
        }else{
            showCantFinishTripDialog();
        }
    }

    @OnClick(R.id.btn_report_location)
    void onReportLocationClick(View view){
        if(GeolocationUtils.locationPermissionGranted(getApplicationContext())) {
            if (Connectivity.isConnected(getApplicationContext())) {
                if (geolocation == null) {
                    geolocation = Geolocation.getInstance(getApplicationContext(), this);
                }
                showProgressBar(true);
                // onLocationUpdated is the callback where we are going to receive the location.
                geolocation.getLastLocation();
            } else {
                showNoConnectionMessage();
            }
        }else{
            showNoLocationPermissionMessage();
        }
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
        rvShipmentPublications.setNestedScrollingEnabled(false);

        shipmentPublicationAdapter =
                new ShipmentPublicationAdapter(getApplicationContext(), trip.getShipmentPublications(), this);
        rvShipmentPublications.setAdapter(shipmentPublicationAdapter);

        rvShipmentPublications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvShipmentPublications.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onShipmentPublicationClick(int position, ShipmentPublication sp) {
        if(sp.getItems() != null && !sp.getItems().isEmpty()) {
            showItemsDialog(sp);
        }
    }

    @Override
    public void onChangeStateClick(int position, ShipmentPublication sp) {
        if(!trip.getState().equals(Trip.TripStates.STATUS_FINISHED.toString())) {
            showChangeStateDialog(position, sp);
        }
    }

    private void showConfirmFinishTripDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_confirm_finish_trip));
        builder.setMessage(getResources().getString(R.string.msg_confirm_finish_trip));
        builder.setPositiveButton(getResources().getString(R.string.action_finish_trip), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishTrip();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void finishTrip(){
        trip.setState(Trip.TripStates.STATUS_FINISHED.toString());
        boolean success = dbDataSource.updateTrip(trip);
        if (success) {
            toggleButtons();
            setTvStateText();
            finishTripSync(trip);
        }
    }

    private void finishTripSync(Trip trip){
        MarkAsFinishedOp op = new MarkAsFinishedOp();
        op.setTripId(trip.getId());

        SyncUtils syncUtils = new SyncUtils(getApplicationContext());
        syncUtils.syncMarkAsFinishedOp(op);
        if(!Connectivity.isConnected(getApplicationContext())) {
            showNoConnectionMessageOnOperation();
        }
    }

    private void showItemsDialog(ShipmentPublication sp){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_item_list) + sp.getClient());
        builder.setItems(Item.getArrayFromList(sp.getItems()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing on item click.
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.action_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showChangeStateDialog(final int position, final ShipmentPublication sp){
        final String[] states = ShipmentPublication.getStatesArray(getApplicationContext());
        int checkedStateIndex = sp.getStateIndex();
        final String originalStateValue = sp.getState();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sp.getClient());
        builder.setSingleChoiceItems(states, checkedStateIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.setState(ShipmentPublication.States.values()[which].toString());
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancelChangeStateDialog(sp, originalStateValue);
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.action_change_state), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onShipmentPublicationStateChanged(position, sp);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onCancelChangeStateDialog(sp, originalStateValue);
            }
        });
        builder.show();
    }

    private void onCancelChangeStateDialog(ShipmentPublication sp, String originalStateValue){
        sp.setState(originalStateValue);
    }

    private void onShipmentPublicationStateChanged(final int position, final ShipmentPublication sp){
        boolean success = dbDataSource.updateShipmentPublication(sp);
        if (success){
            shipmentPublicationAdapter.notifyItemChanged(position);
            // Note: there is no endpoint in the API for marking a shipment publication as waiting pickup,
            // so that state will be managed internally in the app. It's wrong, but I don't give a shit.
            if(sp.getState().equals(ShipmentPublication.States.STATUS_BEING_SHIPPED.toString())){
                syncMarkAsBeingShipped(sp);
            }
            if(sp.getState().equals(ShipmentPublication.States.STATUS_DELIVERED.toString())){
                syncMarkAsDelivered(sp);
            }
        }
    }

    private void syncMarkAsBeingShipped(ShipmentPublication sp){
        MarkAsBeingShippedOp op = new MarkAsBeingShippedOp();
        op.setShipmentPublicationId(sp.getId());

        SyncUtils syncUtils = new SyncUtils(getApplicationContext());
        syncUtils.syncMarkAsBeingShippedOp(op);
        if(!Connectivity.isConnected(getApplicationContext())) {
            showNoConnectionMessageOnOperation();
        }
    }

    private void syncMarkAsDelivered(ShipmentPublication sp){
        MarkAsDeliveredOp op = new MarkAsDeliveredOp();
        op.setShipmentPublicationId(sp.getId());

        SyncUtils syncUtils = new SyncUtils(getApplicationContext());
        syncUtils.syncMarkAsDeliveredOp(op);
        if(!Connectivity.isConnected(getApplicationContext())) {
            showNoConnectionMessageOnOperation();
        }
    }

    private void showCantFinishTripDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_cant_finish_trip));
        builder.setMessage(getResources().getString(R.string.msg_cant_finish_trip));
        builder.setPositiveButton(getResources().getString(R.string.action_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void exit(){
        finish();
    }

    private void showNoConnectionMessageOnOperation(){
        Snackbar.make(toolbar, R.string.msg_no_connection_operations, Snackbar.LENGTH_LONG).show();
    }

    private void showNoConnectionMessage(){
        Snackbar.make(toolbar, R.string.msg_no_connection, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onLocationUpdated(boolean exito, Location location) {
        showProgressBar(false);
        if(location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            GeolocationUtils.printLocation(location);

            if(lat != GeolocationUtils.INVALID_GEO_VALUES && lng != GeolocationUtils.INVALID_GEO_VALUES){
                TripLocation tripLocation = new TripLocation();
                tripLocation.setTripId(trip.getId());
                tripLocation.setLat(lat);
                tripLocation.setLng(lng);

                showLocationDialog(tripLocation);
            }else{
                showNoLocationMessage();
            }
        }else{
            showNoLocationMessage();
        }
    }

    private void showLocationDialog(final TripLocation tripLocation){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_send_location));
        builder.setMessage(getResources().getString(R.string.msg_send_location));

        builder.setPositiveButton(getResources().getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showProgressBar(true);
                reportLocation(tripLocation);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void reportLocation(final TripLocation tripLocation){
        if(Connectivity.isConnected(getApplicationContext())) {
            showProgressBar(true);
            SyncUtils syncUtils = new SyncUtils(getApplicationContext());
            syncUtils.syncLocation(this, tripLocation);
        }else{
            showNoConnectionMessage();
        }
    }

    @Override
    public void onResponse(boolean success, String key) {
        showProgressBar(false);
        if(success && key.equals(SyncUtils.TRIP_LOCATION)){
            Snackbar.make(toolbar, R.string.msg_report_location_ok, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(toolbar, R.string.msg_report_location_error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showNoLocationMessage(){
        Snackbar.make(toolbar, R.string.msg_no_location_error, Snackbar.LENGTH_LONG).show();
    }

    private void showNoLocationPermissionMessage(){
        Snackbar.make(toolbar, R.string.msg_no_location_permission, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(geolocation != null){
            geolocation.stopListeningUpdates();
        }
    }

    private void showProgressBar(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mainScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        mainScrollView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


}