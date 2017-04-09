package com.ecarriers.drivers.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.interfaces.ITripClick;
import com.ecarriers.drivers.models.Trip;

import java.util.ArrayList;

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

        TextView tvOrigin;
        TextView tvDestination;
        TextView tvState;

        public TripView(View itemView) {
            super(itemView);

            //tvOrigin = (TextView) itemView.findViewById(R.id.tv_descripcion_tarifa);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

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
    public void onBindViewHolder(TripView viewHolder, int position) {

        //SeleccionTarifa seleccionTarifa = mTrips.get(position);

//        if(seleccionTarifa != null) {
//
//        }
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}

