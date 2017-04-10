package com.ecarriers.drivers.remote;

import android.content.Context;
import android.util.Log;

import com.ecarriers.drivers.interfaces.IAsyncResponse;
import com.ecarriers.drivers.interfaces.ISyncTrips;
import com.ecarriers.drivers.remote.pojos.TripsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncUtils {

    private Context mContext;

    private EcarriersAPI mEcarriersAPI;

    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    private static final String TRIPS = "TRIPS";

    private IAsyncResponse mListener = null;

    public SyncUtils(Context context){
        mContext = context;
    }

    public void getActiveTrips(ISyncTrips listener){
        mEcarriersAPI = EcarriersAPI.Factory.getInstance(mContext);
        downloadActiveTrips(listener);
    }

    private void downloadActiveTrips(final ISyncTrips listener){
        //Call<TripsResponse> call = mEcarriersAPI.getActiveTrips(Preferences.getSessionToken(mContext));
        Call<TripsResponse> call = mEcarriersAPI.getActiveTrips("application/json");
        call.enqueue(new Callback<TripsResponse>() {
            @Override
            public void onResponse(Call<TripsResponse> call, Response<TripsResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    try {
                        Log.e("ERROR", TRIPS + " " + response.errorBody().string());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    listener.onResponse(FAILURE, new TripsResponse());
                }
            }

            @Override
            public void onFailure(Call<TripsResponse> call, Throwable t) {
                try {
                    if (t != null && t.getCause() != null) {
                        Log.e("ERROR", t.getCause().getMessage());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                listener.onResponse(FAILURE, new TripsResponse());
            }
        });
    }
}
