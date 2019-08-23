package com.fiec.eciot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import com.fiec.eciot.fragments.TrainingFragment;
import com.fiec.eciot.fragments.ClassifyFragment;
import com.fiec.eciot.fragments.HistoryFragment;
import com.fiec.eciot.R;
import com.fiec.eciot.models.Category;
import com.fiec.eciot.models.Token;
import com.fiec.eciot.services.ApiService;
import com.fiec.eciot.services.RetrofitClient;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        handle(R.id.nav_home);
        setTitle("Clasificar Objeto");
        downloadCategories();

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
        handle(item.getItemId());
        setTitle(item.getTitle());
        return true;
    }

    private void handle(int item){
        Fragment fragment = null;
        // Handle navigation view item clicks here.


        if (item == R.id.nav_home) {
            fragment = new ClassifyFragment();
//            Intent intent = new Intent(getBaseContext(), DrawerMenuActivity.class);
//            startActivity(intent);

        } else if (item == R.id.nav_training) {
            fragment = new TrainingFragment();

        }else if (item == R.id.nav_history) {
            fragment = new HistoryFragment();

        }
        else if (item == R.id.nav_share) {
            logOut();

        }

        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    private void downloadCategories(){

        final Realm realm  = Realm.getDefaultInstance();
        try {
            Token token = realm.where(Token.class).findAll().last();
            ApiService api = RetrofitClient.createApiService();
            api.getCategories("JWT "+token.getToken()).enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.isSuccessful()){
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(response.body());
                        realm.commitTransaction();
                        realm.close();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                }
            });
        } catch (Exception e ){
                e.getMessage();
        }



    }


    public void logOut(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DrawerMenuActivity.this, SweetAlertDialog.NORMAL_TYPE)
                .setContentText(("Desea cerrar sesión?"))
                .setConfirmText("Sí")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Realm realm = Realm.getDefaultInstance();
                        try{
                            realm.beginTransaction();
                            realm.where(Token.class).findAll().deleteAllFromRealm();
                            realm.commitTransaction();
                            startActivity(new Intent(DrawerMenuActivity.this, MainActivity.class));
                            finish();

                        } catch (Exception e) {

                        } finally {
                            realm.close();
                        }
                    }
                });
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();


    }

}
