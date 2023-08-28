package com.example.dogwalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.dogwalker.ui.mypage.OwnerMyPageFragment;
import com.example.dogwalker.ui.mypage.WalkerMyPageFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class WalkerMypageAdd extends AppCompatActivity {
    private ProgressBar progressBar;
    Button btnInfoInsert, btnImgInsert;
    EditText etName,etId,etPwd,etAddr,etCareer,etNurture,etTel;
    DatabaseReference mDatabase;
    double longitudes;
    double latitudes;
    private OwnerListAdapter ownerListAdapter;
    WalkerProfile walkerProfile;
    String dogUUID,uid;

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walker_mypage_add);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnInfoInsert = findViewById(R.id.btnInfoInsert);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        etAddr = findViewById(R.id.etAddr);
        etPwd = findViewById(R.id.etPwd);
        etCareer = findViewById(R.id.etCareer);
        etNurture = findViewById(R.id.etNurture);
        etTel = findViewById(R.id.etTel);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();

        btnInfoInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {








                Intent intent = new Intent(getApplicationContext(), WalkerMyPageFragment.class);

                startActivity(intent);


            }
        });


    }






}