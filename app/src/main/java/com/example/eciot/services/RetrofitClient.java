package com.example.eciot.services;

import android.os.Build;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
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
        /* ConnectionSpec.MODERN_TLS is the default value */



        return  mRetrofitBuilder
                .baseUrl("http://amstdb.herokuapp.com/db/")
                .build()
                .create(ApiService.class);
    }


}
