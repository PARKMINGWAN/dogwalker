package com.example.dogwalker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/*
로그인 sharedPreferences 관리용 매니저 클래스
2023-08-17
 */
public class LoginSharedPreferencesManager {
    private static final String PREFERENCES_NAME = "Login_Data_Key";

    public static SharedPreferences getPreferences(Context mContext){
        return mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setLoginInfo(Context context, String email, String password){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("password", password);

        editor.apply();//비동기 처리
    }

    public static Map<String, String> getLoginInfo(Context context){
        SharedPreferences prefs = getPreferences(context);
        Map<String, String> LoginInfo = new HashMap<>();
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");

        LoginInfo.put("email", email);
        LoginInfo.put("password", password);

        return LoginInfo;
    }

    
    //삭제
    public static void clearPreferences(Context context){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    } 
}
