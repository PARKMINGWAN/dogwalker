package com.example.dogwalker.ui.mypage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dogwalker.FirebaseManager;
import com.example.dogwalker.HomePage;
import com.example.dogwalker.LoginSharedPreferencesManager;
import com.example.dogwalker.MainActivity;
import com.example.dogwalker.WalkerMypageAdd;
import com.example.dogwalker.Walker_tab;
import com.example.dogwalker.R;
import com.example.dogwalker.Walker;
import com.example.dogwalker.WalkerAdapter;
import com.example.dogwalker.databinding.FragmentWalkerMypageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WalkerMyPageFragment extends Fragment {

    private FragmentWalkerMypageBinding binding;

    WalkerAdapter walkerAdapter;
    public List<Walker> walkerList;
    Walker walker,walker2;
    DatabaseReference mDatabase;

    TextView txtName, txtId, txtPwd,txtTel,txtAddr,txtCareer,txtNurture;
    String name, id, pwd, tel, addr, career, nurture;
    String uid;
    private ProgressBar progressBar;
    StorageReference reference;
    Uri imgUrl;
    Button btnImgInsert, btnUpdate, btnLogout,btnInsert,btnImgDelete,btnAdd;
    ImageView profileImg;

    EditText etName,etId,etPwd,etTel,etAddr,etCareer;
    Spinner etNurture;
    FirebaseManager firebaseManager = new FirebaseManager();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWalkerMypageBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_walker_mypage, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        profileImg = view.findViewById(R.id.imgProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnInsert = view.findViewById(R.id.btnInsert);
        btnImgInsert = view.findViewById(R.id.btnimgInsert);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnImgDelete = view.findViewById(R.id.btnImgDelete);
        btnAdd = view.findViewById(R.id.btnAdd);
        txtName = view.findViewById(R.id.txtName);
        txtId = view.findViewById(R.id.txtId);
        txtPwd = view.findViewById(R.id.txtPwd);
        txtTel = view.findViewById(R.id.txtTel);
        txtAddr = view.findViewById(R.id.txtAddr);
        txtCareer = view.findViewById(R.id.txtCareer);
        txtNurture = view.findViewById(R.id.txtNurture);



        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        profileImg.setImageURI(uri);
                        imgUrl = uri;

                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        progressBar.setVisibility(View.INVISIBLE);
        firebaseManager.fireBaseImgLoad(profileImg, getContext(), view, 0);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());

            }
        });


        View dialogView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.item_basicinfo, null);

        etName = dialogView.findViewById(R.id.etName);
        etId = dialogView.findViewById(R.id.etId);
        etPwd = dialogView.findViewById(R.id.etPwd);
        etTel = dialogView.findViewById(R.id.etTel);
        etAddr = dialogView.findViewById(R.id.etAddr);
        etCareer = dialogView.findViewById(R.id.etCareer);
        etNurture = dialogView.findViewById(R.id.etNurture);


        btnImgInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseManager.createProfileImg(profileImg, 0, view, imgUrl, progressBar, getContext());
            }
        });

        readFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(Walker value) {
                if (value!=null) {
                    walker2 =new Walker();
                    txtName.setText( value.getName());
                    txtId.setText( value.getId());
                    txtAddr.setText( value.getAddr());
                    txtTel.setText( value.getTel());
                    txtPwd.setText( value.getPwd());
                    txtCareer.setText(value.getCareer());
                    txtNurture.setText(value.getNurture());

                    walker2.setName(value.getName());
                    walker2.setId(value.getId());
                    walker2.setAddr(value.getAddr());
                    walker2.setTel(value.getTel());
                    walker2.setCareer(value.getCareer());
                    walker2.setNurture(value.getNurture());
                    walker2.setPwd(value.getPwd());


                }else {

                }
            }
        });

        btnImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseManager.fireBaseImgDelete(0);
                profileImg.setImageResource(R.drawable.default_profile);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WalkerMypageAdd.class);
                startActivity(intent);
            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
                String uid = user.getUid();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(dialogView);
                builder.setNegativeButton("취소", null);
                etId.setText(LoginSharedPreferencesManager.getLoginInfo(view.getContext()).get("email"));
                etPwd.setText(LoginSharedPreferencesManager.getLoginInfo(view.getContext()).get("password"));

                builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        walker = new Walker();
                        walker.setAddr(etAddr.getText().toString());
                        walker.setTel(etTel.getText().toString());
                        walker.setPwd(etPwd.getText().toString());
                        walker.setCareer(etCareer.getText().toString());
                        walker.setId(etId.getText().toString());
                        walker.setName(etName.getText().toString());
                        walker.setNurture(etNurture.getSelectedItem().toString());

                        mDatabase.child(uid).child("walker").setValue(walker);

                    }
                });
                builder.show();

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> loginInfo = LoginSharedPreferencesManager.getLoginInfo(view.getContext());
                LoginSharedPreferencesManager.clearPreferences(view.getContext());
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btnUpdate 진입 : ", "++++++");
                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
                String uid = user.getUid();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(dialogView);

                etName.setText(walker2.getName());
                etId.setText(walker2.getId());
                etPwd.setText(walker2.getPwd());
                etAddr.setText(walker2.getAddr());
                etCareer.setText(walker2.getCareer());
                etTel.setText(walker2.getTel());

                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     //   etName.setText(walker2.getName());
                        Log.d("수정 이름 : ",walker2.getName()+"");

                        walker2.setAddr(etAddr.getText().toString());
                        walker2.setTel(etTel.getText().toString());
                        walker2.setPwd(etPwd.getText().toString());
                        walker2.setCareer(etCareer.getText().toString());
                        walker2.setId(etId.getText().toString());
                        walker2.setName(etName.getText().toString());
                        walker2.setNurture(etNurture.getSelectedItem().toString());

                        mDatabase.child(uid).child("walker").setValue(walker2);
                        txtName.setText(walker2.getName());
                        txtAddr.setText(walker2.getAddr());
                        txtCareer.setText(walker2.getCareer());
                        txtPwd.setText(walker2.getPwd());
                        txtTel.setText(walker2.getTel());
                        txtNurture.setText(walker2.getNurture());

                    }
                });
                builder.show();

            }
        });

        return view;
    }



    public interface FirebaseCallback {
        void onResponse(Walker value);
    }


//    public void readFirebaseValue(FirebaseCallback callback) {
//
//        DatabaseReference uidRef = mDatabase.child(uid).child("walker");
//        Log.d("getKey : ", uidRef.getKey() );
//        uidRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               // Log.d("Value : ", dataSnapshot.child(uid).child("walker").getValue().toString());
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Log.d("워커이름 : ", ds.getValue().toString()+"" );
//                    Walker value = ds.getValue(Walker.class);
//
//                    Log.d("워커이름 ", value.getName()+"");
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


    public void readFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child(uid).child("walker");
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Walker value = task.getResult().getValue(Walker.class);

                    callback.onResponse(value);
                } else {

                }
            }
        });
    }

}