package com.example.again;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.CookieJar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText e_mail, e_password;
    LinearLayout buttons;
    String email, password;
    SignUpData.ServiceApi serviceApi;
    UserData.serviceApi serviceApi2;
    UserData.UserDataResponse dataList;
    String user;

    MoimCategoryResultData.serviceApi checkMoimServiceApi;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = "";
        serviceApi = RetrofitClient.getClient().create(SignUpData.ServiceApi.class);
        checkMoimServiceApi = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);
        e_mail = findViewById(R.id.mainId);
        e_password = findViewById(R.id.mainPassword);
        buttons = findViewById(R.id.buttons);

        sp = getSharedPreferences("myFile", Context.MODE_PRIVATE);

        email = sp.getString("email", null);
        password = sp.getString("password", null);

        e_mail.setVisibility(View.GONE);
        e_password.setVisibility(View.GONE);
        buttons.setVisibility(View.GONE);

        if (email != null || password != null) {
            LoginData autologin = new LoginData(email, password);
            serviceApi.userLogin(autologin).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse result = response.body();
                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getState() == 200) {

                        Intent i = new Intent(getApplicationContext(), After_have_group.class);
                        i.putExtra("email", email);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "없는 아이디입니다.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "로그인 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("로그인 에러 발생", t.getMessage());
                }

            });
        } else {
            e_mail.setVisibility(View.VISIBLE);
            e_password.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
        }
    }

    public void mainSignUp(View view) {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }

    public void LoginCheck(View view) {
        email = e_mail.getText().toString();
        password = e_password.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "빈칸이 있습니다.", Toast.LENGTH_LONG).show();
        }
        LoginData data = new LoginData(email, password);
        Login(data);
    }

    public void Login(LoginData logindata) {
        serviceApi.userLogin(logindata).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getState() == 200) {

                    sp = getSharedPreferences("myFile", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.commit();
                    getNickName();
                    Intent i = new Intent(getApplicationContext(), After_have_group.class);
                    i.putExtra("email", email);
                    startActivity(i);

                    String s = sp.getString("name", "");
                    System.out.println(s + "sssssssssssssss");
                    finish();
//                    checkHaveOrNotHaveMoim();
                } else {
                    Toast.makeText(MainActivity.this, "없는 아이디입니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "로그인 에러 발생", Toast.LENGTH_LONG).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    protected void getNickName() {
        serviceApi2 = RetrofitClient.getClient().create(UserData.serviceApi.class);
        Call<UserData.UserDataResponse> call = serviceApi2.getMyInfo();

        call.enqueue(new Callback<UserData.UserDataResponse>() {
            @Override
            public void onResponse(Call<UserData.UserDataResponse> call, Response<UserData.UserDataResponse> response) {
                dataList = response.body();

                for (UserData userData : dataList.list) {
                    user = userData.getUser_nick_name();
                }
                sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("name", user);
                edit.commit();
            }

            @Override
            public void onFailure(Call<UserData.UserDataResponse> call, Throwable t) {
                Log.d("마이페이지에서 내 정보 가져오기 ", "실패원인 : ", t);
            }
        });


    }
}