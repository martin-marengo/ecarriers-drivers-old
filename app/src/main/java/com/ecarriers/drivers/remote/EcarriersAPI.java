package com.ecarriers.drivers.remote;

import android.content.Context;

import com.ecarriers.drivers.remote.pojos.LoginResponse;
import com.ecarriers.drivers.remote.pojos.TripsResponse;

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
