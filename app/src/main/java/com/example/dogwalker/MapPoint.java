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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.map.overlay.InfoWindow;

public class MapPoint extends InfoWindow.DefaultViewAdapter {

    private final Context context;
    private final ViewGroup viewGroup;
    private DatabaseReference mDatabase;

    String uid;

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

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        txtProfile.setText("반려인 프로필");
        imagePoint.setImageResource(R.drawable.dog);

        readFirebaseValue(new FirebaseCallback() {

            @Override
            public void onResponse(Owner value) {

              Log.d("강아지 이름 : " ,value.getName());
              String dogname = value.getName();
                txtName.setText("강아지 이름 : " + dogname);
                txtDogAge.setText("강아지 나이 : " + value.getDog_age());
                txtBreed.setText("강아지 품종 : " + value.getBreed());
                txtDogWalk.setText("산책 시간 : " + value.getDog_walk());
            }
        });

        return view;
    }

    public interface FirebaseCallback {
        void onResponse(Owner value);
    }

    private void readFirebaseValue(FirebaseCallback callback) {
        DatabaseReference uidRef = mDatabase.child(uid).child("owner");
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Owner value = task.getResult().getValue(Owner.class);
                    callback.onResponse(value);
                }
            }
        });
    }
}