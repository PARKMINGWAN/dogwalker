package com.example.dogwalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        etAddr = findViewById(R.id.etAddr);
        etCareer = findViewById(R.id.etCareer);
        etNurture = findViewById(R.id.etNurture);
        etTel = findViewById(R.id.etTel);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();


        btnInfoInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkerProfile = new WalkerProfile();
                walkerProfile.setWalkerName(etName.getText().toString());
                walkerProfile.setWalkerTel(etTel.getText().toString());
                walkerProfile.setWalkerNurture(etNurture.getText().toString());
                walkerProfile.setWalkerCareer(etCareer.getText().toString());
                walkerProfile.setWalkerAddr(etAddr.getText().toString());
                walkerProfile.setUid(uid);
                addItem(walkerProfile);

                Intent intent = new Intent(getApplicationContext(), WalkerMyPageFragment.class);
                startActivity(intent);

            }
        });


    }

    private void addItem(WalkerProfile walkerProfile) {
        mDatabase =FirebaseDatabase.getInstance().getReference("list");
        Thread datathread = new Thread(){
            @Override
            public void run(){
                requestGeocode(walkerProfile.getWalkerAddr());
            }
        };
        datathread.start();
        try {
            datathread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        walkerProfile.setLatitude(latitudes);
        walkerProfile.setLongitude(longitudes);
        mDatabase.child("walker").child(walkerProfile.getUid()).setValue(walkerProfile);
    }

    private void requestGeocode(String walkerAddr) {
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(walkerAddr, "UTF-8");
            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null) {
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "cxzobj9huy");
                connection.setRequestProperty("X-NCP-APIGW-API-KEY", "wQF4VmvAGfParPeLkzPmxW0xmJIV06qxB9nd7ENo");
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                int indexFirst;
                int indexLast;

                indexFirst = stringBuilder.indexOf("\"x\":\"");
                indexLast = stringBuilder.indexOf("\",\"y\":");
                String x = stringBuilder.substring(indexFirst + 5, indexLast);

                indexFirst = stringBuilder.indexOf("\"y\":\"");
                indexLast = stringBuilder.indexOf("\",\"distance\":");
                String y = stringBuilder.substring(indexFirst + 5, indexLast);

                Log.d("지오코드 체크",x+" ");

                longitudes= Double.parseDouble(x);
                latitudes = Double.parseDouble(y);
                Log.d("지오코드 체크2",longitudes+" ");
                bufferedReader.close();
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}