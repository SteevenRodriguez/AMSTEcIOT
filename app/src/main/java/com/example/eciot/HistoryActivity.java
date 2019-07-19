package com.example.eciot;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.eciot.adapters.ObjectAdapter;
import com.example.eciot.databinding.ActivityHistoryBinding;
import com.example.eciot.models.ObjectModel;

import java.util.LinkedList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding mActivityHistoryBinding;
    private List<ObjectModel> objectModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHistoryBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_history);
        getData();
        //mActivityHistoryBinding.progressBar.setProgress(90f);
        //mActivityHistoryBinding.progressBar.setForegroundStrokeWidth(5);


        mActivityHistoryBinding.recyclerList.setLayoutManager(new LinearLayoutManager(this));
        mActivityHistoryBinding.recyclerList.setHasFixedSize(true);
        ObjectAdapter adapter = new ObjectAdapter(this,objectModels);
        mActivityHistoryBinding.recyclerList.setAdapter(adapter);
    }

    public void getData(){
        int fallos = 0;
        float progress =0;
        objectModels = new LinkedList<>();
        for (int i=0;i<12;i++){
            if (i%2==0){
                ObjectModel a = new ObjectModel("lapiz",Float.valueOf(32),true);
                objectModels.add(a);


            } else {
                ObjectModel a = new ObjectModel("cuaderno",Float.valueOf(12),false);
                objectModels.add(a);
                fallos++;
            }

        }

        progress = (float) (fallos *1.0  /objectModels.size() *100);


        mActivityHistoryBinding.progressBar.setProgress(progress);
        mActivityHistoryBinding.percent.setText(progress+"%");



    }
}
