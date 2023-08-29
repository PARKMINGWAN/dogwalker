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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WalkerDetail extends AppCompatActivity {
    private ProgressBar progressBar;
    String dogUUID,walkerUid,walkerUUID,ownerUid;
    Button btnApplication,btnCancel;
    DatabaseReference mDatabase;
    ApplicationWalkerProfile applicationWalkerProfile;
    TextView txtWalkerTel, txtWalkerAddr, txtWalkerName,txtWalkerCareer ,txtWalkerNurtuer;

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
        applicationWalkerProfile= new ApplicationWalkerProfile();
        btnApplication =findViewById(R.id.btnApproval);
        btnCancel=findViewById(R.id.btnCancle);
        Log.d("워커 디테일 오너 uid : ",ownerUid+"");
        Log.d("워커 디테일 워커 uuid : ",walkerUUID+"");

        readWalkerProfileFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(ApplicationWalkerProfile value) {
if (value!=null)
{

    applicationWalkerProfile =value;
    Log.d("워커디테일 객체 데이터 로그 : ",applicationWalkerProfile.getWalkerAddr()+"");
    txtWalkerName.setText(value.getWalkerName());
    txtWalkerAddr.setText(value.getWalkerAddr());
    txtWalkerTel.setText(value.getWalkerTel());
    txtWalkerNurtuer.setText(value.getWalkerNurture());
    txtWalkerCareer.setText(value.getWalkerCareer());


}
            }
        });

        
        btnApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationWalkerProfile.setIsReservation("1");//예약진행중
                mDatabase.child("ApplicationList").child(ownerUid).child(walkerUUID).setValue(applicationWalkerProfile);
            }
        });

    }

    public interface FirebaseCallback {
        void onResponse(ApplicationWalkerProfile value);
    }

    public void readWalkerProfileFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child("ApplicationList").child(ownerUid).child(walkerUUID);
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