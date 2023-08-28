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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dogwalker.ApplicationList;
import com.example.dogwalker.LoginSharedPreferencesManager;
import com.example.dogwalker.MainActivity;
import com.example.dogwalker.OwnerMypageAdd;
import com.example.dogwalker.Owner;
import com.example.dogwalker.R;
import com.example.dogwalker.Walker;
import com.example.dogwalker.WalkerAdapter;
import com.example.dogwalker.databinding.FragmentOwnerMypageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OwnerMyPageFragment extends Fragment {

    private FragmentOwnerMypageBinding binding;

    WalkerAdapter walkerAdapter;
    public List<Walker> walkerList;
    Walker walker;
    DatabaseReference mDatabase;

    TextView txtName, txtId, txtPwd, txtTel, txtAddr, txtBreed, txtDogAge, txtDogWalk;
    String name, id, pwd, tel, addr, breed, dogage, dogwalk;
    String uid;
    private ProgressBar progressBar;
    StorageReference reference;
    Uri imgUrl;
    Button btnImgInsert, btnUpdate, btnLogout, btnInsert, btnAdd, btnReservation;
    ImageView profileImg;

    EditText etName, etId, etPwd, etTel, etAddr, etBreed, etDogAge, etDogWalk;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOwnerMypageBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_owner_mypage, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        profileImg = view.findViewById(R.id.imgProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnInsert = view.findViewById(R.id.btnInsert);
        btnImgInsert = view.findViewById(R.id.btnimgInsert);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnReservation = view.findViewById(R.id.btnReservation);
        txtName = view.findViewById(R.id.txtName);
        txtId = view.findViewById(R.id.txtId);
        txtPwd = view.findViewById(R.id.txtPwd);
        txtTel = view.findViewById(R.id.txtTel);
        txtAddr = view.findViewById(R.id.txtAddr);
        txtBreed = view.findViewById(R.id.txtBreed);
        txtDogAge = view.findViewById(R.id.txtDogAge);
        txtDogWalk = view.findViewById(R.id.txtDogWalk);


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
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());

            }
        });


        View dialogView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.item_basicinfo2, null);

        etName = dialogView.findViewById(R.id.etName);
        etId = dialogView.findViewById(R.id.etId);
        etPwd = dialogView.findViewById(R.id.etPwd);
        etTel = dialogView.findViewById(R.id.etTel);
        etAddr = dialogView.findViewById(R.id.etAddr);
        etBreed = dialogView.findViewById(R.id.etBreed);
        etDogAge = dialogView.findViewById(R.id.etDogAge);
        etDogWalk = dialogView.findViewById(R.id.etDogWalk);

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 Intent intent = new Intent(view.getContext(), ApplicationList.class);
 startActivity(intent);
            }
        });
        btnImgInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireBaseImgUpload();

            }
        });

        readFirebaseValue(new FirebaseCallback() {
            @Override
            public void onResponse(Owner value) {
                if (value != null) {
                    txtName.setText(value.getName());
                    txtId.setText(value.getId());
                    txtAddr.setText(value.getAddr());
                    txtTel.setText(value.getTel());
                    txtPwd.setText(value.getPwd());
                    txtBreed.setText(value.getBreed());
                    txtDogAge.setText(value.getDog_age());
                    txtDogWalk.setText(value.getDog_walk());
                } else {

                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), OwnerMypageAdd.class);
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
                        Owner owner1 = new Owner();
                        owner1.setAddr(etAddr.getText().toString());
                        owner1.setTel(etTel.getText().toString());
                        owner1.setPwd(etPwd.getText().toString());
                        owner1.setBreed(etBreed.getText().toString());
                        owner1.setId(etId.getText().toString());
                        owner1.setName(etName.getText().toString());
                        owner1.setDog_age(etDogAge.getText().toString());
                        owner1.setDog_walk(etDogWalk.getText().toString());

                        mDatabase.child(uid).child("owner").setValue(owner1);

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
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("수정", null);
                Log.d("btnUpdate dialogView : ", dialogView.toString());


            }
        });


        return view;
    }


    private void fireBaseImgUpload() {
        if (imgUrl == null) {
            return;
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        //저장할 파일 이름이 중복되지 않도록 날짜 붙여주기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";

        //저장할 파일 위치에 대한 참조객체
        StorageReference imgRef = firebaseStorage.getReference(uid + "/" + fileName); //저장할 이름
        //폴더가 없으면 만들고 있으면 그냥 참조한다

        //위 저장 경로 참조객체에게 실제파일 업로드 시키기
        imgRef.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    public interface FirebaseCallback {
        void onResponse(Owner value);
    }

    public void readFirebaseValue(FirebaseCallback callback) {

        DatabaseReference uidRef = mDatabase.child(uid).child("owner");
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Owner value = task.getResult().getValue(Owner.class);

                    callback.onResponse(value);
                } else {

                }
            }
        });
    }

    private void fireBaseImgLoad(ImageView imageView, Context context, String path) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference imgRef = storageReference.child(uid);
        Toast.makeText(getContext(), "imgRef", Toast.LENGTH_SHORT).show();
        if (imgRef != null) {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(getContext(), "사진불러오기 성공", Toast.LENGTH_SHORT).show();
                    Glide.with(context)
                            .load(storageReference)
                            .into(imageView);

                    imageView.setImageURI(uri);
                }
            });
        }
    }


}