package com.example.eciot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class DrawerMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue mQueue;
    private TextView txtPeso;
    private Button btnIdentificar;
    String token;
    private String pesoObtenido, idCategoriaObtenida;
    private String nombreCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mQueue = Volley.newRequestQueue(this);
        this.token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNCwidXNlcm5hbWUiOiJzb2RlZ29tZSIsImV4cCI6MTU2MzM3NzYyNywiZW1haWwiOiIifQ.clGa4CQDLvjwWRSyrcJiaQT-8ebQHg5Q0cVzl9mbFoQ";
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        txtPeso =  findViewById(R.id.txtPeso);
        btnIdentificar = findViewById(R.id.btnObjeto);

        btnIdentificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identificarObjeto();
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
        final TextView peso = (TextView) findViewById(R.id.txtPeso);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(getBaseContext(), DrawerMenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getBaseContext(), Entrenador.class);
            startActivity(intent);

        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
