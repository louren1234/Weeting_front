package com.example.again;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface serviceApi {
    @POST("/searchbyLocation")
    Call<SearchData.searchResponse> getSearchbyLocation(@Body SearchData data);

    @GET("/fullCategory")
    Call<MoimCategoryData.MoimCategoryResponse> getSpinnerList();

    //로그인 및 회원가입
    @POST("/login/join")
    Call<SignUpData.SignUpResponse> userSignUP(@Body SignUpData data);

    @POST("/login/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    //Qna
    @POST("/mypage/contact")
    Call<QnaData.QnaResponse> createQna(@Body QnaData data);

    //모임 데이터
    @POST("/create")
    Call<MoimData.MoimResponse> createMoim(@Body MoimData data);

    @GET("/fullCategory")
    Call<MoimData.MoimResponse> getData();


}
