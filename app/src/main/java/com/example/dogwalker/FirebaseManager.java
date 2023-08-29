package com.example.dogwalker;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseManager {
//
    private  String uid;
    DatabaseReference mDatabase;


    public FirebaseManager(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 로그인된 사용자
        uid = user.getUid();
    }

    public void fireBaseImgUpload(ProgressBar progressBar, int num,Uri imgUrl,Context context)
    {
        if (imgUrl == null) {
            return;
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String fileName = "PROFILE_" + num+ ".jpg";

        //저장할 파일 위치에 대한 참조객체
        StorageReference imgRef = firebaseStorage.getReference(uid +"/"+ fileName); //저장할 이름
        //폴더가 없으면 만들고 있으면 그냥 참조한다

        //위 저장 경로 참조객체에게 실제파일 업로드 시키기
        imgRef.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "error : " + e, Toast.LENGTH_SHORT).show();
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


    public void fireBaseImgProfileUpload(ProgressBar progressBar, String uuid,Uri imgUrl,Context context,String path)
    {
        if (imgUrl == null) {
            return;
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String fileName = "PROFILE_" + uuid+ ".jpg";

        //저장할 파일 위치에 대한 참조객체
        StorageReference imgRef = firebaseStorage.getReference(uid +path +"/"+ fileName); //저장할 이름
        //폴더가 없으면 만들고 있으면 그냥 참조한다

        //위 저장 경로 참조객체에게 실제파일 업로드 시키기
        imgRef.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "error : " + e, Toast.LENGTH_SHORT).show();
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



    public void fireBaseImgLoad(ImageView profileImg,Context context,View view, int num) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String fileName = "PROFILE_" + num+ ".jpg";
        StorageReference imgRef = storageReference.child(uid+"/"+fileName);
        Toast.makeText(context,"imgRef",Toast.LENGTH_SHORT).show();
        if(imgRef != null) {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(context,"사진불러오기 성공" + uri.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("uri로그 " , uri.toString());
                    Glide.with(view.getContext())
                            .load(uri)
                            .into(profileImg);

                }
            });
        }



    }

    public void fireBaseImgLoad2(ImageView profileImg,Context context,View view, String uuid,String path) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String fileName = "PROFILE_" + uuid+ ".jpg";

        StorageReference imgRef = storageReference.child(uid+path+"/"+fileName);
        Toast.makeText(context,"imgRef",Toast.LENGTH_SHORT).show();
        if(imgRef != null) {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(context,"사진불러오기 성공" + uri.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("uri로그 " , uri.toString());
                    Glide.with(view.getContext())
                            .load(uri)
                            .into(profileImg);

                }
            });
        }



    }
    public void fireBaseImgDelete(int num)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();


        String fileName = "PROFILE_" + num+ ".jpg";
        StorageReference imgRef = storageReference.child(uid+"/"+fileName);
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public   void  createProfileImg(ImageView  profileImg, int num,View view,Uri imgUrl, ProgressBar progressBar,Context context)
    {
        //이미지 삭제
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();


        String fileName = "PROFILE_" + num+ ".jpg";
        StorageReference imgRef = storageReference.child(uid+"/"+fileName);
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//파이어베이스에 이미지 등록

        imgRef.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "error : " + e, Toast.LENGTH_SHORT).show();
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



        Toast.makeText(context,"프로필 불러오기",Toast.LENGTH_SHORT).show();
        if(imgRef != null) {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(context,"사진불러오기 성공" + uri.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("uri로그 " , uri.toString());
                    Glide.with(view.getContext())
                            .load(uri)
                            .into(profileImg);

                    //profileImg.setImageURI(uri);
                }
            });
        }

    }




    }







