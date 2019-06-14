package com.example.eciot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class IdentificarObjeto extends AppCompatActivity {
    private EditText txtPeso;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_objeto);

        txtPeso =  (EditText) findViewById(R.id.txtPeso);
        btn = (Button) findViewById(R.id.btnObjeto);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyObject();
            }
        });



    }

    public void identifyObject(){
        String pesoString = txtPeso.getText().toString();
        Integer peso = Integer.parseInt(pesoString);

        ImageView image =   (ImageView) findViewById(R.id.img_objeto);

        if(peso<2) {
            image.setImageResource(R.drawable.extra_liviano);
        }
        else if(peso <4){
            image.setImageResource(R.drawable.liviano);
        }
        else if(peso < 6){
            image.setImageResource(R.drawable.promedio);
        }
        else if(peso <8){
            image.setImageResource(R.drawable.pesado);
        }
        else{
            image.setImageResource(R.drawable.extra_pesado);
        }

    }
}
