package com.example.eciot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et1,et2;
    private Cursor fila;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logIn = findViewById(R.id.btnInicio);
        et1 = (EditText) findViewById(R.id.txtUsuario);
        et2 = (EditText) findViewById(R.id.txtPassword);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DrawerMenuActivity.class);
                startActivity(intent);

            }
        });



    }
    public void iniciarSesion(View v){
        DBHelper admin = new DBHelper (this,"datos",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String usuario = et1.getText().toString();
        String contrasena = et2.getText().toString();
        fila=db.rawQuery("select usuario,contrasena from usuarios where usuario='"+usuario+"' and contrasena='"+contrasena+"'",null);
        if (fila.moveToFirst()){
            String u= fila.getString(0);
            String p=fila.getString(1);
            if(usuario.equals(u)&& contrasena.equals(p)){
                //Ventana de menu al ingresar
                Intent ingreso= new Intent(this,Menu.class);
                startActivity(ingreso);
                et1.setText("");
                et2.setText("");
            }
            else{
                Toast toast = Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
