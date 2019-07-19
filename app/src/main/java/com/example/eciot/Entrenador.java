package com.example.eciot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;



public class Entrenador extends AppCompatActivity {
    private RequestQueue mQueue;

    private Button btnAcerto, btnFallo;
    String token;
    private String pesoObtenido, idCategoriaObtenida;
    private String nombreCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenador);

        mQueue = Volley.newRequestQueue(this);
        this.token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNCwidXNlcm5hbWUiOiJzb2RlZ29tZSIsImV4cCI6MTU2MzM3NzYyNywiZW1haWwiOiIifQ.clGa4CQDLvjwWRSyrcJiaQT-8ebQHg5Q0cVzl9mbFoQ";

        identificarObjeto();

        btnAcerto = (Button) findViewById(R.id.btnAcerto);
        btnFallo = (Button) findViewById(R.id.btnFallo);


        btnAcerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPeso("1");
            }
        });

        btnFallo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPeso("0");
            }
        });
    }


    /*
    Autor: Sophia Gómez
    Función que recibe el id de la categoria y setea la imagen de la categoria a la que pertenece
    el objeto pesado
     */
    public void setImagen(String idCategoria){

        final ImageView image = findViewById(R.id.imgObjeto);

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
        final TextView peso = (TextView) findViewById(R.id.txtPesoValor);

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
        final TextView clasificador = (TextView) findViewById(R.id.txtClasificador);
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



    public void postPeso(String acierto){
        Map<String, String> params = new HashMap();

        params.put("categoria", idCategoriaObtenida);
        params.put("peso", pesoObtenido);
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
