package com.ecarriers.drivers.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.models.ShipmentPublication;
import com.ecarriers.drivers.view.adapters.listeners.IShipmentPublicationClick;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipmentPublicationAdapter
        extends RecyclerView.Adapter<ShipmentPublicationAdapter.ShipmentPublicationView> {

    private ArrayList<ShipmentPublication> shipmentPublications;
    private Context context;
    private IShipmentPublicationClick clickListener;

    public ShipmentPublicationAdapter(
            Context context,
            ArrayList<ShipmentPublication> list,
            IShipmentPublicationClick clickListener){
        this.context = context;
        shipmentPublications = list;
        this.clickListener = clickListener;
    }

    public void swap(ArrayList<ShipmentPublication> data){
        shipmentPublications.clear();
        shipmentPublications.addAll(data);
        notifyDataSetChanged();
    }

    class ShipmentPublicationView extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_client) TextView tvClient;
        @BindView(R.id.btn_change_state) Button btnChangeState;
        @BindView(R.id.tv_origin_address) TextView tvOriginAddress;
        @BindView(R.id.tv_destination_address) TextView tvDestinationAddress;
        @BindView(R.id.tv_description) TextView tvDescription;

        public ShipmentPublicationView(View itemView){
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO: mostrar items en un dialogo
        }
    }

    @Override
    public ShipmentPublicationView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_shipment_publication, parent, false);

        return new ShipmentPublicationView(itemView);
    }

    @Override
    public void onBindViewHolder(ShipmentPublicationView holder, int position) {
        ShipmentPublication shipmentPublication = shipmentPublications.get(position);

        if(shipmentPublication != null){
            holder.tvClient.setText(shipmentPublication.getClient());
            holder.tvDescription.setText(shipmentPublication.getDescription());
            holder.tvOriginAddress.setText(shipmentPublication.getOriginAddress());
            holder.tvDestinationAddress.setText(shipmentPublication.getDestinationAddress());

            String state;
            if(shipmentPublication.getState().equals(ShipmentPublication.States.STATUS_WAITING_PICKUP.toString())){
                state = context.getString(R.string.text_waiting_pickup_state);
            }else{
                if(shipmentPublication.getState().equals(ShipmentPublication.States.STATUS_BEING_SHIPPED.toString())){
                    state = context.getString(R.string.text_being_shipped_state);
                }else{
                    state = context.getString(R.string.text_delivered_state);
                }
            }
            holder.btnChangeState.setText(state);

            //TODO click listener del btn. Dialogo con radio buttons para elegir estado.
        }
    }

    @Override
    public int getItemCount() {
        return shipmentPublications.size();
    }
}
