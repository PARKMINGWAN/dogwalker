package com.example.dogwalker;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WalkerAdapter
        {
    DatabaseReference mDatabase;
    private List<Walker> walkerList;
    public WalkerAdapter(List<Walker> walkerList){
        this.walkerList = walkerList;
    }


   /* public void findAll() {

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Query myTopPostsQuery = mDatabase.equalTo(uid);
        Log.d("UID",uid);
        Log.d("Query",myTopPostsQuery.toString());


    }*/

   public void addItem(Walker walker){
       mDatabase = FirebaseDatabase.getInstance().getReference("users");

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       String uid = user.getUid();
       Log.d("getUid",uid);
       mDatabase.child(uid).child("userId").setValue(walker.getId().toString());
       mDatabase.child(uid).child("Name").setValue(walker.getName().toString());
       mDatabase.child(uid).child("PassWord").setValue(walker.getPwd().toString());
       mDatabase.child(uid).child("Tel").setValue(walker.getTel().toString());
       mDatabase.child(uid).child("Address").setValue(walker.getAddr().toString());
       mDatabase.child(uid).child("Career").setValue(walker.getCareer().toString());
       mDatabase.child(uid).child("Nurture").setValue(walker.getNurture().toString());

/*
       mDatabase.child(userId).setValue(walker.getName().toString());
       mDatabase.child(userId).setValue(walker.getAddr().toString());
       mDatabase.child(userId).setValue(walker.getTel().toString());
       mDatabase.child(userId).setValue(walker.getCareer().toString());
       mDatabase.child(userId).setValue(walker.getNurture().toString());*/
       walkerList.add(walker);


   }








}
