package com.example.dogwalker.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dogwalker.OwnerListAdapter;
import com.example.dogwalker.OwnerProfile;
import com.example.dogwalker.R;
import com.example.dogwalker.WalkerListAdapter;
import com.example.dogwalker.WalkerProfile;
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

public class HomeFragment2 extends Fragment {

    List<WalkerProfile> walkerList;
    private WalkerListAdapter walkerListAdapter;

    EditText walker_name, walker_tel, walker_addr, walker_career;
    TextView walker_nurture;
    RadioGroup radioGroup;
    RadioButton btn1, btn2;

    DatabaseReference mDatabase;
    String uid,walkerUUID;


    public HomeFragment2() {
        // Required empty public constructor
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        FloatingActionButton floatBtn2 = view.findViewById(R.id.floatBtn2);

        walkerList = new ArrayList<WalkerProfile>();

        readFirebaseValue();
        walkerListAdapter = new WalkerListAdapter(walkerList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext(),
                RecyclerView.VERTICAL, false);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setAdapter(walkerListAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        walkerUUID = UUID.randomUUID().toString();
        floatBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });

        return view;
    }
    private void addContact() {
        View dialogView = LayoutInflater.from(getContext()).
                inflate(R.layout.walker_list_add, null);
        walker_name = dialogView.findViewById(R.id.walker_name);
        walker_tel = dialogView.findViewById(R.id.walker_tel);
        walker_addr = dialogView.findViewById(R.id.walker_addr);
        walker_career = dialogView.findViewById(R.id.walker_career);
        walker_nurture = dialogView.findViewById(R.id.walker_nurture);
        radioGroup = dialogView.findViewById(R.id.radioGroup);
        btn1 = dialogView.findViewById(R.id.btn1);
        btn2 = dialogView.findViewById(R.id.btn2);

        mDatabase =FirebaseDatabase.getInstance().getReference("list");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("신청자 프로필");
        builder.setView(dialogView);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WalkerProfile walker = new WalkerProfile();

                walker.setWalkerName(walker_name.getText().toString());
                walker.setWalkerTel(walker_tel.getText().toString());
                walker.setWalkerAddr(walker_addr.getText().toString());
                walker.setWalkerCareer(walker_career.getText().toString());
                walker.setWalkerNurture(walker_nurture.getText().toString());
                walker.setUid(uid);

                walker.setWalkerUUID(walkerUUID);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (btn1.isChecked()) {

                        } else if (btn2.isChecked()) {

                        }
                    }
                });

                walkerListAdapter.addItem(walker);

            }
        });
        builder.setNegativeButton("닫기", null);
        builder.show();
    }

    public void readFirebaseValue() {
        DatabaseReference listRef = mDatabase.child("list").child("walker");
        listRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    walkerList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String Key = snapshot.getKey();
                        Log.d("오너 키값 확인", Key + " ");
                        WalkerProfile value = snapshot.getValue(WalkerProfile.class);
                        Log.d("오너 값 확인", value.getWalkerName() + " ");
                        walkerList.add(value);
                    }
                    walkerListAdapter.notifyDataSetChanged();

                } else {
                    //  Log.d(TAG, task.getException().getMessage());.
                }
            }
        });
    }
}
