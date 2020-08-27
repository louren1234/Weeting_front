package com.example.again;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class After_have_group extends AppCompatActivity implements MainMoimRecyclerAdapter.OnMainMoimClickListener {
//    private ImageView firstRecommendMoim, secondRecommendMoim, firstMyMoim, secondMyMoim;

    private Context mcontext;

    MainMoimThumbnailData.serviceApi serviceApi;
    MainMoimThumbnailData.MaimMoimThumbnailDataResponse dataList;

    MainMoimThumbnailData.MaimMoimThumbnailDataResponse myMoimList;
    List<MainMoimThumbnailData> myMoim;
    List<MainMoimThumbnailData> recommendMoim;

    RecyclerView myMoimRecyclerView, recommendMoimRecyclerView;
    MainMoimRecyclerAdapter myMoimRecyclerAdapter, recommendMoimRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_have_group);

        TextView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);

        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMap = findViewById(R.id.toMap);
//        ImageButton toCalender = (ImageButton) findViewById(R.id.toCalender);
        ImageButton toMypage = findViewById(R.id.toMypage);

        myMoimRecyclerView = findViewById(R.id.mainMyRecycler); // 이렇게 해야됨 1
        myMoimRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 이렇게 해야됨 2

        recommendMoimRecyclerView = findViewById(R.id.mainRecommedRecycler);
        recommendMoimRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceApi = RetrofitClient.getClient().create(MainMoimThumbnailData.serviceApi.class);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), No_have_group.class);
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

        toMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
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

        serviceApi.getAfterHaveMoimMain().enqueue(new Callback<MainMoimThumbnailData.MaimMoimThumbnailDataResponse>() {
            @Override
            public void onResponse(Call<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> call, Response<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> response) {

                    myMoimList = response.body();

                    myMoim = myMoimList.getMy_meetings();
                    myMoimRecyclerAdapter = new MainMoimRecyclerAdapter(mcontext, myMoim);
                    mainMoimrecyclerAdapterinit(myMoimRecyclerAdapter);

                    recommendMoim = myMoimList.getRecommend_mettings();
                    recommendMoimRecyclerAdapter = new MainMoimRecyclerAdapter(mcontext, recommendMoim);
                    mainrecommendrecyclerAdapterinit(recommendMoimRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> call, Throwable t) {
                Log.d("MainMoim : ", "메인 모임 오류 : " + t);
            }
        });


    }

    public void mainMoimrecyclerAdapterinit(MainMoimRecyclerAdapter mainMoimRecyclerAdapter){
        mainMoimRecyclerAdapter.setOnItemClicklistener(this);
        myMoimRecyclerView.setAdapter(mainMoimRecyclerAdapter);
    }

    public void mainrecommendrecyclerAdapterinit(MainMoimRecyclerAdapter mainMoimRecyclerAdapter){
        mainMoimRecyclerAdapter.setOnItemClicklistener(this);
        recommendMoimRecyclerView.setAdapter(mainMoimRecyclerAdapter);
    }

    @Override
    public void onMainThumbnailClick(View view, MainMoimThumbnailData mainMoimThumbnailData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
        intent.putExtra("meetingId", mainMoimThumbnailData.getMeeting_id());
        startActivity(intent);
        Log.e("RecyclerVIew :: ", mainMoimThumbnailData.toString());
    }
}
