package com.example.dogwalker;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.dogwalker.ui.home.HomeFragment;
import com.example.dogwalker.ui.mypage.WalkerMyPageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class OwnerDetail extends AppCompatActivity implements OnMapReadyCallback {
    private ProgressBar progressBar;
    String dogUUID,walkerUid,walkerUUID,ownerUid;
    TextView txtDogName, txtDogAge,txtDogWalk,txtOwnerTel,txtOwnerAddr,txtDogBread;
    DatabaseReference mDatabase;
    Button btnApplication;
    WalkerProfile walkerProfile;
    ApplicationWalkerProfile applicationWalkerProfile;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    private FusedLocationSource locationSource;
    NaverMap naverMap;

    List<Double> longitudesPath;
    List<Double> latitudesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_owner);

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
        walkerUUID = UUID.randomUUID().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        walkerUid = user.getUid();
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
                    ownerUid=value.getUid();

                }
            }
        });

        reaWalkerFirebaseValue(new FirebaseCallback2() {
            @Override
            public void onResponse(WalkerProfile value) {

                applicationWalkerProfile.setUid(value.getUid());
                applicationWalkerProfile.setWalkerAddr(value.getWalkerAddr());
                applicationWalkerProfile.setWalkerCareer(value.getWalkerCareer());
                applicationWalkerProfile.setWalkerTel(value.getWalkerTel());
                applicationWalkerProfile.setWalkerName(value.getWalkerName());
                applicationWalkerProfile.setWalkerNurture(value.getWalkerNurture());
                applicationWalkerProfile.setWalkerUUID(walkerUUID);

            }
        });

        btnApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HomeFragment.class);

                Log.d("디테일 UUID",walkerUUID+"" );
                Log.d("디테일 오너 UID",ownerUid+"" );

                Log.d("디테일 신청 프로필 경력 : ",applicationWalkerProfile.getWalkerCareer());
                mDatabase.child("ApplicationList").child(ownerUid).child(walkerUUID).setValue(applicationWalkerProfile);
                //  mDatabase.child("ApplicationList").child(walkerUid).child(dogUUID).setValue(applicationWalkerProfile);
                //오너 프로필로 나중에 하나 저장해서 새로운 인텐트
                startActivity(intent);
            }
        });

        longitudesPath = new ArrayList<Double>();
        latitudesPath = new ArrayList<Double>();

        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1561411, 129.0594806), 12));

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        pathFormFirebase();

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        pathFormFirebase();
    }

    private void pathFormFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pathRef = database.getReference("paths").child(dogUUID);

        pathRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PathLocation pathLocation = snapshot.getValue(PathLocation.class);
                if (pathLocation != null) {
                    latitudesPath = pathLocation.getLatitude();
                    longitudesPath = pathLocation.getLongitude();
                    drawPathOnMap(latitudesPath, longitudesPath);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void drawPathOnMap(List<Double> latitudesPath, List<Double> longitudesPath) {
        if (latitudesPath.size() >= 2 && latitudesPath.size() == longitudesPath.size()) {
            PathOverlay path = new PathOverlay();
            List<LatLng> latLngs = new ArrayList<>();

            for (int i = 0; i<latitudesPath.size(); i++) {
                latLngs.add(new LatLng(latitudesPath.get(i),longitudesPath.get(i)));
            }

            path.setCoords(latLngs);
            path.setColor(Color.BLUE);
            path.setMap(naverMap);
        } else {
            Log.e("DrawPath", "경로 좌표 부족");
        }
    }

    public interface FirebaseCallback2 {
        void onResponse(WalkerProfile value);
    }

    public void reaWalkerFirebaseValue(FirebaseCallback2 callback) {

        DatabaseReference uidRef = mDatabase.child("list").child("walker").child(walkerUid);
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