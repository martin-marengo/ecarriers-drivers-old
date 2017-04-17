package com.ecarriers.drivers.data.remote;

import android.content.Context;

import com.ecarriers.drivers.data.remote.pojos.LoginResponse;
import com.ecarriers.drivers.data.remote.pojos.OperationResponse;
import com.ecarriers.drivers.data.remote.pojos.TripsResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.ecarriers.drivers.utils.Constants.ECARRIERS_BASE_URL;

public interface EcarriersAPI {

    @FormUrlEncoded
    @POST("signin")
    Call<LoginResponse> login(@Field("user") String user,
                              @Field("password") String password);

    // All trips either pendings or traveling.
//    @FormUrlEncoded
//    @POST("trips/actives")
//    Call<TripsResponse> getActiveTrips(@Header("token") String token);

    @GET("apidummy/test")
    Call<TripsResponse> getActiveTrips(@Header("Accept") String token);


    @GET("mark_as_driving")
    Call<OperationResponse> markAsDriving(@Header("token") String token,
                                          @Query("trip_id") long tripId);

    @GET("mark_as_finished")
    Call<OperationResponse> markAsFinished(@Header("token") String token,
                                          @Query("trip_id") long tripId);

    @GET("mark_as_being_shipped")
    Call<OperationResponse> markAsBeingShipped(@Header("token") String token,
                                               @Query("shipment_publication_id") long shipmentPublicationId);

    @GET("mark_as_delivered")
    Call<OperationResponse> markAsDelivered(@Header("token") String token,
                                            @Query("shipment_publication_id") long shipmentPublicationId);

    @GET("report_location")
    Call<OperationResponse> reportLocation(@Header("token") String token,
                                           @Query("trip_id") long trip_id,
                                           @Query("lat") double lat,
                                           @Query("lng") double lng);

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
