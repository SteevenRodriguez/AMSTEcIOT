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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        mQueue = Volley.newRequestQueue(getContext());
        this.token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNCwidXNlcm5hbW" +
                "UiOiJzb2RlZ29tZSIsImV4cCI6MTU2MzM3NzYyNywiZW1haWwiOiIifQ.clGa4CQDLvjwWRSyrcJiaQT-" +
                "8ebQHg5Q0cVzl9mbFoQ";
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
        final TextView peso = mFragmentClassifyBinding.txtPeso;

        String url_temp = "https://amstdb.herokuapp.com/db/registroDePeso/2";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            pesoObtenido=response.getString("peso");
                            System.out.println(pesoObtenido);
                            peso.setText("El peso del objeto es " + pesoObtenido + " gramos");

                            idCategoriaObtenida=response.getString("categoria");
                            System.out.println(idCategoriaObtenida);

                            obtenerCategoria(idCategoriaObtenida);
                            setImagen(idCategoriaObtenida);
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
