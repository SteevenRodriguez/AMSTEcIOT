package com.example.eciot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

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

    }
}
