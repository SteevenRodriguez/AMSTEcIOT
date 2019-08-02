package com.example.eciot.fragments;

import android.annotation.SuppressLint;
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
import com.example.eciot.models.UltimoRegistro;
import com.example.eciot.services.ApiService;
import com.example.eciot.services.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext());
        progressDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Descargando Datos");
        progressDialog.show();

        final Realm realm = Realm.getDefaultInstance();
        try {
            Token token = realm.where(Token.class).findFirst();
            ApiService apiService = RetrofitClient.createApiService();
            apiService.getLastObject("JWT "+token.getToken()).enqueue(new Callback<UltimoRegistro>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UltimoRegistro> call, retrofit2.Response<UltimoRegistro> response) {
                    if (response.isSuccessful()){
                        ObjectModel object = response.body().getUltimoRegistro();

                        mFragmentClassifyBinding.txtPeso.setText(String.format(Locale.US,"%.4f", object.getPeso())
                                + " gramos");
                        Category category = realm.where(Category.class).
                                equalTo("id",object.getCategoria()).findFirst();
                        mFragmentClassifyBinding.txtClasificador.
                                setText(category.getNombre());
                        setImagen(String.valueOf(category.getId()));

                        realm.close();
                        progressDialog.dismissWithAnimation();


                    }
                }

                @Override
                public void onFailure(Call<UltimoRegistro> call, Throwable t) {
                    progressDialog.dismissWithAnimation();
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
        };
        mQueue.add(request);
        //return nombreCategoria;
    }
}
