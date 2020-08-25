package com.example.again;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class SignUpActivity extends AppCompatActivity {

    private EditText e_name, e_password, e_nickname, e_mail;
    private SignUpData.ServiceApi serviceApi;
    boolean cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        e_name = findViewById(R.id.sign_upName);
        e_password = findViewById(R.id.sign_upPassword);
        e_nickname = findViewById(R.id.sign_upId);
        e_mail= findViewById(R.id.sign_upEmail);
        serviceApi = RetrofitClient.getClient()
                .create(SignUpData.ServiceApi.class);

        cancel = false;

    }
    private boolean SignUpValid(){
        e_name.setError(null);
        e_password.setError(null);
        e_nickname.setError(null);
        e_mail.setError(null);
        String name = e_name.getText().toString();
        String password = e_password.getText().toString();
        String id = e_nickname.getText().toString();
        String email = e_mail.getText().toString();
        View focusView = null;
        if (name.isEmpty() || password.isEmpty() || id.isEmpty()||email.isEmpty()) {
//            e_nickname.setError("이름을 입력해주세요");
//            focusView=e_nickname;
//            cancel=true;
            Toast.makeText(getApplicationContext(),"비어있는 칸이 있습니다.",Toast.LENGTH_LONG).show();
        }
        else{
            cancel = true;
        }
        return cancel;
    }
    public void SignUpCheck(View view) throws ParseException {
        e_name.setError(null);
        e_password.setError(null);
        e_nickname.setError(null);
        e_mail.setError(null);
        String birth = "2017-07-30 10:10:10";
        SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date birth_date = trans.parse(birth);
        String name = e_name.getText().toString();
        String password = e_password.getText().toString();
        String id = e_nickname.getText().toString();
        String email = e_mail.getText().toString();
        ImageView imageView = findViewById(R.id.basicImage);
        imageView.setImageResource(R.drawable.ic_person_black_24dp);

        View focusView = null;
        if (name.isEmpty() || password.isEmpty() || id.isEmpty()||email.isEmpty()) {
//            e_nickname.setError("이름을 입력해주세요");
//            focusView=e_nickname;
//            cancel=true;
            Toast.makeText(getApplicationContext(),"비어있는 칸이 있습니다.",Toast.LENGTH_LONG).show();
            //String user_email, String user_name, String user_passwd, String user_nick_name, Date user_birth
        }
        else{
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            startSignUp(new SignUpData(password,birth_date,email,name,id,null));
            Toast.makeText(getApplicationContext(), "로그인을 해보세요!", Toast.LENGTH_LONG).show();
        }
    }
    private void startSignUp(SignUpData data){
        serviceApi.userSignUP(data).enqueue(new Callback<SignUpData.SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpData.SignUpResponse> call, Response<SignUpData.SignUpResponse> response) {
                SignUpData.SignUpResponse result = response.body();
                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                if(result.getStatus()==200){
                    System.out.println("sdfsdfsdfdfdfsfsdfsd");
                    finish();
                }
                else if(result.getStatus() == 400){
                    System.out.println("gggggggggggggggggggggg");
                }
            }

            @Override
            public void onFailure(Call<SignUpData.SignUpResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"회원가입 에러",Toast.LENGTH_LONG).show();
                Log.e("회원가입 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
