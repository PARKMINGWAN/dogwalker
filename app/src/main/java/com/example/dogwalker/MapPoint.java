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
import com.naver.maps.map.overlay.InfoWindow;

import java.util.ArrayList;
import java.util.List;

public class MapPoint extends InfoWindow.DefaultViewAdapter {

    private final Context context;
    private final ViewGroup viewGroup;
    private DatabaseReference mDatabase;
    List<OwnerProfile> ownerList;
    private OwnerListAdapter ownerListAdapter;

    public MapPoint(@NonNull Context context, ViewGroup viewGroup) {
        super(context);
        this.context = context;
        this.viewGroup = viewGroup;
    }


    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_map_point, viewGroup, false);
        TextView txtProfile = (TextView) view.findViewById(R.id.txtProfile);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.imagePoint);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtDogAge = (TextView) view.findViewById(R.id.txtDogAge);
        TextView txtBreed = (TextView) view.findViewById(R.id.txtBreed);
        TextView txtDogWalk = (TextView) view.findViewById(R.id.txtDogWalk);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtProfile.setText("반려인 프로필");
        imagePoint.setImageResource(R.drawable.dog);

        ownerList = new ArrayList<OwnerProfile>();

        readFirebaseValue();
        ownerListAdapter = new OwnerListAdapter(ownerList);

        return view;
    }

    public void readFirebaseValue() {
        DatabaseReference listRef = mDatabase.child("list").child("owner");
        listRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ownerList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String Key = snapshot.getKey();


                        Log.d("오너 키값 확인", Key + " ");
                        OwnerProfile value = snapshot.getValue(OwnerProfile.class);
                        Log.d("오너 값 확인", value.getDogName() + " ");
                        ownerList.add(value);
                    }
                    ownerListAdapter.notifyDataSetChanged();

                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }
}