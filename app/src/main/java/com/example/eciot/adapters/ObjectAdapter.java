package com.example.eciot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eciot.R;
import com.example.eciot.databinding.ItemObjectBinding;
import com.example.eciot.models.ObjectModel;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ChildViewHolder> {


    private Context mContext;
    private final LayoutInflater mInflater;
    private List<ObjectModel> mObjectss;

    public ObjectAdapter(Context context, List<ObjectModel> mObjects){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mObjectss = mObjects;

    }

    @NonNull
    @Override
    public ObjectAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemObjectBinding binding = ItemObjectBinding.inflate(mInflater, parent, false);
        return new ObjectAdapter.ChildViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectAdapter.ChildViewHolder holder, int position) {
        final ObjectModel objectModel = mObjectss.get(position);
        holder.bind(objectModel);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return  mObjectss.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        private ItemObjectBinding mBinding;

        ChildViewHolder(ItemObjectBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(ObjectModel objectModel) {
            mBinding.setObject(objectModel);

            if (objectModel.getType().equals("lapiz")){
                mBinding.image.setImageResource(R.drawable.lapiz);
            } else {
                mBinding.image.setImageResource(R.drawable.cuaderno);
            }

            if (objectModel.getStatus()){
                mBinding.state.setText("Acertado");
                mBinding.state.setTextColor(Color.GREEN);
            }

        }
    }
}
