package com.example.dogwalker;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ApplicationWalkerListAdapter extends RecyclerView.Adapter<ApplicationWalkerListAdapter.MyViewHolder> {

    Context context;
    private List<ApplicationWalkerProfile> walkerProfileList;

    public ApplicationWalkerListAdapter(Context context, List<ApplicationWalkerProfile> walkerProfileList) {
        this.context = context;
        this.walkerProfileList = walkerProfileList;

    }

    String uid;



    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    private OwnerListAdapter.onItemClickListener onItemClickListener;



    @NonNull
    @Override
    public ApplicationWalkerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.walkerlist_profile, parent, false);
        ApplicationWalkerListAdapter.MyViewHolder myViewHolder = new ApplicationWalkerListAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ApplicationWalkerProfile walker = walkerProfileList.get(position);
        if (walker != null) {
            holder.walkerName.setText(walker.getWalkerName());
            holder.walkerTel.setText(walker.getWalkerTel());
            holder.walkerAddr.setText(walker.getWalkerAddr());
            holder.walkerNurture.setText(walker.getWalkerNurture());
            holder.walkerCareer.setText(walker.getWalkerCareer());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = view.inflate(view.getContext(), R.layout.walker_list_add, null);
                Intent intent = new Intent(view.getContext(), WalkerDetail2.class);
                intent.putExtra("walkerUUID",walker.getWalkerUUID());
                intent.putExtra("walkerUid",walker.getUid());
                intent.putExtra("onwerUid",walker.getOwnerUID());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return walkerProfileList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView walkerName, walkerTel, walkerAddr, walkerNurture, walkerCareer;
        ImageView imgProfile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //  readFirebaseValue();
            walkerName = itemView.findViewById(R.id.walkerName);
            walkerTel = itemView.findViewById(R.id.walkerTel);
            walkerAddr = itemView.findViewById(R.id.walkerAddr);
            walkerNurture = itemView.findViewById(R.id.walkerNurture);
            walkerCareer = itemView.findViewById(R.id.walkerCareer);
            imgProfile = itemView.findViewById(R.id.imgProfile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                }
            });

        }
    }
}