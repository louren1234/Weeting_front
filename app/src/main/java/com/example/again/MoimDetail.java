package com.example.again;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimDetail extends AppCompatActivity {

    private TextView detailMoimName, detailMoimDescription, detailMoimLocation, detailMoimTime, detailMoimRecruitment, detailMoimAgeLimitMin, detailMoimAgeLimitMax, detailMoimCaptainNickname;
    private ImageView detailMoimImage;
    private Button detailMoimEditButton, detailMoimDeleteButton;
    private MoimDetailData.serviceApi serviceApi;
    MoimDetailData.MoimDetailDataResponse detailList;
    MoimDetailData detailListConponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moim_detail_layout);

        detailMoimName = findViewById(R.id.detailMoimName);
        detailMoimDescription = findViewById(R.id.detailMoimDescription);
        detailMoimLocation = findViewById(R.id.detailMoimLocation);
        detailMoimTime = findViewById(R.id.detailMoimTime);
        detailMoimRecruitment = findViewById(R.id.detailMoimRecruitment);
        detailMoimAgeLimitMin = findViewById(R.id.detailMoimAgeLimitMin);
        detailMoimAgeLimitMax = findViewById(R.id.detailMoimAgeLimitMax);
        detailMoimImage = findViewById(R.id.detailMoimImage);
        detailMoimEditButton = findViewById(R.id.detailMoimEditButton);
        detailMoimDeleteButton = findViewById(R.id.detailMoimDeleteButton);
        detailMoimCaptainNickname = findViewById(R.id.detailMoimCaptainNickname);

        Intent intent = getIntent();
        final int meeting_id = intent.getExtras().getInt("meetingId");

        Log.d("MoimDetail", "모임 아이디가 뭘까? : " + meeting_id);

        final MoimDetailData.serviceApi apiInterface = RetrofitClient.getClient().create(MoimDetailData.serviceApi.class);
        Call<MoimDetailData.MoimDetailDataResponse> call = apiInterface.getMoimDetail(meeting_id);

        call.enqueue(new Callback<MoimDetailData.MoimDetailDataResponse>() {
            @Override
            public void onResponse(Call<MoimDetailData.MoimDetailDataResponse> call, Response<MoimDetailData.MoimDetailDataResponse> response) {
                detailList = response.body();

                for(MoimDetailData moimDetailData : detailList.data){

                    Glide.with(getApplicationContext())
                            .load(moimDetailData.getMeeting_img())
                            .error(R.drawable.error)
                            .fallback(R.drawable.nullimage)
                            .into(detailMoimImage);

                    detailMoimName.setText(moimDetailData.getMeeting_name());
                    detailMoimDescription.setText(moimDetailData.getMeeting_description());
                    detailMoimLocation.setText(moimDetailData.getMeeting_location());
                    detailMoimTime.setText(String.valueOf(moimDetailData.getMeeting_time()));
                    detailMoimRecruitment.setText(String.valueOf(moimDetailData.getMeeting_recruitment()));
                    detailMoimAgeLimitMin.setText(String.valueOf(moimDetailData.getAge_limit_min()));
                    detailMoimAgeLimitMax.setText(String.valueOf(moimDetailData.getAge_limit_max()));
                    detailMoimCaptainNickname.setText(String.valueOf(moimDetailData.getCaptain_nick_name()));
                }

            }
            @Override
            public void onFailure(Call<MoimDetailData.MoimDetailDataResponse> call, Throwable t) {
                Toast.makeText(MoimDetail.this, "모임 디테일 에러", Toast.LENGTH_LONG).show();
                Log.e("모임 디테일 에러", t.getMessage());
                t.printStackTrace();
            }
        });

        detailMoimEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), UpdateMyMoim.class);
                intent.putExtra("meetingId", meeting_id);
                startActivity(intent);
            }
        });

        detailMoimDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                TestDeleteData data = new TestDeleteData(meeting_id);
                TestDeleteData.serviceApi testapiInterface = RetrofitClient.getClient().create(TestDeleteData.serviceApi.class);

                Call<TestDeleteData.MoimDetailDataResponse> calls = testapiInterface.deleteMoim(data);
//                Log.d("MoimDetail", "테스트" + meeting_id);
                calls.enqueue(new Callback<TestDeleteData.MoimDetailDataResponse>() {
                    @Override
                    public void onResponse(Call<TestDeleteData.MoimDetailDataResponse> call, Response<TestDeleteData.MoimDetailDataResponse> response) {
                        TestDeleteData.MoimDetailDataResponse result = response.body();
                        if(result.getStatus() == 200){
                            Intent intent = new Intent(getApplicationContext(), MoimList.class);
                            intent.putExtra("category", "all");
                            startActivity(intent);
                        }
                        else{
                            Log.d("MoimDetail", "서버에러");
                        }
                    }

                    @Override
                    public void onFailure(Call<TestDeleteData.MoimDetailDataResponse> call, Throwable t) {
                        Toast.makeText(MoimDetail.this, "모임 삭제 에러", Toast.LENGTH_LONG).show();
                        Log.e("모임 삭제 에러", t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}
