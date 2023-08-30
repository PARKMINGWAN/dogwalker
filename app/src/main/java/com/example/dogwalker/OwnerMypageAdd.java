package com.example.dogwalker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dogwalker.ui.mypage.OwnerMyPageFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerMypageAdd extends AppCompatActivity implements OnMapReadyCallback {
    List<OwnerProfile> ownerList;
    private ProgressBar progressBar;
    Button btnInfoInsert, btnImgInsert;
    EditText etName,etTel,etAddr,etBreed,etDogAge,etDogWalk;
    DatabaseReference mDatabase;
    Context context;//intent 사용하기 위해서 추가ㄴ
    double longitudes;
    double latitudes;

    Uri imgUrl;

    List<Double> longitudesPath;
    List<Double> latitudesPath;
    private OwnerListAdapter ownerListAdapter;
    OwnerProfile ownerProfile;
    String dogUUID,uid;

    ImageView imgProfile;

    FirebaseManager firebaseManager;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    private FusedLocationSource locationSource;
    NaverMap naverMap;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_mypage_add);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnInfoInsert = findViewById(R.id.btnInfoInsert);
        btnImgInsert= findViewById(R.id.btnImgInsert);
        imgProfile = findViewById(R.id.imgProfile);

        firebaseManager =new FirebaseManager();

        etName = findViewById(R.id.etName);
        etTel = findViewById(R.id.etTel);
        etAddr = findViewById(R.id.etAddr);
        etBreed = findViewById(R.id.etBreed);
        etDogAge = findViewById(R.id.etDogAge);
        etDogWalk = findViewById(R.id.etDogWalk);

        longitudesPath = new ArrayList<Double>();
        latitudesPath = new ArrayList<Double>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();

        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1561411, 129.0594806), 12));

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imgProfile.setImageURI(uri);
                        imgUrl = uri;

                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });



        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        pathFormFirebase();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("오너에드 클릭 : ","클릭됨");
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());

            }
        });




        btnImgInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseManager.fireBaseOwnerImgProfileUpload(progressBar,dogUUID,imgUrl,OwnerMypageAdd.this);
            }
        });
        btnInfoInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveLatLngToFirebase(latitudesPath, longitudesPath);

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

                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        naverMap.setOnMapClickListener((point, coord) -> {
            double latitude = coord.latitude;
            double longitude = coord.longitude;

            longitudesPath.add(longitude);
            latitudesPath.add(latitude);

            drawPathOnMap(latitudesPath, longitudesPath);
        });
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

    private void saveLatLngToFirebase(List<Double> latitude, List<Double> longitude) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pathRef = database.getReference("paths");

        PathLocation pathLocation = new PathLocation(latitudesPath, longitudesPath);
        pathRef.child(dogUUID).setValue(pathLocation);
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