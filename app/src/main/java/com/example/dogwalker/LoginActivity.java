package com.example.dogwalker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences shp;
    SharedPreferences.Editor shpEditor;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private EditText etEmail, etPass, etName;
    private Button btnCancel, btnLogin;
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private int RC_SIGN_IN = 40;
    GoogleSignInClient mGoogleSignInClient;

    private SignInButton btnGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etUserId);
        etPass = findViewById(R.id.etUserPass);
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnSignIn);
        btnCancel = findViewById(R.id.btnCancel);
        btnGoogleLogin = findViewById(R.id.btn_google_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = etEmail.getText().toString();
                String StrPass = etPass.getText().toString();

                mAuth.signInWithEmailAndPassword(strEmail, StrPass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //로그인정보를 SharedPreferences에 저장.
                            LoginSharedPreferencesManager.setLoginInfo(LoginActivity.this, strEmail, StrPass);

                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            Log.d("intent 화면 전환", intent.toString());
                            startActivity(intent);


                        } else {


                        }
                    }
                });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "구글 확인 : 클릭", Toast.LENGTH_SHORT).show();
                signIn();
            }
        });


    }

    private void signIn() {
        Toast.makeText(getApplicationContext(), "구글 확인 : siginIn", Toast.LENGTH_SHORT).show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    /* @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
         if (requestCode == RC_SIGN_IN) {
             // The Task returned from this call is always completed, no need to attach
             // a listener.
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             handleSignInResult(task);
         }
     }
     private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
         try {
             GoogleSignInAccount account = completedTask.getResult(ApiException.class);

             firebaseAuth(account.getIdToken());
         } catch (ApiException e) {
             // The ApiException status code indicates the detailed failure reason.
             // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
         }
     }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "구글 확인 : 파이어베이스 액티브 리졸트 시작", Toast.LENGTH_SHORT).show();

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Toast.makeText(getApplicationContext(), "구글 확인 : 액티브 리졸트", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(getApplicationContext(), "구글 확인 : 구글인 어카운트 생성", Toast.LENGTH_SHORT).show();
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }

        }

    }


    private void firebaseAuth(String idToken) {
        Toast.makeText(getApplicationContext(), "구글 확인 : 파베인증 시작", Toast.LENGTH_SHORT).show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Toast.makeText(getApplicationContext(), "구글 확인 : 파이어베이스 어스시작", Toast.LENGTH_SHORT).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "구글 확인 : task 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserAccount users = new UserAccount();
                            Log.d("user Uid : ",user.getUid()+" ");
                            Log.d("user Uid : ",user.getDisplayName()+" ");
                            users.setUserEmail(user.getUid());
                            users.setUserName(user.getDisplayName());
                            Toast.makeText(getApplicationContext(), "구글 확인 : " + users.getUserName().toString(), Toast.LENGTH_SHORT).show();

                            //mDatabaseRef.child("Users").child(user.getUid()).setValue(users);
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "구글 확인 : 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}