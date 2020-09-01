package com.example.again;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Interest extends AppCompatActivity {
    private UserData.serviceApi serviceApi;
    private List<UserData> userDataInfo;
    private RecyclerView myInterestsRecyclerView;
    private MyInterestsRecyclerAdapter myInterestsRecyclerAdapter;

    UserData.UserDataResponse userDataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_interest);

        ImageButton back = findViewById(R.id.back);

        myInterestsRecyclerView = findViewById(R.id.myInterestRecycler);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
                finish();
            }
        });

        showUserInterests();
    }

    public void showUserInterests(){
        serviceApi = RetrofitClient.getClient().create(UserData.serviceApi.class);
        serviceApi.getMyInfo().enqueue(new Callback<UserData.UserDataResponse>() {
            @Override
            public void onResponse(Call<UserData.UserDataResponse> call, Response<UserData.UserDataResponse> response) {
                userDataResponse = response.body();
                if ( userDataResponse.getState() == 200){
                    userDataInfo = userDataResponse.list;

                    myInterestsRecyclerAdapter = new MyInterestsRecyclerAdapter(getApplicationContext(), userDataInfo);
                    myInterestsRecyclerView.setAdapter(myInterestsRecyclerAdapter);
                } else {
                    Log.d("모임 interests retrofit 에러", "서버에러");
                }
            }

            @Override
            public void onFailure(Call<UserData.UserDataResponse> call, Throwable t) {
                Log.e("모임 interests 에러 : ", "에러메시지 : " + t);
            }
        });
    }
}