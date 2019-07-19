package com.example.eciot.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class TrainingFragment extends Fragment {
    private RequestQueue mQueue;
    private FragmentTrainingBinding mBinding;
    private Button btnAcerto, btnFallo;
    String token;
    private String pesoObtenido, idCategoriaObtenida;
    private String nombreCategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_training,container,
                false);
        mQueue = Volley.newRequestQueue(getContext());
        this.token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNCwidXNlcm5hbWUiOiJzb2RlZ29tZSIsImV4cCI6MTU2MzM3NzYyNywiZW1haWwiOiIifQ.clGa4CQDLvjwWRSyrcJiaQT-8ebQHg5Q0cVzl9mbFoQ";

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
        final TextView peso = mBinding.txtPesoValor;

        String url_temp = "https://amstdb.herokuapp.com/db/registroDePeso/3";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            pesoObtenido=response.getString("peso");
                            System.out.println(pesoObtenido);
                            peso.setText(pesoObtenido + " gramos");

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
                Map<String, String> params = new HashMap<String,
                        String>();
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
        };;
        mQueue.add(request);
        //return nombreCategoria;
    }



    public void postPeso(boolean acierto){
        Map<String, Object> params = new HashMap();

        params.put("clasificador", 1);
        params.put("categoria", Integer.valueOf(idCategoriaObtenida));
        params.put("peso", Float.valueOf(pesoObtenido));
        params.put("acerto", acierto);
        JSONObject parametros = new JSONObject(params);

        String login_url = "http://amstdb.herokuapp.com/db/registroDePeso";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, login_url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);

    }
}
