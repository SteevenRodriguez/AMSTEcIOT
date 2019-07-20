package com.example.eciot.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eciot.R;
import com.example.eciot.databinding.FragmentClassifyBinding;
import com.example.eciot.models.Category;
import com.example.eciot.models.ObjectModel;
import com.example.eciot.models.Token;
import com.example.eciot.services.ApiService;
import com.example.eciot.services.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassifyFragment extends Fragment {
    private FragmentClassifyBinding mFragmentClassifyBinding;
    private RequestQueue mQueue;
    private TextView txtPeso;
    private Button btnIdentificar;
    String token;
    private String pesoObtenido, idCategoriaObtenida;
    private String nombreCategoria;

    public ClassifyFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentClassifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_classify,
                container,false);
        txtPeso =  mFragmentClassifyBinding.txtPeso;
        btnIdentificar = mFragmentClassifyBinding.btnObjeto;

        btnIdentificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identificarObjeto();

            }
        });




        return mFragmentClassifyBinding.getRoot();

    }

    /*
    Autor: Sophia Gómez
    Función que recibe el id de la categoria y setea la imagen de la categoria a la que pertenece
    el objeto pesado
     */
    public void setImagen(String idCategoria){

        final ImageView image = mFragmentClassifyBinding.imgObjeto;

        if(idCategoria.equals("2")) {
            image.setImageResource(R.drawable.celular);
        }
        else if(idCategoria.equals("3")){
            image.setImageResource(R.drawable.lapiz);
        }
        else if(idCategoria.equals("4")){
            image.setImageResource(R.drawable.moneda);
        }
        else if(idCategoria.equals("5")){
            image.setImageResource(R.drawable.cuaderno);
        }
        else if(idCategoria.equals("6")){
            image.setImageResource(R.drawable.esmalte);
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
            apiService.getObject(2,"JWT "+token.getToken()).enqueue(new Callback<ObjectModel>() {
                @Override
                public void onResponse(Call<ObjectModel> call, retrofit2.Response<ObjectModel> response) {
                    if (response.isSuccessful()){
                        ObjectModel obj = response.body();
                        mFragmentClassifyBinding.txtPeso.setText("El peso del objeto es "
                                + obj.getPeso() + " gramos");
                        Category category = realm.where(Category.class).
                                equalTo("id",obj.getCategoria()).findFirst();
                        mFragmentClassifyBinding.txtClasificador.
                                setText("Categoría: "+category.getNombre());
                        setImagen(String.valueOf(category.getId()));

                        realm.close();


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

    /*
    Autor: Sophia Gómez
    Función que recibe el id de la categoria y devuelve el nombre de la categoria
    obtenida de la base de datos de herokuapp
     */
    public void obtenerCategoria(String idCategoria){
        final TextView clasificador = mFragmentClassifyBinding.txtClasificador;
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
                            clasificador.setText("Categoria: " + nombreCategoria);

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
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };;
        mQueue.add(request);
        //return nombreCategoria;
    }
}
