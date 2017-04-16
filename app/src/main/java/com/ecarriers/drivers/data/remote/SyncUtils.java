package com.ecarriers.drivers.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ecarriers.drivers.data.db.operations.MarkAsBeingShippedOp;
import com.ecarriers.drivers.data.db.operations.MarkAsDeliveredOp;
import com.ecarriers.drivers.data.db.operations.MarkAsDrivingOp;
import com.ecarriers.drivers.data.db.operations.MarkAsFinishedOp;
import com.ecarriers.drivers.data.db.operations.OperationsDAO;
import com.ecarriers.drivers.data.db.operations.OperationsQueue;
import com.ecarriers.drivers.data.db.operations.ReportLocationOp;
import com.ecarriers.drivers.data.preferences.Preferences;
import com.ecarriers.drivers.data.remote.listeners.IAsyncResponse;
import com.ecarriers.drivers.data.remote.listeners.ISyncOperation;
import com.ecarriers.drivers.data.remote.listeners.ISyncTrips;
import com.ecarriers.drivers.data.remote.pojos.OperationResponse;
import com.ecarriers.drivers.data.remote.pojos.TripsResponse;
import com.ecarriers.drivers.utils.Connectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncUtils implements ISyncOperation {

    private Context context;
    private OperationsDAO operationsDAO;

    private EcarriersAPI ecarriersAPI;

    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    private static final String TRIPS = "TRIPS";

    private IAsyncResponse listener = null;

    public SyncUtils(Context context){
        this.context = context;
        operationsDAO = new OperationsDAO(context);
    }

    public void getActiveTrips(ISyncTrips listener){
        ecarriersAPI = EcarriersAPI.Factory.getInstance(context);
        downloadActiveTrips(listener);
    }

    public void syncAllOperations(IAsyncResponse listener){
        this.listener = listener;
        syncOperations();
    }

    public void syncMarkAsDrivingOp(@NonNull MarkAsDrivingOp operation){
        this.listener = null;
        boolean enqueued = operationsDAO.enqueueMarkAsDrivingOp(operation);

        if(enqueued && Connectivity.isConnected(context)){
            syncOperations();
        }
    }

    public void syncMarkAsFinishedOp(@NonNull MarkAsFinishedOp operation){
        this.listener = null;
        boolean enqueued = operationsDAO.enqueueMarkAsFinishedOp(operation);

        if(enqueued && Connectivity.isConnected(context)){
            syncOperations();
        }
    }

    public void syncMarkAsBeingShippedOp(@NonNull MarkAsBeingShippedOp operation){
        this.listener = null;
        boolean enqueued = operationsDAO.enqueueMarkAsBeingShippedOp(operation);

        if(enqueued && Connectivity.isConnected(context)){
            syncOperations();
        }
    }

    public void syncMarkAsDeliveredOp(@NonNull MarkAsDeliveredOp operation){
        this.listener = null;
        boolean enqueued = operationsDAO.enqueueMarkAsDeliveredOp(operation);

        if(enqueued && Connectivity.isConnected(context)){
            syncOperations();
        }
    }

    public void syncReportLocationOp(@NonNull ReportLocationOp operation){
        this.listener = null;
        boolean enqueued = operationsDAO.enqueueReportLocationOp(operation);

        if(enqueued && Connectivity.isConnected(context)){
            syncOperations();
        }
    }

    private void syncOperations(){
        OperationsQueue queue = operationsDAO.getQueue();

        OperationsQueue.Operation nextOp = queue.getNextOperation();
        if(nextOp != null){
            syncOperation(nextOp);
        }
    }

    private void syncOperation(OperationsQueue.Operation op){
        switch (op.getOperationType()){
            case MarkAsDrivingOp.OPERATION_TYPE:
                MarkAsDrivingOp markAsDrivingOp = operationsDAO.getMarkAsDrivingOp(op.getTimestamp());
                if(markAsDrivingOp != null){
                    markAsDriving(this, markAsDrivingOp);
                }
                break;

            case MarkAsFinishedOp.OPERATION_TYPE:
                MarkAsFinishedOp markAsFinishedOp = operationsDAO.getMarkAsFinishedOp(op.getTimestamp());
                if(markAsFinishedOp != null){
                    markAsFinished(this, markAsFinishedOp);
                }
                break;

            case MarkAsBeingShippedOp.OPERATION_TYPE:
                MarkAsBeingShippedOp markAsBeingShippedOp = operationsDAO.getMarkAsBeingShippedOp(op.getTimestamp());
                if(markAsBeingShippedOp != null){
                    markAsBeingShipped(this, markAsBeingShippedOp);
                }
                break;

            case MarkAsDeliveredOp.OPERATION_TYPE:
                MarkAsDeliveredOp markAsDeliveredOp = operationsDAO.getMarkAsDeliveredOp(op.getTimestamp());
                if(markAsDeliveredOp != null){
                    markAsDelivered(this, markAsDeliveredOp);
                }
                break;

            case ReportLocationOp.OPERATION_TYPE:
                ReportLocationOp reportLocationOp = operationsDAO.getReportLocationOp(op.getTimestamp());
                if(reportLocationOp != null){
                    reportLocation(this, reportLocationOp);
                }
                break;

            default:
                break;
        }
    }

    private void downloadActiveTrips(final ISyncTrips listener){
        // TODO: corregir
        //Call<TripsResponse> call = ecarriersAPI.getActiveTrips(Preferences.getSessionToken(context));
        Call<TripsResponse> call = ecarriersAPI.getActiveTrips("application/json");
        call.enqueue(new Callback<TripsResponse>() {
            @Override
            public void onResponse(Call<TripsResponse> call, Response<TripsResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, TRIPS);
                    listener.onResponse(FAILURE, new TripsResponse());
                }
            }

            @Override
            public void onFailure(Call<TripsResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new TripsResponse());
            }
        });
    }

    private void markAsDriving(final ISyncOperation listener, final MarkAsDrivingOp op){
        Call<OperationResponse> call = ecarriersAPI.markAsDriving(
                Preferences.getSessionToken(context), op.getTripId());

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        operationsDAO.completeOperation(MarkAsDrivingOp.OPERATION_TYPE, op.getTimestamp());
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, MarkAsDrivingOp.TAG);
                    listener.onResponse(FAILURE, new OperationResponse());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsFinished(final ISyncOperation listener, final MarkAsFinishedOp op){
        Call<OperationResponse> call = ecarriersAPI.markAsFinished(
                Preferences.getSessionToken(context), op.getTripId());

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        operationsDAO.completeOperation(MarkAsFinishedOp.OPERATION_TYPE, op.getTimestamp());
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, MarkAsFinishedOp.TAG);
                    listener.onResponse(FAILURE, new OperationResponse());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsBeingShipped(final ISyncOperation listener, final MarkAsBeingShippedOp op){
        Call<OperationResponse> call = ecarriersAPI.markAsBeingShipped(
                Preferences.getSessionToken(context), op.getShipmentPublicationId());

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        operationsDAO.completeOperation(MarkAsBeingShippedOp.OPERATION_TYPE, op.getTimestamp());
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, MarkAsBeingShippedOp.TAG);
                    listener.onResponse(FAILURE, new OperationResponse());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsDelivered(final ISyncOperation listener, final MarkAsDeliveredOp op){
        Call<OperationResponse> call = ecarriersAPI.markAsDelivered(
                Preferences.getSessionToken(context), op.getShipmentPublicationId());

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        operationsDAO.completeOperation(MarkAsDeliveredOp.OPERATION_TYPE, op.getTimestamp());
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, MarkAsDeliveredOp.TAG);
                    listener.onResponse(FAILURE, new OperationResponse());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void reportLocation(final ISyncOperation listener, final ReportLocationOp op){
        Call<OperationResponse> call = ecarriersAPI.reportLocation(
                Preferences.getSessionToken(context), op.getTripId(), op.getLat(), op.getLng());

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        operationsDAO.completeOperation(ReportLocationOp.OPERATION_TYPE, op.getTimestamp());
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, ReportLocationOp.TAG);
                    listener.onResponse(FAILURE, new OperationResponse());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    @Override
    public void onResponse(boolean success, OperationResponse response) {

    }

    private void logOnFailure(Throwable t){
        try {
            if (t != null && t.getCause() != null) {
                Log.e("ERROR", t.getCause().getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void logOnUnsuccesfulResponse(Response response, String tag){
        try {
            Log.e("ERROR", tag + " " + response.errorBody().string());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
