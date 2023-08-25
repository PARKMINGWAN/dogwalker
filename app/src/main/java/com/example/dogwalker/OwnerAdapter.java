package com.example.dogwalker;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OwnerAdapter {
    DatabaseReference mDatabase;
    private List<Owner> ownerList;

    public OwnerAdapter(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    public void addItem(Owner owner) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.d("getUid()", uid);

        mDatabase.child(uid).child("userId").setValue(owner.getId().toString());
        mDatabase.child(uid).child("PassWord").setValue(owner.getPwd().toString());
        mDatabase.child(uid).child("Tel").setValue(owner.getTel().toString());
        mDatabase.child(uid).child("Address").setValue(owner.getAddr().toString());
        mDatabase.child(uid).child("Name").setValue(owner.getName().toString());
        mDatabase.child(uid).child("Breed").setValue(owner.getBreed().toString());
        mDatabase.child(uid).child("Dog_age").setValue(owner.getDog_age().toString());
        mDatabase.child(uid).child("Dog_walk").setValue(owner.getDog_walk().toString());

        ownerList.add(owner);
    }
}
