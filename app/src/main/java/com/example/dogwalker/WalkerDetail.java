package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WalkerDetail extends AppCompatActivity {
    private ProgressBar progressBar;
    String dogUUID,walkerUid,walkerUUID,ownerUid;
    Button btnApplication,btnCancel,btnComplete;
    DatabaseReference mDatabase;
    ApplicationWalkerProfile applicationWalkerProfile , applicationWalkerProfileWalker;
    TextView txtWalkerTel, txtWalkerAddr, txtWalkerName,txtWalkerCareer ,txtWalkerNurtuer,txtDay,txtCompleteDay,txtWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_walke);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        walkerUUID = intent.getStringExtra("walkerUUID");
        ownerUid = intent.getStringExtra("onwerUid");
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        txtWalkerAddr =findViewById(R.id.txtAddr);
        txtWalkerName =findViewById(R.id.txtName);
        txtWalkerCareer =findViewById(R.id.txtCareer);
        txtWalkerNurtuer=findViewById(R.id.txtNurture);
        txtWalkerTel=findViewById(R.id.txtTel);
        txtDay =findViewById(R.id.test8);
        txtCompleteDay=findViewById(R.id.txtCompleteDay);
txtWait=findViewById(R.id.txtWait);

        txtWait.setVisibility(View.GONE);
        txtDay.setVisibility(View.GONE);
        txtCompleteDay.setVisibility(View.GONE);

        applicationWalkerProfile= new ApplicationWalkerProfile();
       // applicationWalkerProfileWalker = new ApplicationWalkerProfile();

        btnApplication =findViewById(R.id.btnApproval);
        btnCancel=findViewById(R.id.btnCancle);
        btnComplete= findViewById(R.id.btnComplete);


        btnComplete.setVisibility(View.GONE);
        Log.d("워커 디테일 오너 uid : ",ownerUid+"");
        Log.d("워커 디테일 워커 uuid : ",walkerUUID+"");

        readWalkerProfileFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(ApplicationWalkerProfile value) {
if (value!=null) {

    applicationWalkerProfile = value;
    Log.d("워커디테일 객체 데이터 로그 : ", applicationWalkerProfile.getWalkerAddr() + "");
    walkerUid = value.getUid();
    Log.d("워커디테일 워커 uid : ",walkerUid+"");
    txtWalkerName.setText(value.getWalkerName());
    txtWalkerAddr.setText(value.getWalkerAddr());
    txtWalkerTel.setText(value.getWalkerTel());
    txtWalkerNurtuer.setText(value.getWalkerNurture());
    txtWalkerCareer.setText(value.getWalkerCareer());





    if (value.getIsReservation().equals("0")) //워커가 오너한테 신청 오너가 수락 단계
    {
        btnApplication.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnComplete.setVisibility(View.GONE);
        txtWait.setVisibility(View.GONE);

    }else if (value.getIsReservation().equals("1"))//워커가 인수 인계 하는 단계 산책중으로 넘어감
    {

        btnApplication.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnComplete.setVisibility(View.GONE);
        txtWait.setVisibility(View.VISIBLE);

    }else if(value.getIsReservation().equals("2"))//오너가 인수인계 하는 단계 산책완료로 넘어감
    {
        btnComplete.setVisibility(View.VISIBLE);
        btnApplication.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        txtDay.setVisibility(View.GONE);
        txtCompleteDay.setVisibility(View.GONE);
        txtWait.setVisibility(View.GONE);

    }else  if(value.getIsReservation().equals("3"))
    {
        btnComplete.setVisibility(View.VISIBLE);
        btnApplication.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        txtDay.setVisibility(View.VISIBLE);
        txtCompleteDay.setVisibility(View.GONE);
        txtWait.setVisibility(View.GONE);
    }
    btnComplete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            applicationWalkerProfile.setIsReservation("3");//인수인계완료
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            applicationWalkerProfile.setCompleteDay(formatter.format(date).toString());
            txtCompleteDay.setText(applicationWalkerProfile.getCompleteDay());
            mDatabase.child("ApplicationList").child("owner").child(ownerUid).child(walkerUUID).setValue(applicationWalkerProfile);
            mDatabase.child("ApplicationList").child("walker").child(walkerUid).child(walkerUUID).setValue(applicationWalkerProfile);
            Intent intent1 = new Intent(WalkerDetail.this, ApplicationList.class);
            startActivity(intent1);
        }
    });
    btnApplication.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            applicationWalkerProfile.setIsReservation("1");//오너가 수락

            mDatabase.child("ApplicationList").child("owner").child(ownerUid).child(walkerUUID).setValue(applicationWalkerProfile);
            mDatabase.child("ApplicationList").child("walker").child(walkerUid).child(walkerUUID).setValue(applicationWalkerProfile);



            Intent intent1 = new Intent(WalkerDetail.this, ApplicationList.class);
            startActivity(intent1);
        }
    });


}
}



        });


      /*  readProfileFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(ApplicationWalkerProfile value) {
                applicationWalkerProfileWalker= value;

            }
        });
*/




    }

    public interface FirebaseCallback {
        void onResponse(ApplicationWalkerProfile value);
    }

    public void readWalkerProfileFirebaseValue(FirebaseCallback callback) {

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



    public interface FirebaseCallbackOwnerProfile {
        void onResponse(OwnerProfile value);
    }

    public void readWalkerProfileFirebaseValue(FirebaseCallbackOwnerProfile callback) {

        DatabaseReference uidRef = mDatabase.child("list").child("owner").child(dogUUID);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    OwnerProfile value = task.getResult().getValue(OwnerProfile.class);

                    callback.onResponse(value);
                } else {

                }
            }
        });
    }


}