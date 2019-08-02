package com.example.eciot.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eciot.R;
import com.example.eciot.databinding.FragmentTrainingBinding;
import com.example.eciot.models.Category;
import com.example.eciot.models.ObjectModel;
import com.example.eciot.models.Token;
import com.example.eciot.models.UltimoRegistro;
import com.example.eciot.services.ApiService;
import com.example.eciot.services.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;


public class TrainingFragment extends Fragment {
    private RequestQueue mQueue;
    private FragmentTrainingBinding mBinding;
    private Button btnAcerto, btnFallo;
    public ObjectModel object;
    private Token token;
    private String pesoObtenido, idCategoriaObtenida;
    private String nombreCategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_training,container,
                false);

        identificarObjeto();

        btnAcerto = mBinding.btnAcerto;
        btnFallo = mBinding.btnFallo;


        btnAcerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPeso(true);
            }
        });

        btnFallo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPeso(false);
            }
        });

        return mBinding.getRoot();
    }


    /*
    Autor: Sophia Gómez
    Función que recibe el id de la categoria y setea la imagen de la categoria a la que pertenece
    el objeto pesado
     */
    public void setImagen(String idCategoria){

        final ImageView image = mBinding.imgObjeto;

        if(idCategoria.equals("2")) {
            image.setImageResource(R.drawable.ic_phone);
        }
        else if(idCategoria.equals("5")){
            image.setImageResource(R.drawable.cuaderno);
        }
        else if(idCategoria.equals("7")){
            image.setImageResource(R.drawable.ic_glass);
        }
        else if(idCategoria.equals("8")){
            image.setImageResource(R.drawable.ic_laptop);
        }
        else if(idCategoria.equals("9")){
            image.setImageResource(R.drawable.ic_plate);
        }
        else if(idCategoria.equals("10")){
            image.setImageResource(R.drawable.ic_unknown);
        }

    }
    /*
    Autor: Sophia Gómez
    Función que al clickear en el botón, sensa el peso del objeto en la balanza
    y muestra una imagen de la clasificación
     */
    public void identificarObjeto(){
        final Realm realm = Realm.getDefaultInstance();
        try {
            Token token = realm.where(Token.class).findFirst();
            ApiService apiService = RetrofitClient.createApiService();
            apiService.getLastObject("JWT "+token.getToken()).enqueue(new Callback<UltimoRegistro>() {
                @Override
                public void onResponse(Call<UltimoRegistro> call, retrofit2.Response<UltimoRegistro> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null) {
                            object = response.body().getUltimoRegistro();
                        }
                        mBinding.txtPesoValor.setText(String.format(Locale.US, "%.4f", object.getPeso()) + " gramos");
                        Category category = realm.where(Category.class).
                                equalTo("id",object.getCategoria()).findFirst();
                        if (category != null) {
                            mBinding.txtClasificador.
                                    setText(category.getNombre());
                        }
                        if (category != null) {
                            setImagen(String.valueOf(category.getId()));
                        }

                        realm.close();


                    }
                }

                @Override
                public void onFailure(Call<UltimoRegistro> call, Throwable t) {

                }
            });

        } catch (Exception e){
            e.getMessage();
        }

    }

    /*
    Autor: Sophia Gómez
    Función que recibe el id de la categoria y devuelve el nombre de la categoria
    obtenida de la base de datos de herokuapp
     */
    public void obtenerCategoria(String idCategoria){
        final TextView clasificador = mBinding.txtClasificador;
        String url_temp = "https://amstdb.herokuapp.com/db/categoria/" + idCategoria;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            nombreCategoria=response.getString("nombre");
                            System.out.println(nombreCategoria);
                            clasificador.setText(nombreCategoria);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String,
                    String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,
                        String>();
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
        //return nombreCategoria;
    }



    public void postPeso(boolean acierto){
        object.setAcerto(acierto);
        final Realm realm = Realm.getDefaultInstance();
        try {
            Token token = realm.where(Token.class).findFirst();
            ApiService apiService = RetrofitClient.createApiService();
            apiService.createObjectModel("JWT "+token.getToken(),object).enqueue(new Callback<ObjectModel>() {
                @Override
                public void onResponse(Call<ObjectModel> call, retrofit2.Response<ObjectModel> response) {
                    if (response.isSuccessful()){

                        Log.d("Acierto","Correcto");
                    }
                }

                @Override
                public void onFailure(Call<ObjectModel> call, Throwable t) {

                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }
}
