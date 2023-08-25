package com.example.dogwalker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class WalkerListAdapter extends RecyclerView.Adapter<WalkerListAdapter.MyViewHolder> {
    DatabaseReference database;
    private List<Walker> walkerList;

    @NonNull
    @Override
    public WalkerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.walkerlist_profile, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalkerListAdapter.MyViewHolder holder, int position) {
        Walker walker = walkerList.get(position);
        holder.setItem(walker);

    }

    public WalkerListAdapter(List<Walker> walkerList) {
        this.walkerList = walkerList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return walkerList.size();
    }

    public void addItem(Walker walker) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void setItem(Walker walker) {


        }
    }
}
