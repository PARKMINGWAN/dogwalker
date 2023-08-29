package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class WalkerDetail extends AppCompatActivity {
    private ProgressBar progressBar;
    String dogUUID,walkerUid,walkerUUID,ownerUid;
    Button btnApplication,btnCancel;
    DatabaseReference mDatabase;
    ApplicationWalkerProfile applicationWalkerProfile;
    TextView txtWalkerTel, txtWalkerAddr, txtWalkerName,txtWalkerCareer ,txtWalkerNurtuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_walke);
        Intent intent = getIntent();
        walkerUUID = intent.getStringExtra("walkerUUID");
        ownerUid = intent.getStringExtra("onwerUid");
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        txtWalkerAddr =findViewById(R.id.txtAddr);
        txtWalkerName =findViewById(R.id.txtName);
        txtWalkerCareer =findViewById(R.id.txtCareer);
        txtWalkerNurtuer=findViewById(R.id.txtNurture);
        txtWalkerTel=findViewById(R.id.txtTel);

        btnApplication =findViewById(R.id.btnApproval);
        btnCancel=findViewById(R.id.btnCancle);



    }

    public interface FirebaseCallback {
        void onResponse(ApplicationWalkerProfile value);
    }

    public void readFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child("ApplicationList").child("owner").child(ownerUid).child(walkerUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    ApplicationWalkerProfile value = task.getResult().getValue(ApplicationWalkerProfile.class);
                    Log.d("워커 디테일 : ",value.getUid()+"");
                    callback.onResponse(value);
                } else {

                }
            }
        });
    }
}