package com.example.again;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMoim extends AppCompatActivity implements RecyclerAdapter.OnDataDetailClickListener {

    private MoimCategoryResultData.serviceApi serviceApi;
    private MoimCategoryResultData.MoimCategoryResultDataResponse dataList;
    private List<MoimCategoryResultData> dataInfo;

    RecyclerView myMoimRecyclerView;
    RecyclerAdapter myMoimRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_moim);

        TextView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMap = findViewById(R.id.toMap);
//        ImageButton toCalender = (ImageButton) findViewById(R.id.toCalender);
        ImageButton toMypage = findViewById(R.id.toMypage);

        myMoimRecyclerView = findViewById(R.id.myMoim);
        myMoimRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceApi = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);
        serviceApi.getMyMoim().enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {
                dataList = response.body();
                dataInfo = dataList.data;

                myMoimRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), dataInfo);
                myMoimRecyclerAdapterinit(myMoimRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

            }
        });

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
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

//        toCalender.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ToCalender.class);
//                startActivity(intent);
//            }
//        });

        toMypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

    }

    public void myMoimRecyclerAdapterinit(RecyclerAdapter myMoimRecyclerAdapter){
        myMoimRecyclerAdapter.setOnItemClicklistener(this); // onItemClick 함수 만들면 this 빨간줄 사라짐
        myMoimRecyclerView.setAdapter(myMoimRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, MoimCategoryResultData moimCategoryResultData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
        intent.putExtra("meetingId", moimCategoryResultData.getMeeting_id());
        startActivity(intent);
        Log.e("RecyclerVIew :: ", moimCategoryResultData.toString());
    }

    @Override
    protected void onResume(){
        super.onResume();

        serviceApi = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);
        serviceApi.getMyMoim().enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {
                dataList = response.body();
                dataInfo = dataList.data;

                myMoimRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), dataInfo);
                myMoimRecyclerAdapterinit(myMoimRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

            }
        });

    }
}
