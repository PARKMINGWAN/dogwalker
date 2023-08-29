package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyDogList extends AppCompatActivity {
    RecyclerView recyclerView;
    List<OwnerProfile> ownerList;
    MyDogAdapter myDogAdapter;
    String uid,dogUUID;

    DatabaseReference mDatabase;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dog_list);
        Button btnBack = findViewById(R.id.btnBack);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);

        ownerList = new ArrayList<OwnerProfile>();
        readFirebaseValue();

        myDogAdapter = new MyDogAdapter(getApplicationContext(),ownerList);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(),
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myDogAdapter);
        Log.d("리사이클러뷰 : ", "2222222");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
                        Log.d("오너 키값 확인2222", Key + " ");
                        OwnerProfile value = snapshot.getValue(OwnerProfile.class);
                        Log.d("오너 값 확인2222", value.getDogName() + " ");
                        if (value.getIsReservation().equals("0")) {
                            ownerList.add(value);
                        }
                    }
                    myDogAdapter.notifyDataSetChanged();

                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }
}