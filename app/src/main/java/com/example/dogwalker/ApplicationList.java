package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class ApplicationList extends AppCompatActivity {
    List<ApplicationWalkerProfile> applicationWalkerProfileList,waitList,ProceedingList,CompleteList;
    private  ApplicationListAdapter applicationListAdapter;

    private WalkerListAdapter walkerListAdapter;

    EditText walker_name, walker_tel, walker_addr, walker_career;
    TextView walker_nurture;

    RadioButton btn1, btn2;
    Button btnWaitlist, btnProceeding,btnComplete;

    DatabaseReference mDatabase;
    String uid,walkerUUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_list);
        btnWaitlist = findViewById(R.id.btnWaitlist);
        btnProceeding=findViewById(R.id.btnProceeding);
        btnComplete =findViewById(R.id.btnComplete);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        btnWaitlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applicationWalkerProfileList =new ArrayList<ApplicationWalkerProfile>();
                readFirebaseValue();
                applicationListAdapter =new ApplicationListAdapter(ApplicationList.this,applicationWalkerProfileList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ApplicationList.this,
                        RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(applicationListAdapter);
                for (int i = 0; i < applicationWalkerProfileList.size(); i++)
                {
                    //if (applicationWalkerProfileList.get(i).get)
                }


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
                    applicationListAdapter.notifyDataSetChanged();

                } else {

                }
            }
        });
    }




}