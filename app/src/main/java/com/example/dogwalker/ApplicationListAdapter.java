package com.example.dogwalker;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalker.OwnerDetail;
import com.example.dogwalker.OwnerListAdapter;
import com.example.dogwalker.OwnerProfile;
import com.example.dogwalker.R;
import com.example.dogwalker.WalkerProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.MyViewHolder> {

    Context context;
    private List<ApplicationWalkerProfile> walkerProfileList;

    public ApplicationListAdapter(Context context, List<ApplicationWalkerProfile> walkerProfileList) {
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
    public ApplicationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.walkerlist_profile, parent, false);
        ApplicationListAdapter.MyViewHolder myViewHolder = new ApplicationListAdapter.MyViewHolder(view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
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
                EditText walker_name = dialogView.findViewById(R.id.walker_name);
                EditText walker_tel = dialogView.findViewById(R.id.walker_tel);
                EditText walker_addr = dialogView.findViewById(R.id.walker_addr);
                EditText walker_career = dialogView.findViewById(R.id.walker_career);
                TextView walker_nurture = dialogView.findViewById(R.id.walker_nurture);
                walker_name.setText(walker.getWalkerName());
                walker_tel.setText(walker.getWalkerTel());
                walker_addr.setText(walker.getWalkerAddr());
                walker_career.setText(walker.getWalkerCareer());
                walker_nurture.setText(walker.getWalkerNurture());

            }
        });
    }

    @Override
    public int getItemCount() {
        return walkerProfileList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView walkerName, walkerTel, walkerAddr, walkerNurture, walkerCareer;
        ImageView imgWalker;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //  readFirebaseValue();
            walkerName = itemView.findViewById(R.id.walkerName);
            walkerTel = itemView.findViewById(R.id.walkerTel);
            walkerAddr = itemView.findViewById(R.id.walkerAddr);
            walkerNurture = itemView.findViewById(R.id.walkerNurture);
            walkerCareer = itemView.findViewById(R.id.walkerCareer);
            imgWalker = itemView.findViewById(R.id.imgWalker);

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