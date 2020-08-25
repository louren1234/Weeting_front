package com.example.again;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText e_mail, e_password;
    String email, password;
    SignUpData.ServiceApi serviceApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceApi = RetrofitClient.getClient().create(SignUpData.ServiceApi.class);
        e_mail = findViewById(R.id.mainId);
        e_password = findViewById(R.id.mainPassword);
    }

//    public void findId(View view){
//        Intent i = new Intent(getApplicationContext(),FIndIdActivity.class);
//        startActivity(i);
//    }
//    public void findPassword(View view){
//        Intent i = new Intent(getApplicationContext(),FindPasswordActivity.class);
//        startActivity(i);
//
//    }
    public void mainSignUp(View view){
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }
    public void LoginCheck(View view){
        email = e_mail.getText().toString();
        password = e_password.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this,"빈칸이 있습니다.",Toast.LENGTH_LONG).show();
        }
        LoginData data = new LoginData(email,password);
        serviceApi.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if(result.getState()==200){
                    Intent i = new Intent(getApplicationContext(), After_have_group.class);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "없는 아이디입니다.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "로그인 에러 발생", Toast.LENGTH_LONG).show();
                Log.e("로그인 에러 발생",t.getMessage());
            }

        });


    }

}
