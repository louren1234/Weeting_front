package com.example.again;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimDetail extends AppCompatActivity {

    private TextView detailMoimName, detailMoimDescription, detailMoimLocation, detailMoimTime, detailMoimRecruitment, detailMoimAgeLimitMin, detailMoimAgeLimitMax, detailMoimPresentMembersNum, detailMoimCaptainNickname;
    private ImageView detailMoimImage;
    private LinearLayout memberLayout;
    private Button  moimParticipateButton, chatButton;
    private TextView detailMoimDeleteButton, moimWithdrawButton, detailMoimEditButton;
    private MoimDetailData.serviceApi serviceApi;
    private int is_member;
    private int is_captain;

    private MoimDetailData.MoimDetailDataResponse detailList;
    private MoimDetailData detailListConponent;

    List<MoimDetailData> moimMemberInfo;
    RecyclerView moimMemberRecyclerView;
    MoimMemberRecyclerAdapter moimMemberRecyclerAdapter;

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
        moimParticipateButton = findViewById(R.id.moimParticipateButton);
        moimWithdrawButton = findViewById(R.id.moimWithDrawButton);
        detailMoimPresentMembersNum = findViewById(R.id.detailMoimPresentMembersNum);
        detailMoimCaptainNickname = findViewById(R.id.detailMoimCaptainNickname);

        memberLayout = findViewById(R.id.meetingMemberLayout);
        moimMemberRecyclerView = findViewById(R.id.meetingMemberRecyclerView);

        moimMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //chat
        chatButton = findViewById(R.id.chatting_in);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
            }
        });
        Intent intent = getIntent();
        final int meeting_id = intent.getExtras().getInt("meetingId");

        Log.d("MoimDetail", "모임 아이디가 뭘까? : " + meeting_id);

        serviceApi = RetrofitClient.getClient().create(MoimDetailData.serviceApi.class);
        Call<MoimDetailData.MoimDetailDataResponse> call = serviceApi.getMoimDetail(meeting_id);

        call.enqueue(new Callback<MoimDetailData.MoimDetailDataResponse>() {
            @Override
            public void onResponse(Call<MoimDetailData.MoimDetailDataResponse> call, Response<MoimDetailData.MoimDetailDataResponse> response) {
                detailList = response.body();

                moimMemberInfo = detailList.getMeeting_members();
                moimMemberRecyclerAdapter = new MoimMemberRecyclerAdapter(getApplicationContext(), moimMemberInfo);
                moimMemberRecyclerView.setAdapter(moimMemberRecyclerAdapter);

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
                    detailMoimPresentMembersNum.setText(String.valueOf(moimDetailData.getPresent_members()));
                    detailMoimCaptainNickname.setText(String.valueOf(moimDetailData.getCaptain_nick_name()));
                }

                is_member = detailList.is_member;
                is_captain = detailList.is_captain;

                if(is_member != 1) {
                    memberLayout.setVisibility(View.GONE);
                    moimWithdrawButton.setVisibility(View.GONE);
                    detailMoimDeleteButton.setVisibility(View.GONE);
                    detailMoimEditButton.setVisibility(View.GONE);
                  
                }else if(is_member == 1){
                    moimParticipateButton.setVisibility(View.GONE);
                    if(is_captain != 1) {
                        detailMoimEditButton.setVisibility(View.GONE);
                        detailMoimDeleteButton.setVisibility(View.GONE);
                    } else if (is_captain == 1) {
                        moimWithdrawButton.setVisibility(View.GONE);
                    }
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
                finish();
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
                            finish();
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

        moimParticipateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                serviceApi.participateMoim(meeting_id).enqueue(new Callback<MoimDetailData.MoimDetailDataResponse>() {
                    @Override
                    public void onResponse(Call<MoimDetailData.MoimDetailDataResponse> call, Response<MoimDetailData.MoimDetailDataResponse> response) {
                        MoimDetailData.MoimDetailDataResponse response1 = response.body();

                        if ( response1.getStatus() == 200 ){

                            Toast myToast = Toast.makeText(getApplicationContext(),"모임 참여 완료", Toast.LENGTH_SHORT);
                            myToast.show();
                            Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
                            intent.putExtra("meetingId", meeting_id);
                            startActivity(intent);
                            finish();

                        } else{
                            Log.d("모임 참여 에러 : ", " 서버 에러 ");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoimDetailData.MoimDetailDataResponse> call, Throwable t) {
                        Log.d("모임 참여 에러 : ", " retrofit 에러 ");
                    }
                });
            }
        });

        moimWithdrawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                serviceApi.withdrawMoim(meeting_id).enqueue(new Callback<MoimDetailData.MoimDetailDataResponse>() {
                    @Override
                    public void onResponse(Call<MoimDetailData.MoimDetailDataResponse> call, Response<MoimDetailData.MoimDetailDataResponse> response) {
                        MoimDetailData.MoimDetailDataResponse response1 = response.body();

                        if ( response1.getStatus() == 200 ){

                            Toast myToast = Toast.makeText(getApplicationContext(),"모임 탈퇴 완료", Toast.LENGTH_SHORT);
                            myToast.show();
                            Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
                            intent.putExtra("meetingId", meeting_id);
                            startActivity(intent);
                            finish();

                        } else{
                            Log.d("모임 참여 에러 : ", " 서버 에러 ");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoimDetailData.MoimDetailDataResponse> call, Throwable t) {
                        Log.d("모임 참여 에러 : ", " retrofit 에러 ");
                    }
                });
            }
        });

    }

//    public void MoimMemberRecyclerAdapterinit(MoimMemberRecyclerAdapter moimMemberRecyclerAdapter){
//        moimMemberRecyclerAdapter
//    }
}
