package com.example.dogwalker.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dogwalker.MapPoint;
import com.example.dogwalker.Owner;
import com.example.dogwalker.OwnerProfile;
import com.example.dogwalker.R;
import com.example.dogwalker.databinding.FragmentMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private  List<OwnerProfile> ownerList;
    private DatabaseReference mDatabase;

   private  Button btnList;

    private InfoWindow currentlyOpenInfoWindow;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    private FusedLocationSource locationSource;
    NaverMap naverMap;

    PathOverlay path = new PathOverlay();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1561411, 129.0594806), 12));

        FragmentManager fm = getParentFragmentManager();
        com.naver.maps.map.MapFragment mapFragment = (com.naver.maps.map.MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync((OnMapReadyCallback) this);
        ownerList = new ArrayList<>();
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readFirebaseValue();


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);

        View view= inflater.inflate(R.layout.fragment_map, container, false);
        btnList = view.findViewById(R.id.testButton);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i =0 ; i < ownerList.size() ; i++) {
                            CreateMarker(i);
                        }
                    }
                }, 1000);

            }
        });
                return view;
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        InfoWindow infoWindow = new InfoWindow();



        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);


          /*  Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i =0 ; i < ownerList.size() ; i++) {
                        CreateMarker(i);
                    }
                }
            }, 1000);*/



    }

    private void readFirebaseValue() {
        DatabaseReference listRef = mDatabase.child("list").child("owner");
        listRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ownerList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String Key = snapshot.getKey();


                        Log.d("탭2 키값 확인", Key + " ");
                        OwnerProfile value = snapshot.getValue(OwnerProfile.class);
                        Log.d("탭2 값 확인", value.getDogName() + " ");
                        ownerList.add(value);
                    }


                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }
    private void closeCurrentlyOpenInfoWindow() {
        if (currentlyOpenInfoWindow != null) {
            currentlyOpenInfoWindow.close();
            currentlyOpenInfoWindow = null;
        }
    }
    private void showInfoWindowForMarker(Marker marker, int position) {
        closeCurrentlyOpenInfoWindow();

        ViewGroup viewGroup = getView().findViewById(R.id.fragment_map_container);
        MapPoint mapPoint = new MapPoint(getContext().getApplicationContext(), viewGroup,ownerList.get(position));
     //   mapPoint.updateMarkerOwner(ownerList.get(position));

        //ui
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(mapPoint);
        infoWindow.setZIndex(10);
        infoWindow.setAlpha(0.9f);
        infoWindow.open(marker);

        currentlyOpenInfoWindow = infoWindow;
    }
    private  void CreateMarker(int position)
    {
        Log.d("마커 생성 ","");
        Marker marker = new Marker();

        marker.setPosition(new LatLng(ownerList.get(position).getLatitude(),ownerList.get(position).getLongitude()));

        marker.setMap(naverMap);

            marker.setOnClickListener(overlay -> {
                showInfoWindowForMarker(marker,position);
                return true;
            });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}