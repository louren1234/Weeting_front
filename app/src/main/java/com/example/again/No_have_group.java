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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class No_have_group extends AppCompatActivity implements MainMoimRecyclerAdapter.OnMainMoimClickListener {
//    private ImageView firstRecommendMoim, secondRecommendMoim, firstMyMoim, secondMyMoim;

    private Context mcontext;

    MainMoimThumbnailData.serviceApi serviceApi;
    MainMoimThumbnailData.MaimMoimThumbnailDataResponse dataList;

    List<MainMoimThumbnailData> recommendMoim;

    RecyclerView recommendMoimRecyclerView;
    MainMoimRecyclerAdapter recommendMoimRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_have_group);

        TextView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);

        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMap = findViewById(R.id.toMap);
//        ImageButton toCalender = (ImageButton) findViewById(R.id.toCalender);
        ImageButton toMypage = findViewById(R.id.toMypage);

        recommendMoimRecyclerView = findViewById(R.id.noHaveMoimRecommendRecycler);
        recommendMoimRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

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
                Intent intent = new Intent(getApplicationContext(), SearchList.class);
                startActivity(intent);
            }
        });

        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                dataList = response.body();

                recommendMoim = dataList.getRecommend_mettings();
                recommendMoimRecyclerAdapter = new MainMoimRecyclerAdapter(getApplicationContext(), recommendMoim);
                mainrecommendrecyclerAdapterinit(recommendMoimRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> call, Throwable t) {

            }
        });


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
