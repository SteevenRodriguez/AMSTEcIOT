package com.example.eciot.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eciot.R;
import com.example.eciot.databinding.FragmentHistoryBinding;
import com.example.eciot.models.ObjectModel;

import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding mFragmentBinding;
    private List<ObjectModel> objectModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_history,container,false);


        return mFragmentBinding.getRoot();
    }


    public void getData(){




    }
}
