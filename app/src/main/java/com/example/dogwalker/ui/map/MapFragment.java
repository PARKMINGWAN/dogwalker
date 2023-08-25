package com.example.dogwalker.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dogwalker.MapPoint;
import com.example.dogwalker.R;
import com.example.dogwalker.databinding.FragmentMapBinding;
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

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        InfoWindow infoWindow = new InfoWindow();

        Marker marker = new Marker();
        marker.setPosition(new LatLng(35.1561411, 129.0594806));
        marker.setMap(naverMap);
        marker.setOnClickListener(overlay -> {
            ViewGroup viewGroup = (ViewGroup) getView().findViewById(R.id.fragment_map_container);
            MapPoint mapPoint = new MapPoint(getContext().getApplicationContext(), viewGroup);

            infoWindow.setAdapter(mapPoint);

            infoWindow.setZIndex(10);
            infoWindow.setAlpha(0.9f);
            infoWindow.open(marker);
            return false;
        });

        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        path.setCoords(Arrays.asList(
                new LatLng(35.1561411, 129.0594806),
                new LatLng(35.155509, 129.058220),
                new LatLng(35.154200, 129.057825)
        ));
        path.setMap(naverMap);

        List<LatLng> coords = new ArrayList<>();
        Collections.addAll(coords,
                new LatLng(35.1561411, 129.0594806),
                new LatLng(35.155509, 129.058220),
                new LatLng(35.154200, 129.057825));
        path.setCoords(coords);

        coords.set(0, new LatLng(35.154200, 129.057825));
        path.setCoords(coords);
        path.setWidth(30);
        path.setOutlineWidth(5);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}