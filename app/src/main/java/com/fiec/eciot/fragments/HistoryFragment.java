package com.fiec.eciot.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fiec.eciot.R;
import com.fiec.eciot.adapters.ObjectAdapter;
import com.fiec.eciot.databinding.FragmentHistoryBinding;
import com.fiec.eciot.models.ObjectModel;
import com.fiec.eciot.models.Token;
import com.fiec.eciot.services.ApiService;
import com.fiec.eciot.services.RetrofitClient;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding mFragmentBinding;
    private List<ObjectModel> objectModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_history,container,false);
        mFragmentBinding.recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentBinding.recyclerList.setHasFixedSize(true);

        getData();
        return mFragmentBinding.getRoot();
    }


    public void getData(){
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext());
        progressDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Descargando Datos");
        progressDialog.show();


        final Realm realm = Realm.getDefaultInstance();
        try {
            Token token = realm.where(Token.class).findFirst();
            ApiService api = RetrofitClient.createApiService();
            api.getObjects("JWT "+token.getToken()).enqueue(new Callback<List<ObjectModel>>() {
                @Override
                public void onResponse(Call<List<ObjectModel>> call, Response<List<ObjectModel>> response) {
                    if (response.isSuccessful()){
                        int fallas = 0;
                        ObjectAdapter adapter = new ObjectAdapter(getContext(),response.body());
                        mFragmentBinding.recyclerList.setAdapter(adapter);

                        for (ObjectModel obj : response.body()){
                            if (!obj.isAcerto()){
                                fallas++;
                            }
                        }
                        float progress = (fallas *1.00f/response.body().size()) *100;

                        mFragmentBinding.percent.setText(String.format("%.2f", progress)+"%");
                        mFragmentBinding.progressBar.setProgress(progress);
                        progressDialog.dismissWithAnimation();
                    } else {
                        progressDialog.dismissWithAnimation();
                        Toast toast = Toast.makeText(getContext(), "Error de descarga", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<List<ObjectModel>> call, Throwable t) {
                    progressDialog.dismissWithAnimation();
                    Toast toast = Toast.makeText(getContext(), "Error de descarga", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }



    }
}
