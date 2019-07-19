package com.example.eciot.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eciot.DBHelper;
import com.example.eciot.R;
import com.example.eciot.databinding.ActivityMainBinding;
import com.example.eciot.models.Token;
import com.example.eciot.models.User;
import com.example.eciot.services.ApiService;
import com.example.eciot.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mMainBinding;

    EditText usernameEdit,passwordEdit;
    private Cursor fila;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        usernameEdit = mMainBinding.txtUsuario;
        passwordEdit = mMainBinding.txtPassword;

    }
    public void iniciarSesion(View v){

        ApiService api = RetrofitClient.createApiService();
        api.getToken(usernameEdit.getText().toString(),passwordEdit.getText().toString()).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()){
                    User user = new User(usernameEdit.getText().toString(),passwordEdit.getText().
                            toString());
                    /*
                     Si es necesario guardar usuarios
                     user.save();
                     */

                    Token token = response.body();
                    token.save();

                    Intent ingreso= new Intent(getApplicationContext(), DrawerMenuActivity.class);
                    startActivity(ingreso);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }



}
