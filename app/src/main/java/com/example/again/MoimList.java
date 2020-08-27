package com.example.again;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimList extends AppCompatActivity implements RecyclerAdapter.OnDataDetailClickListener, InterestListRecyclerAdapter.OnCategoryListClickListener {
    private MoimCategoryData.serviceApi serviceApi;
    private Context mcontext;

    MoimCategoryResultData.MoimCategoryResultDataResponse moimDataList;
    MoimCategoryData.MoimCategoryResponse categoryDataList;

    List<MoimCategoryResultData> moimDataInfo;
    List<MoimCategoryData> categoryDataInfo;

    RecyclerView moimRecyclerView, categoryRecyclerView;
    RecyclerAdapter moimRecyclerAdapter;
    InterestListRecyclerAdapter categoryRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moimlist);

        serviceApi = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);

        ImageView create = findViewById(R.id.toMap);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });

        moimRecyclerView = findViewById(R.id.recyclerView); // 이렇게 해야됨 1
        categoryRecyclerView = findViewById(R.id.category);

        moimRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 이렇게 해야됨 2
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mcontext = this;

        TextView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);

        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMap = findViewById(R.id.toMap);
//        ImageButton toCalender = (ImageButton) findViewById(R.id.toCalender);
        ImageButton toMypage = findViewById(R.id.toMypage);

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

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this); 이렇게 하면 안됨. 1 내 생각엔 this를 못 읽어오는 것 같음. 직접 레이아웃 지정해주는게 맞는듯. 아,
//        recyclerView.setLayoutManager(layoutManager); 이렇게 하면 안됨. 2

        Intent intent = getIntent();
        final String categoryName = intent.getExtras().getString("category");

        MoimCategoryResultData.serviceApi moimApiInterface = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);

        if (categoryName.equals("all")) {
            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getAllMoim();
//            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getAllMoim();
            moimCall.enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                @Override
                public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {

                    moimDataList = response.body();

                    Log.d("MoimList 성공", moimDataList.toString());

                    moimDataInfo = moimDataList.data;

                    moimRecyclerAdapter = new RecyclerAdapter(mcontext, moimDataInfo);
                    recyclerAdapterinit(moimRecyclerAdapter);
                }

                @Override
                public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

                    Log.d("MoimList 실패", t.toString());
                }
            });
        }
        else {
            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getCategoryResultMoim(categoryName);
//            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getAllMoim();
            moimCall.enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                @Override
                public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {

                    moimDataList = response.body();

                    Log.d("MoimList 성공", moimDataList.toString());

                    moimDataInfo = moimDataList.data;

                    moimRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), moimDataInfo);
                    recyclerAdapterinit(moimRecyclerAdapter);
                }

                @Override
                public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

                    Log.d("MoimList 실패", t.toString());
                }
            });
        }


        // 카테고리

        MoimCategoryData.serviceApi categoryApiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
        Call<MoimCategoryData.MoimCategoryResponse> categoryCall = categoryApiInterface.getSpinnerList();

        categoryCall.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {

                categoryDataList = response.body();

                Log.d("카테고리 불러오기 성공", categoryDataList.toString());

                categoryDataInfo = categoryDataList.data;

                categoryRecyclerAdapter = new InterestListRecyclerAdapter(getApplicationContext(), categoryDataInfo);
                InterestListRecyclerAdapterinit(categoryRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {

                Log.d("카테고리 불러오기 실패", t.toString());
            }
        });
    }

    //모임 리스트 클릭 시 setting
    public void recyclerAdapterinit(RecyclerAdapter moimRecyclerAdapter){
        moimRecyclerAdapter.setOnItemClicklistener(this);
        moimRecyclerView.setAdapter(moimRecyclerAdapter);
    }

    //모임 카테고리 클릭 시 setting
    public void InterestListRecyclerAdapterinit(InterestListRecyclerAdapter categoryRecyclerAdapter){
        categoryRecyclerAdapter.setOnItemClicklistener(this);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
    }

    // 모임 리스트 클릭 시
    @Override
    public void onItemClick(View view, MoimCategoryResultData moimCategoryResultData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
        intent.putExtra("meetingId", moimCategoryResultData.getMeeting_id());
        startActivity(intent);
        Log.e("RecyclerVIew :: ", moimCategoryResultData.toString());
    }

    //카테고리 클릭 시
    @Override
    public void onCategoryClick(View view, MoimCategoryData moimCategoryData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Fragment fragment = new Fragment();
        Intent intent = new Intent(getApplicationContext(), MoimList.class);
        intent.putExtra("category", moimCategoryData.getInterests_name());
        startActivity(intent);
        Log.e("RecyclerVIew :: ", moimCategoryData.toString());
    }

}

