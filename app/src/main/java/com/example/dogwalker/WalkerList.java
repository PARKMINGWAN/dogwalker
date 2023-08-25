package com.example.dogwalker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WalkerList extends AppCompatActivity {
    private WalkerListAdapter walkerListAdapter;
    EditText name, nurture, carrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton floatBtn = findViewById(R.id.floatBtn);

        List<Walker> walkerList = new ArrayList<>();
        walkerListAdapter = new WalkerListAdapter(walkerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WalkerList.this,
                RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(walkerListAdapter);

        for (int i=0; i<walkerList.size(); i++) {
            walkerListAdapter.addItem(new Walker());
            // walkerList.add(new Walker());
            // }
        }

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addContact();
            }
        });
    }
    private void addContact() {
        View dialogView = LayoutInflater.from(getApplicationContext()).
            inflate(R.layout.walker_list_add, null);

        name = dialogView.findViewById(R.id.name);
        nurture = dialogView.findViewById(R.id.nurture);
        carrer = dialogView.findViewById(R.id.carrer);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        RadioButton btn1 = dialogView.findViewById(R.id.btn1);
        RadioButton btn2 = dialogView.findViewById(R.id.btn2);

        AlertDialog.Builder builder = new AlertDialog.Builder(WalkerList.this);
        builder.setTitle("리스트 등록");
        builder.setView(dialogView);

        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {Walker walker = new Walker();
                walker.setName("이름: " + name.getText().toString());walker.setNurture("양육 유무: " + nurture.getText().toString());
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (btn1.isChecked()) {walker.setNurture(btn1.getText().toString());
                    } else if (btn2.isChecked()) {
                        walker.setNurture(btn2.getText().toString());
                    }
                }

        });
        walker.setCareer("경력: " + carrer.getText().toString());
        Log.d("insert", "onClick: 등록 클릭시 값 확인" + walker);
        walkerListAdapter.addItem(walker);
        }
    });

        /*        AlertDialog.Builder builder2 = new AlertDialog.Builder(OwnerList.this);
        builder2.setView(dialogView);
        builder2.setPositiveButton("수정", new DialogInterface.OnClickListener() {
        @Overridepublic void onClick(DialogInterface dialogInterface, int i) {
        Walker walker = new Walker();
        }});*/
        builder.setNegativeButton("닫기", null);
        builder.show();

        }
}