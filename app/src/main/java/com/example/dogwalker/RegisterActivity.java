package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private EditText etEmail, etPass, etName;
    private Button btnCancel, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.etUserId);
        etPass = findViewById(R.id.etUserPass);
        etName = findViewById(R.id.etUserName);
        btnJoin = findViewById(R.id.btnJoin);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
       btnCancel= findViewById(R.id.btnCancel);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String strEmail = etEmail.getText().toString();
                String strPass = etPass.getText().toString();
                String strName = etName.getText().toString();

                mAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("join success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setUserIdToken(user.getUid());
                            account.setUserEmail(user.getEmail());
                            account.setUserPass(strPass);
                           // account.setUserName(strName);
                            mDatabaseRef.child("UserAccount").child(user.getUid()).setValue(account);
                            Toast.makeText(RegisterActivity.this, "회원가입 성공.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("join fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "회원가입 실패.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}




