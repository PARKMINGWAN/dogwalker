package com.example.dogwalker;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WalkerListAdapter extends RecyclerView.Adapter<WalkerListAdapter.MyViewHolder> {
    DatabaseReference mDatabase;
    String uid;
    private List<WalkerProfile> walkerList;
    ImageView imgProfile;
    Button btnInfoInsert, btnImgInsert;
    Uri imgUrl;
    private ProgressBar progressBar;

    public WalkerListAdapter(List<WalkerProfile> walkerList, Context context) {
        this.walkerList = walkerList;
        this.context = context;
    }

    Context context;


    FirebaseManager firebaseManager = new FirebaseManager();

    public WalkerListAdapter(List<WalkerProfile> walkerList) {
        this.walkerList = walkerList;
    }

    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    private OwnerListAdapter.onItemClickListener onItemClickListener;

    public void addItem(WalkerProfile walkerProfile) {
        //Toast.makeText(,Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference("list");
        walkerList.add(walkerProfile);

        mDatabase.child("walker").child(walkerProfile.getUid()).setValue(walkerProfile);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.walkerlist_profile, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        imgProfile = view.findViewById(R.id.imgWalker);




    



        return myViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull WalkerListAdapter.MyViewHolder holder, int position) {
        WalkerProfile walker = walkerList.get(position);
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
        return walkerList.size();
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