package com.ecarriers.drivers.view.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.interfaces.ITripClick;
import com.ecarriers.drivers.models.Trip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripView> {

    private ArrayList<Trip> mTrips;
    private Context mContext;
    private ITripClick mTripClickListener = null;

    public TripsAdapter(Context context, ArrayList<Trip> trips, ITripClick clickListener) {
        mTrips = trips;
        mContext = context;
        mTripClickListener = clickListener;
    }

    public void swap(ArrayList<Trip> data){
        mTrips.clear();
        mTrips.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<Trip> getList(){
        return mTrips;
    }

    class TripView extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_origin) TextView tvOrigin;
        @BindView(R.id.tv_destination) TextView tvDestination;
        @BindView(R.id.btn_start_trip) ImageButton btnStartTrip;

        public TripView(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mTripClickListener.onTripClick(getAdapterPosition(), mTrips.get(getAdapterPosition()));
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TripView onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View complejoView = inflater.inflate(R.layout.trip_item, parent, false);

        TripView viewHolder = new TripView(complejoView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final TripView viewHolder, final int position) {

        final Trip trip = mTrips.get(position);

        if (trip != null){
            if(trip.getState().equals(Trip.TripStates.STATUS_DRIVING.toString())) {
                viewHolder.btnStartTrip.setColorFilter(ContextCompat.getColor(mContext, R.color.driving_trip));
            }else{
                viewHolder.btnStartTrip.setColorFilter(ContextCompat.getColor(mContext, R.color.pending_trip));
            }

            viewHolder.tvOrigin.setText(trip.getOrigin());
            viewHolder.tvDestination.setText(trip.getDestination());

            viewHolder.btnStartTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTripClickListener.onStartTripClick(position, trip);
                    viewHolder.btnStartTrip.setColorFilter(ContextCompat.getColor(mContext, R.color.driving_trip));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}

