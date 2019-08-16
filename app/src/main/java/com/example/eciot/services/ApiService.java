package com.example.eciot.services;

import com.example.eciot.models.Batery;
import com.example.eciot.models.Category;
import com.example.eciot.models.Device;
import com.example.eciot.models.ObjectModel;
import com.example.eciot.models.Token;
import com.example.eciot.models.UltimoRegistro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {

    @POST("nuevo-jwt")
    @FormUrlEncoded
    Call<Token> getToken(@Field("username") String username,
                         @Field("password") String password);

    @GET("categoria")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    @GET("registroDePeso")
    Call<List<ObjectModel>> getObjects(@Header("Authorization") String token);

    @GET("registroDePeso/{pk}")
    Call<ObjectModel> getObject(@Path("pk") int pk, @Header("Authorization") String authorization );

    @PATCH("registroDePeso/{pk}")
    Call<ObjectModel> updateObject(@Path("pk") int pk, @Header("Authorization") String authorization,
                                   @Body ObjectModel objectModel);

    @POST("registroDePeso")
    Call<ObjectModel> createObjectModel(@Header("Authorization") String token,
                                        @Body ObjectModel objectModel);

    @GET("dispositivo/{pk}")
    Call<Batery> getBatery(@Path("pk") int pk, @Header("Authorization") String authorization );


    @GET("dispositivo")
    Call<List<Device>> getDevices(@Header("Authorization") String token);

    @POST("clasificador_objetos/ultimoRegistro")
    Call<UltimoRegistro> getLastObject(@Header("Authorization") String token);










}
