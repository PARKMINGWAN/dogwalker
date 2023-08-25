package com.example.dogwalker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.internal.Sleeper;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapPoint extends InfoWindow.DefaultViewAdapter {

    private final Context context;
    private final ViewGroup viewGroup;

    private  OwnerProfile ownerProfile;

    TextView txtProfile;
    ImageView imagePoint;
    TextView txtName,txtDogAge,txtBreed ,txtDogWalk;


    public MapPoint(@NonNull Context context, ViewGroup viewGroup, OwnerProfile ownerProfile) {
        super(context);
        this.context = context;
        this.viewGroup = viewGroup;
        if (ownerProfile!=null) {
            this.ownerProfile = new OwnerProfile();
            this.ownerProfile.setDogName(ownerProfile.getDogName());
            this.ownerProfile.setDogAge(ownerProfile.getDogAge());
            this.ownerProfile.setBread(ownerProfile.getBread());
            this.ownerProfile.setWalkTime(ownerProfile.getWalkTime());
        }
    }



    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow) {


        View view = LayoutInflater.from(context).inflate(R.layout.activity_map_point, viewGroup, false);
     txtProfile = (TextView) view.findViewById(R.id.txtProfile);
        imagePoint = (ImageView) view.findViewById(R.id.imagePoint);
      txtName = (TextView) view.findViewById(R.id.txtName);
       txtDogAge = (TextView) view.findViewById(R.id.txtDogAge);
       txtBreed = (TextView) view.findViewById(R.id.txtBreed);
     txtDogWalk = (TextView) view.findViewById(R.id.txtDogWalk);

     txtName.setText(ownerProfile.getDogName());
        txtDogAge.setText(ownerProfile.getDogAge());
        txtBreed.setText(ownerProfile.getBread());
        txtDogWalk.setText(ownerProfile.getWalkTime());

        txtProfile.setText("반려인 프로필");



        imagePoint.setImageResource(R.drawable.dog);


        return view;
    }
    public void updateMarkerOwner(OwnerProfile ownerProfile) {

        if (ownerProfile!=null) {


            this.ownerProfile = new OwnerProfile();

            this.ownerProfile.setDogName(ownerProfile.getDogName());
            this.ownerProfile.setDogAge(ownerProfile.getDogAge());
            this.ownerProfile.setBread(ownerProfile.getBread());
            this.ownerProfile.setWalkTime(ownerProfile.getWalkTime());






        }

    }



}