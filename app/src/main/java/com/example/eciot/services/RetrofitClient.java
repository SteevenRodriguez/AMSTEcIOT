package com.example.eciot.services;

import com.google.gson.GsonBuilder;

import java.util.Date;
import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /**
     * Build simple REST adapter
     */

    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory
            .create(new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create());

    private static Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory);

    public static ApiService createApiService() {
        return  mRetrofitBuilder
                .baseUrl("http://amstdb.herokuapp.com/db/")
                .build()
                .create(ApiService.class);
    }


}
