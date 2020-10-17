package com.example.again;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    private String getMyInterests;
    private String[] sliceMyInterests;

    private TextView myInterests;
    private Button editMyInterestsButton;

    UserData.UserDataResponse userDataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_interest);

        ImageButton back = findViewById(R.id.back);

        myInterestsRecyclerView = findViewById(R.id.myInterestRecycler);
//        myInterests = findViewById(R.id.myInterests);
        editMyInterestsButton = findViewById(R.id.editMyInterestsButton);

        myInterestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
                finish();
            }
        });

        editMyInterestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMyInterest.class);
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

                    for(UserData userData : userDataInfo) {
                        getMyInterests = userData.getUser_interests();
                        sliceMyInterests = getMyInterests.split("/");

//                        for(int i = 0; i < sliceMyInterests.length; i++){
//                            Log.d("split 테스트 : ", "잘 나오나?" + sliceMyInterests[i]);
//                        }

                        myInterestsRecyclerAdapter = new MyInterestsRecyclerAdapter(getApplicationContext(), sliceMyInterests);
                        myInterestsRecyclerView.setAdapter(myInterestsRecyclerAdapter);
                    }

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