package com.example.again;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import org.w3c.dom.Text;

public class Mypage extends AppCompatActivity {

    private ImageView myImage;
    private TextView myId;
    private TextView myIntroduce;

    UserData.serviceApi serviceApi;
    UserData.UserDataResponse dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        myImage = findViewById(R.id.mypageMyImage);
        myImage.setBackground(new ShapeDrawable(new OvalShape()));
        myImage.setClipToOutline(true);

        myId = findViewById(R.id.mypageMyId);
        myIntroduce = findViewById(R.id.mypageMyIntroduce);

        ImageView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMypage = findViewById(R.id.toMypage);

        LinearLayout editmyinform = findViewById(R.id.myinform);
        LinearLayout interest = findViewById(R.id.interest);
        LinearLayout qna = findViewById(R.id.qna);
        LinearLayout terms_of_use = findViewById(R.id.terms_of_use);

        setInfo();

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchList.class);
                startActivity(intent);
            }
        });

        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                startActivity(intent);
            }
        });

        toList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MoimList.class);
                intent.putExtra("category", "all");
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });

        toMypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        // 리스트

        editmyinform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditMyInformation.class);
                startActivity(intent);
            }
        });

        interest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Interest.class);
                startActivity(intent);
            }
        });


        qna.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Qna.class);
                startActivity(intent);
            }
        });

        terms_of_use.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Terms_of_use.class);
                startActivity(intent);
            }
        });
    }

    protected void setInfo(){
        serviceApi = RetrofitClient.getClient().create(UserData.serviceApi.class);
        Call<UserData.UserDataResponse> call = serviceApi.getMyInfo();

        call.enqueue(new Callback<UserData.UserDataResponse>() {
            @Override
            public void onResponse(Call<UserData.UserDataResponse> call, Response<UserData.UserDataResponse> response) {
                dataList = response.body();

                for( UserData userData : dataList.list ){
                    Glide.with(getApplicationContext())
                            .load(userData.getUser_img())
                            .error(R.drawable.error)
                            .fallback(R.drawable.nullimage)
                            .into(myImage);

                    myId.setText(userData.getUser_nick_name());
                    myIntroduce.setText(userData.getUser_introduce());
                }
            }

            @Override
            public void onFailure(Call<UserData.UserDataResponse> call, Throwable t) {
                Log.d("마이페이지에서 내 정보 가져오기 ", "실패원인 : ", t);
            }
        });
    }
}
