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
import com.ecarriers.drivers.data.preferences.Preferences;
import com.ecarriers.drivers.data.remote.listeners.IGenericListener;
import com.ecarriers.drivers.data.remote.listeners.ILoginListener;
import com.ecarriers.drivers.data.remote.listeners.IOperationListener;
import com.ecarriers.drivers.data.remote.listeners.ITripsListener;
import com.ecarriers.drivers.data.remote.requests.LoginRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsBeingShippedRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsDeliveredRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsDrivingRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsFinishedRequest;
import com.ecarriers.drivers.data.remote.requests.ReportLocationRequest;
import com.ecarriers.drivers.data.remote.responses.LoginResponse;
import com.ecarriers.drivers.data.remote.responses.OperationResponse;
import com.ecarriers.drivers.data.remote.responses.TripsResponse;
import com.ecarriers.drivers.models.TripLocation;
import com.ecarriers.drivers.utils.Connectivity;
import com.ecarriers.drivers.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncUtils implements IOperationListener {

    private Context context;
    private OperationsDAO operationsDAO;

    private EcarriersAPI ecarriersAPI;

    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    public static final String LOGIN = "LOGIN";
    public static final String TRIPS = "TRIPS";
    public static final String OPERATIONS = "OPERATIONS";
    public static final String TRIP_LOCATION = "TRIP_LOCATION";
    public static final String ERRORS = "ERRORS";

    private IGenericListener listener = null;

    public SyncUtils(Context context){
        this.context = context;
        operationsDAO = new OperationsDAO(context);
        ecarriersAPI = EcarriersAPI.Factory.getInstance(context);
    }

    public void login(ILoginListener listener, String email, String password){
        attemptLogin(listener, email, password);
    }

    public void getActiveTrips(ITripsListener listener){
        downloadActiveTrips(listener);
    }

    // Run at startup, before downloading active trips.
    public void syncAllOperations(IGenericListener listener){
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

    public void syncLocation(IGenericListener listener, TripLocation tripLocation){
        reportLocation(listener, tripLocation);
    }

    private void syncOperations(){
        OperationsQueue queue = operationsDAO.getQueue();

        OperationsQueue.Operation nextOp = queue.getNextOperation();
        if(nextOp != null){
            syncOperation(nextOp);
        }else{
            // If it get here, is because there is no ops to run or all were successfully executed.
            // Also the listener can be null and just finish without notifying anywhere.
            if(this.listener != null){
                listener.onResponse(SUCCESS, OPERATIONS);
            }
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
            default:
                break;
        }
    }

    private void attemptLogin(final ILoginListener listener, String email, String password){
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = ecarriersAPI.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        listener.onResponse(SUCCESS, response.body());
                    }
                }else{
                    logOnUnsuccesfulResponse(response, LOGIN);
                    listener.onResponse(FAILURE, new LoginResponse());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new LoginResponse());
            }
        });
    }

    private void downloadActiveTrips(final ITripsListener listener){
        Call<TripsResponse> call = ecarriersAPI.getActiveTrips(Preferences.getSessionToken(context));
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

    private void markAsDriving(final IOperationListener listener, final MarkAsDrivingOp op){
        MarkAsDrivingRequest request = new MarkAsDrivingRequest(Preferences.getSessionToken(context), op.getTripId());
        Call<OperationResponse> call = ecarriersAPI.markAsDriving(request);

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                OperationResponse resp = new OperationResponse();

                if(response.isSuccessful()) {
                    operationsDAO.completeOperation(MarkAsDrivingOp.OPERATION_TYPE, op.getTimestamp());

                    resp.setCode(Constants.HTTP_OK);
                    if(response.raw() != null && response.raw().message() != null) {
                        resp.setSuccess(response.raw().message());
                    }

                    listener.onResponse(SUCCESS, resp);
                }else{
                    resp.setCode(response.code());

                    logOnUnsuccesfulResponse(response, MarkAsDrivingOp.TAG);
                    listener.onResponse(FAILURE, resp);
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsFinished(final IOperationListener listener, final MarkAsFinishedOp op){
        MarkAsFinishedRequest request = new MarkAsFinishedRequest(Preferences.getSessionToken(context), op.getTripId());
        Call<OperationResponse> call = ecarriersAPI.markAsFinished(request);

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                OperationResponse resp = new OperationResponse();

                if(response.isSuccessful()) {
                    operationsDAO.completeOperation(MarkAsFinishedOp.OPERATION_TYPE, op.getTimestamp());

                    resp.setCode(Constants.HTTP_OK);
                    if(response.raw() != null && response.raw().message() != null) {
                        resp.setSuccess(response.raw().message());
                    }

                    listener.onResponse(SUCCESS, resp);
                }else{
                    resp.setCode(response.code());

                    logOnUnsuccesfulResponse(response, MarkAsFinishedOp.TAG);
                    listener.onResponse(FAILURE, resp);
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsBeingShipped(final IOperationListener listener, final MarkAsBeingShippedOp op){
        MarkAsBeingShippedRequest request = new MarkAsBeingShippedRequest(Preferences.getSessionToken(context),
                op.getShipmentPublicationId());
        Call<OperationResponse> call = ecarriersAPI.markAsBeingShipped(request);

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                OperationResponse resp = new OperationResponse();

                if(response.isSuccessful()) {
                    operationsDAO.completeOperation(MarkAsBeingShippedOp.OPERATION_TYPE, op.getTimestamp());

                    resp.setCode(Constants.HTTP_OK);
                    if(response.raw() != null && response.raw().message() != null) {
                        resp.setSuccess(response.raw().message());
                    }

                    listener.onResponse(SUCCESS, resp);
                }else{
                    resp.setCode(response.code());

                    logOnUnsuccesfulResponse(response, MarkAsBeingShippedOp.TAG);
                    listener.onResponse(FAILURE, resp);
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void markAsDelivered(final IOperationListener listener, final MarkAsDeliveredOp op){
        MarkAsDeliveredRequest request = new MarkAsDeliveredRequest(Preferences.getSessionToken(context),
                op.getShipmentPublicationId());
        Call<OperationResponse> call = ecarriersAPI.markAsDelivered(request);

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                OperationResponse resp = new OperationResponse();

                if(response.isSuccessful()) {
                    operationsDAO.completeOperation(MarkAsDeliveredOp.OPERATION_TYPE, op.getTimestamp());

                    resp.setCode(Constants.HTTP_OK);
                    if(response.raw() != null && response.raw().message() != null) {
                        resp.setSuccess(response.raw().message());
                    }

                    listener.onResponse(SUCCESS, resp);
                }else{
                    resp.setCode(response.code());

                    logOnUnsuccesfulResponse(response, MarkAsDeliveredOp.TAG);
                    listener.onResponse(FAILURE, resp);
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, new OperationResponse());
            }
        });
    }

    private void reportLocation(final IGenericListener listener, final TripLocation loc){
        ReportLocationRequest request = new ReportLocationRequest(Preferences.getSessionToken(context),
                loc.getTripId(), loc.getLat(), loc.getLng());
        Call<OperationResponse> call = ecarriersAPI.reportLocation(request);

        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        listener.onResponse(SUCCESS,TRIP_LOCATION);
                    }
                }else{
                    logOnUnsuccesfulResponse(response, TRIP_LOCATION);
                    listener.onResponse(FAILURE, TRIP_LOCATION);
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                logOnFailure(t);
                listener.onResponse(FAILURE, TRIP_LOCATION);
            }
        });
    }

    @Override
    public void onResponse(boolean success, OperationResponse response) {
        if(success){
            // In case of success, keep synchronizing ops until there is no more.

            String resp = "";
            if(response.getSuccess() != null){
                resp = response.getSuccess();
            }
            Log.d(OPERATIONS, String.valueOf(response.getCode()) + ": " + resp);

            syncOperations();
        }else{
            // In case of failure, stop running operations and notify the listener if it's needed.
            if(this.listener != null){
                listener.onResponse(FAILURE, OPERATIONS);
            }
        }
    }

    private void logOnFailure(Throwable t){
        try {
            if (t != null && t.getCause() != null) {
                Log.e(ERRORS, t.getCause().getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void logOnUnsuccesfulResponse(Response response, String tag){
        try {
            Log.e(ERRORS, tag + " " + response.errorBody().string());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
