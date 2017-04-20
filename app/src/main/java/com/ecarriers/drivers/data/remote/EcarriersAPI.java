package com.ecarriers.drivers.data.remote;

import android.content.Context;

import com.ecarriers.drivers.data.remote.requests.LoginRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsBeingShippedRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsDeliveredRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsDrivingRequest;
import com.ecarriers.drivers.data.remote.requests.MarkAsFinishedRequest;
import com.ecarriers.drivers.data.remote.requests.ReportLocationRequest;
import com.ecarriers.drivers.data.remote.responses.LoginResponse;
import com.ecarriers.drivers.data.remote.responses.OperationResponse;
import com.ecarriers.drivers.data.remote.responses.TripsResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.ecarriers.drivers.utils.Constants.ECARRIERS_BASE_URL;

public interface EcarriersAPI {

    @POST("carriers/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("trips/non_finished")
    Call<TripsResponse> getActiveTrips(@Query("api_token") String apiToken);

    @PATCH("trips/mark_as_driving")
    Call<OperationResponse> markAsDriving(@Body MarkAsDrivingRequest request);

    @PATCH("trips/mark_as_finished")
    Call<OperationResponse> markAsFinished(@Body MarkAsFinishedRequest request);

    @PATCH("shipment_publication/mark_as_being_shipped")
    Call<OperationResponse> markAsBeingShipped(@Body MarkAsBeingShippedRequest request);

    @PATCH("shipment_publication/mark_as_delivered")
    Call<OperationResponse> markAsDelivered(@Body MarkAsDeliveredRequest request);

    @POST("location_reports/report")
    Call<OperationResponse> reportLocation(@Body ReportLocationRequest request);

    // Not used
//    @GET("trips/{:id}")
//    Call<TripsResponse> getActiveTrips(@Path("id") String tripId, @Query("api_token") String apiToken);

    class Factory {
        private static EcarriersAPI ecarriersAPI;

        public static EcarriersAPI getInstance(Context context){
            if(ecarriersAPI == null) {

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ECARRIERS_BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                return ecarriersAPI = retrofit.create(EcarriersAPI.class);
            }else{
                return ecarriersAPI;
            }
        }

        public static void resetInstance(Context context){
            if(ecarriersAPI != null) {
                ecarriersAPI = null;
            }
        }
    }
}
