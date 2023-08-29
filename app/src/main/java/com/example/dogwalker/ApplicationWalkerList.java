package com.example.dogwalker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ApplicationWalkerList extends AppCompatActivity {
    List<ApplicationWalkerProfile> applicationWalkerProfileList,waitList,proceedingList,completeList;
    private  ApplicationWalkerListAdapter applicationWalkerListAdapter;



    EditText walker_name, walker_tel, walker_addr, walker_career;
    TextView walker_nurture,textState;

    RadioButton btn1, btn2;
    Button btnWaitlist, btnProceeding,btnComplete;

    DatabaseReference mDatabase;
    String uid,walkerUUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_walker_list);
        btnWaitlist = findViewById(R.id.btnWaitlist);
        btnProceeding=findViewById(R.id.btnProceeding);
        btnComplete =findViewById(R.id.btnComplete);
        textState = findViewById(R.id.textState);
        applicationWalkerProfileList =new ArrayList<ApplicationWalkerProfile>();
        waitList =new ArrayList<ApplicationWalkerProfile>();
        proceedingList =new ArrayList<ApplicationWalkerProfile>();
        completeList =new ArrayList<ApplicationWalkerProfile>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        readFirebaseValue();
        applicationWalkerListAdapter =new ApplicationWalkerListAdapter(ApplicationWalkerList.this,proceedingList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ApplicationWalkerList.this,
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(applicationWalkerListAdapter);
        textState.setVisibility(View.GONE);
        btnWaitlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textState.setVisibility(View.VISIBLE);
                textState.setText("예약 대기 리스트");
                waitList.clear();
                readFirebaseValue();
                for (int i = 0; i < applicationWalkerProfileList.size(); i++)
                {
                    if (applicationWalkerProfileList.get(i).getIsReservation().equals("0"))
                    {
                        waitList.add(applicationWalkerProfileList.get(i));
                    }
                }

                applicationWalkerListAdapter =new ApplicationWalkerListAdapter(ApplicationWalkerList.this,waitList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ApplicationWalkerList.this,
                        RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(applicationWalkerListAdapter);
                applicationWalkerListAdapter.notifyDataSetChanged();


            }
        });

        btnProceeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textState.setVisibility(View.VISIBLE);
                textState.setText("산책 진행중 리스트");
                proceedingList.clear();
                readFirebaseValue();
                for (int i = 0; i < applicationWalkerProfileList.size(); i++)
                {
                    if (applicationWalkerProfileList.get(i).getIsReservation().equals("1"))
                    {
                        proceedingList.add(applicationWalkerProfileList.get(i));
                    }
                }

                applicationWalkerListAdapter =new ApplicationWalkerListAdapter(ApplicationWalkerList.this,proceedingList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ApplicationWalkerList.this,
                        RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(applicationWalkerListAdapter);
                applicationWalkerListAdapter.notifyDataSetChanged();


            }
        });


        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textState.setVisibility(View.VISIBLE);
                textState.setText("산책 완료 내역 리스트");
                completeList.clear();
                readFirebaseValue();
                for (int i = 0; i < applicationWalkerProfileList.size(); i++)
                {
                    if (applicationWalkerProfileList.get(i).getIsReservation().equals("2"))
                    {
                        completeList.add(applicationWalkerProfileList.get(i));
                    }
                }

                applicationWalkerListAdapter =new ApplicationWalkerListAdapter(ApplicationWalkerList.this,completeList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ApplicationWalkerList.this,
                        RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(applicationWalkerListAdapter);
                applicationWalkerListAdapter.notifyDataSetChanged();


            }
        });

        Log.d("리스트 테스트 ","생성 끝");
    }

    public void readFirebaseValue() {
        Log.d("오우너 파베 시작 : ","1");
        DatabaseReference listRef = mDatabase.child("ApplicationList").child(uid);
        Log.d("오우너 파베 시작 : ","2");
        listRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("오우너 파베 시작 : ","3");
                if (task.isSuccessful()) {
                    Log.d("오우너 파베 시작 : ","4");
                    applicationWalkerProfileList.clear();
                    Log.d("오우너 UID : ",uid+"6");
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        //  String Key = snapshot.getKey();
                        // Log.d("오우너 키값 확인", Key + " ");
                        Log.d("오우너 UID : ",uid+"6");
                        ApplicationWalkerProfile value = snapshot.getValue(ApplicationWalkerProfile.class);
                        Log.d("오우너 값 확인", value.getWalkerAddr() + " ");

                        applicationWalkerProfileList.add(value);

                    }
                    applicationWalkerListAdapter.notifyDataSetChanged();

                } else {

                }
            }
        });
    }




}