package com.example.again;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout noHaveMoimLayout;
    private LinearLayout haveMoim;
    private Button myMoimButton;
    private Button makeGroup, createMoim;

    private Context mcontext;
    private Context recommendedcontext;

    MoimCategoryResultData.serviceApi checkMoimServiceApi;
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

        ImageView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMypage = findViewById(R.id.toMypage);

//        LinearLayout noHaveMoimLayout = findViewById(R.id.noHaveMoimLayout);

        myMoimButton = findViewById(R.id.myMoimButton);
        makeGroup = findViewById(R.id.makeGroup);

        noHaveMoimLayout = findViewById(R.id.noHaveMoimLayout);
        haveMoim = findViewById(R.id.mainMyMoim);

        myMoimRecyclerView = findViewById(R.id.mainMyRecycler); // 이렇게 해야됨 1
        myMoimRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // 이렇게 해야됨 2

        recommendMoimRecyclerView = findViewById(R.id.mainRecommedRecycler);
        recommendMoimRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        serviceApi = RetrofitClient.getClient().create(MainMoimThumbnailData.serviceApi.class);
        checkMoimServiceApi = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);

        createMoim = findViewById(R.id.createMoim);

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

        toMypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        myMoimButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyMoim.class);
                startActivity(intent);
            }
        });

        makeGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });

        createMoim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });

        serviceApi.getAfterHaveMoimMain().enqueue(new Callback<MainMoimThumbnailData.MaimMoimThumbnailDataResponse>() {
            @Override
            public void onResponse(Call<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> call, Response<MainMoimThumbnailData.MaimMoimThumbnailDataResponse> response) {

                myMoimList = response.body();

                recommendMoim = myMoimList.getRecommend_mettings();
                recommendMoimRecyclerAdapter = new MainMoimRecyclerAdapter(getApplicationContext(), recommendMoim);
                mainrecommendrecyclerAdapterinit(recommendMoimRecyclerAdapter);

                checkMoimServiceApi.getMyMoim().enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                    @Override
                    public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {
                        MoimCategoryResultData.MoimCategoryResultDataResponse data = response.body();
                        if(data.data != null){
                            if (data.data.size() > 0){
                                noHaveMoimLayout.setVisibility(View.GONE);
                                myMoim = myMoimList.getMy_meetings();
                                myMoimRecyclerAdapter = new MainMoimRecyclerAdapter(getApplicationContext(), myMoim);
                                mainMoimrecyclerAdapterinit(myMoimRecyclerAdapter);
                            }
                        }
                        else if (data.data == null) {
                            haveMoim.setVisibility(View.GONE);
                            myMoimButton.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {
                        Log.e("내 모임 불러오기 에러 발생", t.getMessage());
                    }
                });

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

    public void mainrecommendrecyclerAdapterinit(MainMoimRecyclerAdapter recommendMoimRecyclerAdapter){
        recommendMoimRecyclerAdapter.setOnItemClicklistener(this);
        recommendMoimRecyclerView.setAdapter(recommendMoimRecyclerAdapter);
    }

    @Override
    public void onMainThumbnailClick(View view, MainMoimThumbnailData mainMoimThumbnailData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
        intent.putExtra("meetingId", mainMoimThumbnailData.getMeeting_id());
        startActivity(intent);
//        Log.e("RecyclerVIew :: ", mainMoimThumbnailData.toString());
    }

}
