package com.example.dogwalker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyDogAdapter
        extends RecyclerView.Adapter<MyDogAdapter.ViewHolder>{
    List<OwnerProfile> ownerList;
    Context context;

    public MyDogAdapter(Context context, List<OwnerProfile> ownerList) {
        this.context=context;
        this.ownerList = ownerList;
    }

    @NonNull
    @Override
    public MyDogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_my_dog_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDogAdapter.ViewHolder holder, int position) {
        OwnerProfile ownerProfile =ownerList.get(position);
        if (ownerProfile != null) {
            holder.txtBreed.setText(ownerProfile.getBread());
            holder.txtDogAge.setText(ownerProfile.getDogAge());
            holder.txtDogWalk.setText(ownerProfile.getWalkTime());

        }

    }

    @Override
    public int getItemCount() {
        return ownerList == null ? 0 : ownerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBreed,txtDogAge,txtDogWalk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBreed = itemView.findViewById(R.id.txtBreed);
            txtDogAge = itemView.findViewById(R.id.txtDogAge);
            txtDogWalk = itemView.findViewById(R.id.txtDogWalk);
            Log.d("리사이클러뷰66 : ", txtDogAge.getText().toString()+"");
        }
    }


}
