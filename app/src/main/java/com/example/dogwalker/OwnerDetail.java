package com.example.dogwalker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogwalker.ui.home.HomeFragment;
import com.example.dogwalker.ui.mypage.WalkerMyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OwnerDetail extends AppCompatActivity {
    private ProgressBar progressBar;
    String dogUUID;
    TextView txtDogName, txtDogAge,txtDogWalk,txtOwnerTel,txtOwnerAddr,txtDogBread;
    DatabaseReference mDatabase;
    Button btnApplication;
    WalkerProfile walkerProfile;
    ApplicationWalkerProfile applicationWalkerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_owner);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        dogUUID = intent.getStringExtra("dogUUID");
        txtDogName =findViewById(R.id.txtName);
        txtDogBread=findViewById(R.id.txtBreed);
        txtDogAge=findViewById(R.id.txtDogAge);
        txtOwnerAddr=findViewById(R.id.txtAddr);
        txtOwnerTel=findViewById(R.id.txtTel);
        btnApplication = findViewById(R.id.btnApply);
        applicationWalkerProfile = new ApplicationWalkerProfile();
        Log.d("uuid :", dogUUID + "");
        readFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(OwnerProfile value) {
                if (value!=null)
                {
                    txtDogName.setText(value.getDogName());
                    txtDogBread.setText(value.getBread());
                    txtDogAge.setText(value.getDogAge());
                    txtOwnerAddr.setText(value.getAddr());
                    txtOwnerTel.setText(value.getOwnerTel());


                }
            }
        });

        btnApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HomeFragment.class);
                startActivity(intent);
            }
        });

    }
    public interface FirebaseCallback2 {
        void onResponse(WalkerProfile value);
    }

    public void reaWalkerFirebaseValue(FirebaseCallback2 callback) {

        DatabaseReference uidRef = mDatabase.child("list").child("walker").child(dogUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    WalkerProfile value = task.getResult().getValue(WalkerProfile.class);
                    callback.onResponse(value);
                } else {

                }
            }
        });
    }

    public interface FirebaseCallback {
        void onResponse(OwnerProfile value);
    }

    public void readFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child("list").child("owner").child(dogUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    OwnerProfile value = task.getResult().getValue(OwnerProfile.class);
                    callback.onResponse(value);
                } else {

                }
            }
        });
    }
}