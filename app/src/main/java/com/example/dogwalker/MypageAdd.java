package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.dogwalker.ui.mypage.OwnerMyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

public class MypageAdd extends AppCompatActivity {
    List<OwnerProfile> ownerList;
    private ProgressBar progressBar;
    Button btnInfoInsert, btnImgInsert;
    EditText etName,etTel,etAddr,etBreed,etDogAge,etDogWalk;
    DatabaseReference mDatabase;
     Context context;//intent 사용하기 위해서 추가ㄴ
    double longitudes;
    double latitudes;
    private OwnerListAdapter ownerListAdapter;
    OwnerProfile ownerProfile;
    String dogUUID,uid;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_add);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnInfoInsert = findViewById(R.id.btnInfoInsert);

        etName = findViewById(R.id.etName);
        etTel = findViewById(R.id.etTel);
        etAddr = findViewById(R.id.etAddr);
        etBreed = findViewById(R.id.etBreed);
        etDogAge = findViewById(R.id.etDogAge);
        etDogWalk = findViewById(R.id.etDogWalk);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();

        btnInfoInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ownerProfile =new OwnerProfile();

                ownerProfile.setDogName(etName.getText().toString());
                ownerProfile.setOwnerTel(etTel.getText().toString());
                ownerProfile.setAddr(etAddr.getText().toString());
                ownerProfile.setBread(etBreed.getText().toString());
                ownerProfile.setDogAge(etDogAge.getText().toString());
                ownerProfile.setWalkTime(etDogWalk.getText().toString());
                ownerProfile.setOwnerUUID(dogUUID);
                ownerProfile.setUid(uid);

                addItem(ownerProfile);





                Intent intent = new Intent(getApplicationContext(), OwnerMyPageFragment.class);

                startActivity(intent);


            }
        });


    }

    public void addItem(OwnerProfile ownerProfile) {
        //Toast.makeText(,Toast.LENGTH_SHORT).show();
        mDatabase =FirebaseDatabase.getInstance().getReference("list");
        Thread datathread = new Thread(){
            @Override
            public void run(){
                requestGeocode(ownerProfile.getAddr());
            }
        };
        datathread.start();
        try {
            datathread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ownerProfile.setLatitude(latitudes);
        ownerProfile.setLongitude(longitudes);
        Log.d("지오코드 체크3",longitudes+" ");
        Log.d("프로필 UUID",ownerProfile.getOwnerUUID()+" ");

        mDatabase.child("owner").child(ownerProfile.getOwnerUUID()).setValue(ownerProfile);

    }
    private void requestGeocode(String addr) {
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(addr, "UTF-8");
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