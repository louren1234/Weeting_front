//package com.example.again;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MoimList_test extends AppCompatActivity {
//    private MoimCategoryData.serviceApi serviceApi;
//
//    MoimData.MoimResponse moimDataList;
//    MoimCategoryData.MoimCategoryResponse categoryDataList;
//
//    List<MoimData> moimDataInfo;
//    List<MoimCategoryData> categoryDataInfo;
//
//    RecyclerView moimRecyclerView, categoryRecyclerView;
//    RecyclerAdapter moimRecyclerAdapter;
//    InterestListRecyclerAdapter categoryRecyclerAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.moimlist);
//
//        serviceApi = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
//
//        ImageView create = findViewById(R.id.toMap);
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Create.class);
//                startActivity(intent);
//            }
//        });
//
//        moimRecyclerView = findViewById(R.id.recyclerView); // 이렇게 해야됨 1
//        categoryRecyclerView = findViewById(R.id.category);
//
//        moimRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 이렇게 해야됨 2
//        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//
////        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this); 이렇게 하면 안됨. 1 내 생각엔 this를 못 읽어오는 것 같음. 직접 레이아웃 지정해주는게 맞는듯. 아,
////        recyclerView.setLayoutManager(layoutManager); 이렇게 하면 안됨. 2
//
//        MoimData.ServiceApi moimApiInterface = RetrofitClient.getClient().create(MoimData.ServiceApi.class);
//        Call<MoimData.MoimResponse> moimCall = moimApiInterface.getData();
//
//        MoimCategoryData.serviceApi categoryApiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
//        Call<MoimCategoryData.MoimCategoryResponse> categoryCall = categoryApiInterface.getSpinnerList();
//
//        moimCall.enqueue(new Callback<MoimData.MoimResponse>() {
//            @Override
//            public void onResponse(Call<MoimData.MoimResponse> call, Response<MoimData.MoimResponse> response) {
//
//                moimDataList = response.body();
//
//                Log.d("MoimList 성공", moimDataList.toString());
//
//                moimDataInfo = moimDataList.data;
//
//                moimRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), moimDataInfo);
//                moimRecyclerView.setAdapter(moimRecyclerAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<MoimData.MoimResponse> call, Throwable t) {
//
//                Log.d("MoimList 실패", t.toString());
//            }
//        });
//
//        categoryCall.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
//            @Override
//            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {
//
//                categoryDataList = response.body();
//
//                Log.d("MoimList 성공", categoryDataList.toString());
//
//                categoryDataInfo = categoryDataList.data;
//
//                categoryRecyclerAdapter = new InterestListRecyclerAdapter(getApplicationContext(), categoryDataInfo);
//                categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {
//
//                Log.d("MoimList 실패", t.toString());
//            }
//        });
//    }
//
//    public void toGetCategoryMoim(View v) {
//
//        String selectedCategory = categoryRecyclerView.toString();
//
//        serviceApi.getCategoryList(selectedCategory).enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>(){
//            @Override
//            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {
//                MoimCategoryData.MoimCategoryResponse result = response.body();
//
//                Toast.makeText(MoimList_test.this, result.getMessage(), Toast.LENGTH_LONG).show();
//
//                if(result.getState() == 200) {
//                    System.out.println("yeah");
//                    finish();
//                }
//                else if(result.getState() == 400) {
//                    System.out.println("nooooo");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {
//                Toast.makeText(MoimList_test.this, "모임생성 에러", Toast.LENGTH_LONG).show();
//                Log.e("모임생성 에러", t.getMessage());
//                t.printStackTrace();
//            }
//        });
//
//    }
//
//}
//
//// 코드 백업시키고 MoimList에서 실험할 예정