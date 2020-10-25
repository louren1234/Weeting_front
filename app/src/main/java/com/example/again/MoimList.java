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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimList extends FragmentActivity implements InterestListRecyclerAdapter.OnCategoryListClickListener {
    private MoimCategoryData.serviceApi serviceApi;
    private Context mcontext;
    private String categoryName;
    private MoimListFragment fragment;

    MoimCategoryData.MoimCategoryResponse categoryDataList;

    List<MoimCategoryData> categoryDataInfo;

    RecyclerView moimRecyclerView, categoryRecyclerView;
    InterestListRecyclerAdapter categoryRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moimlist);

        serviceApi = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);

        categoryRecyclerView = findViewById(R.id.category);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mcontext = this;

        ImageView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMypage = findViewById(R.id.toMypage);

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

        Intent intent = getIntent();
        categoryName = intent.getExtras().getString("category");

        if(fragment == null){
            fragment = new MoimListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", categoryName);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }


        // 카테고리

        MoimCategoryData.serviceApi categoryApiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
        Call<MoimCategoryData.MoimCategoryResponse> categoryCall = categoryApiInterface.getSpinnerList();

        categoryCall.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {

                categoryDataList = response.body();

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

    //모임 카테고리 클릭 시 setting
    public void InterestListRecyclerAdapterinit(InterestListRecyclerAdapter categoryRecyclerAdapter){
        categoryRecyclerAdapter.setOnItemClicklistener(this);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
    }

    //카테고리 클릭 시
    @Override
    public void onCategoryClick(View view, MoimCategoryData moimCategoryData){

        if(fragment == null) {
            fragment = new MoimListFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("category", moimCategoryData.getInterests_name());
        fragment.setArguments(bundle);

        fragment.refreshFragment();
    }
}

