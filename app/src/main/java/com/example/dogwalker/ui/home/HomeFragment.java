package com.example.dogwalker.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalker.Owner;
import com.example.dogwalker.OwnerListAdapter;
import com.example.dogwalker.OwnerProfile;
import com.example.dogwalker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {
    List<OwnerProfile> ownerList;
    private OwnerListAdapter ownerListAdapter;
    EditText dog_name, dog_age, dog_breed, dog_walk, dog_addr ,onwerTel;
    String uid,dogUUID;

    DatabaseReference mDatabase;

    public HomeFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ownerList = new ArrayList<OwnerProfile>();
        //    readUser();
        readFirebaseValue();
        ownerListAdapter = new OwnerListAdapter(getContext(),ownerList);

        //ownerListAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext(),
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ownerListAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        dogUUID = UUID.randomUUID().toString();
       /* if (ownerList.get(1)!=null) {
            Toast.makeText(getContext(), ownerList.get(1) + "", Toast.LENGTH_SHORT).show();

        }*/


        return view;
    }

    private void addContact() {
        View dialogView = LayoutInflater.from(getContext()).
                inflate(R.layout.owner_list_add, null);
        dog_name = dialogView.findViewById(R.id.dog_name);
        dog_age = dialogView.findViewById(R.id.dog_age);
        dog_breed = dialogView.findViewById(R.id.dog_breed);
        dog_walk = dialogView.findViewById(R.id.dog_walk);
        dog_addr = dialogView.findViewById(R.id.dog_addr);
        onwerTel = dialogView.findViewById(R.id.txtOwnerTel);

        mDatabase =FirebaseDatabase.getInstance().getReference("list");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("반려인 프로필 등록");
        builder.setView(dialogView);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OwnerProfile owner = new OwnerProfile();
                owner.setDogName(dog_name.getText().toString());
                owner.setDogAge(dog_age.getText().toString());
                owner.setBread(dog_breed.getText().toString());
                owner.setWalkTime(dog_walk.getText().toString());
                owner.setAddr(dog_addr.getText().toString());
                owner.setOwnerTel(onwerTel.getText().toString());
                owner.setUid(uid);
                owner.setOwnerUUID(dogUUID);
                Log.d("owner name : ", owner.getDogName().toString() );
                Toast.makeText(getContext(),"addItem 진입",Toast.LENGTH_SHORT).show();

                // mDatabase.child("list").child("owner").setValue(owner);
                ownerListAdapter.addItem(owner);

            }
        });
        builder.setNegativeButton("닫기", null);
        builder.show();
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
                        if (value.isReservation()==false) {
                            ownerList.add(value);
                        }
                    }
                    ownerListAdapter.notifyDataSetChanged();

                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }
}
