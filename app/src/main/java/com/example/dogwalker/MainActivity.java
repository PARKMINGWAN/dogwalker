package com.example.dogwalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
//.
    Button btnJoinPage;
    Button btnSignPage;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnJoinPage = findViewById(R.id.btnJoinPage);
        btnSignPage = findViewById(R.id.btnSignInPage);
        btnLogout=findViewById(R.id.btnLogout);

        //자동 로그인처리
        Map<String, String> loginInfo = LoginSharedPreferencesManager.getLoginInfo(this);
        if (!loginInfo.isEmpty()) {
            String email = loginInfo.get("email");
            String password = loginInfo.get("password");
        }


        if (loginInfo.get("email").length()==0) //로그 아웃 상태
        {
            btnSignPage.setVisibility(View.VISIBLE);
            btnJoinPage.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            btnJoinPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
            btnSignPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }else { //로그인상태.
           Intent intent = new Intent(MainActivity.this, Walker_tab.class);

            startActivity(intent);
            btnSignPage.setVisibility(View.GONE);
            btnJoinPage.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> loginInfo = LoginSharedPreferencesManager.getLoginInfo(MainActivity.this);
                    LoginSharedPreferencesManager.clearPreferences(MainActivity.this);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

        }

    }
}