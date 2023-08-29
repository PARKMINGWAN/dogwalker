package com.example.dogwalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WalkerDetail2 extends AppCompatActivity {
    private ProgressBar progressBar;
    String dogUUID,walkerUid,walkerUUID,ownerUid;
    Button btnCancel, btnWalkerComplete;
    DatabaseReference mDatabase;
    ApplicationWalkerProfile applicationWalkerProfile , applicationWalkerProfileWalker;
    TextView txtWalkerTel, txtWalkerAddr, txtWalkerName,txtWalkerCareer ,txtWalkerNurtuer,txtDay,txtCompleteDay;


    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_walke2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
         intent = getIntent();
        applicationWalkerProfileWalker = new ApplicationWalkerProfile();
        walkerUUID = intent.getStringExtra("walkerUUID");
        ownerUid = intent.getStringExtra("onwerUid");
        walkerUid =intent.getStringExtra("walkerUid");
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        txtWalkerAddr = findViewById(R.id.txtAddr);
        txtWalkerName = findViewById(R.id.txtName);
        txtWalkerCareer = findViewById(R.id.txtCareer);
        txtWalkerNurtuer = findViewById(R.id.txtNurture);
        txtWalkerTel = findViewById(R.id.txtTel);
        txtDay = findViewById(R.id.test8);
        txtCompleteDay = findViewById(R.id.txtCompleteDay);

        btnWalkerComplete = findViewById(R.id.btnWalkerComplete);

        txtDay.setVisibility(View.GONE);
        txtCompleteDay.setVisibility(View.GONE);
        btnWalkerComplete.setVisibility(View.GONE);
        applicationWalkerProfile = new ApplicationWalkerProfile();


        btnCancel = findViewById(R.id.btnCancle);





         readWalkerProfileFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(ApplicationWalkerProfile value) {
                if (value != null) {

                    applicationWalkerProfile = value;
                    Log.d("워커디테일 객체 데이터 로그 : ", applicationWalkerProfile.getWalkerAddr() + "");
                    txtWalkerName.setText(value.getWalkerName());
                    txtWalkerAddr.setText(value.getWalkerAddr());
                    txtWalkerTel.setText(value.getWalkerTel());
                    txtWalkerNurtuer.setText(value.getWalkerNurture());
                    txtWalkerCareer.setText(value.getWalkerCareer());
                    walkerUid = value.getUid();
                    Log.d("워커디테일 워커 uid : ", walkerUid + "");


                    if (value.getIsReservation().equals("0")) //워커가 오너한테 신청 오너가 수락 단계
                    {

                        btnCancel.setVisibility(View.VISIBLE);
                        btnWalkerComplete.setVisibility(View.GONE);
                    } else if (value.getIsReservation().equals("1"))//워커가 인수 인계 하는 단계 산책중으로 넘어감
                    {
                        btnWalkerComplete.setVisibility(View.VISIBLE);

                        btnCancel.setVisibility(View.VISIBLE);

                    } else if (value.getIsReservation().equals("2"))//오너가 인수인계 하는 단계 산책완료로 넘어감
                    {

                        btnCancel.setVisibility(View.GONE);
                        txtDay.setVisibility(View.GONE);
                        txtCompleteDay.setVisibility(View.GONE);
                    } else if (value.getIsReservation().equals("3")) {

                        btnCancel.setVisibility(View.GONE);
                        txtDay.setVisibility(View.VISIBLE);
                        txtCompleteDay.setVisibility(View.GONE);
                    }

                    btnWalkerComplete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            applicationWalkerProfile.setIsReservation("2");//인수인계완료
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                            Date date = new Date(System.currentTimeMillis());
                            applicationWalkerProfile.setCompleteDay(formatter.format(date).toString());
                            txtCompleteDay.setText(applicationWalkerProfile.getCompleteDay());
                            mDatabase.child("ApplicationList").child("owner").child(ownerUid).child(walkerUUID).setValue(applicationWalkerProfile);
                            mDatabase.child("ApplicationList").child("walker").child(walkerUid).child(walkerUUID).setValue(applicationWalkerProfile);
                            finish();
                        }
                    });
                }
            }


        });






    }

    public interface FirebaseCallback {
        void onResponse(ApplicationWalkerProfile value);
    }

    public void readWalkerProfileFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child("ApplicationList").child("walker").child(walkerUid).child(walkerUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    ApplicationWalkerProfile value = task.getResult().getValue(ApplicationWalkerProfile.class);
                    callback.onResponse(value);
                } else {

                }
            }
        });
    }
    public void readProfileFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child("ApplicationList").child("owner").child(ownerUid).child(walkerUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    ApplicationWalkerProfile value = task.getResult().getValue(ApplicationWalkerProfile.class);
                    callback.onResponse(value);
                } else {

                }
            }
        });
    }
}