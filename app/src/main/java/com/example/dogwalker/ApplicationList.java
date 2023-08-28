package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ApplicationList extends Fragment {
    List<ApplicationWalkerProfile> applicationWalkerProfileList;
    private  ApplicationListAdapter applicationListAdapter;

    DatabaseReference mDatabase;

    public ApplicationList()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.application_list, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

return view;
    }

    public void readFirebaseValue() {
        DatabaseReference listRef = mDatabase.child("list").child("owner");
        listRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    applicationWalkerProfileList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String Key = snapshot.getKey();
                        Log.d("오너 키값 확인", Key + " ");
                        ApplicationWalkerProfile value = snapshot.getValue(ApplicationWalkerProfile.class);
                       // Log.d("오너 값 확인", value.getDogName() + " ");
                        if (value.isReservation()==false) {
                            applicationWalkerProfileList.add(value);
                        }
                    }
                    applicationListAdapter.notifyDataSetChanged();

                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }




}