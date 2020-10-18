package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class UserData {
    @SerializedName("user_nick_name")
    String user_nick_name;
    @SerializedName("user_img")
    String user_img;
    @SerializedName("user_introduce")
    String user_introduce;
    @SerializedName("user_interests")
    String user_interests;
    @SerializedName("user_birth")
    String user_birth;
    @SerializedName("user_email")
    String user_email;

//    public UserData(String user_nick_name, String user_img, String user_introduce, String user_interests, String user_birth, String user_email) {
//        this.user_nick_name = user_nick_name;
//        this.user_img = user_img;
//        this.user_introduce = user_introduce;
//        this.user_interests = user_interests;
//        this.user_birth = user_birth;
//        this.user_email = user_email;
//    }

    public UserData(String user_img){
        this.user_img = user_img;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public void setUser_nick_name(String user_nick_name) {
        this.user_nick_name = user_nick_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_introduce() {
        return user_introduce;
    }

    public void setUser_introduce(String user_introduce) {
        this.user_introduce = user_introduce;
    }

    public String getUser_interests() {
        return user_interests;
    }

    public void setUser_interests(String user_interests) {
        this.user_interests = user_interests;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    class UserDataResponse {
        int state;
        String message;
        List<UserData> list;

        int getState(){ return state; }
        String getMessage(){ return message; }
        List<UserData> getList(){ return list; }
    }

    class UserImgorIntroorInterestsResponse {
        int state;
        String message;

        int getState(){ return state; }
        String getMessage(){ return message; }
    }

    public interface serviceApi {
        @GET("/mypage")
        Call<UserDataResponse> getMyInfo();

        @POST("/mypage/edit/interests")
        Call<UserImgorIntroorInterestsResponse> updateMyInterests(@Query("user_interests") String user_interests);

        @POST("/mypage/edit/img")
        Call<UserImgorIntroorInterestsResponse> updateMyImg(@Query("user_img") String user_img);

//        @FormUrlEncoded
        @Multipart
        @POST("/mypage/edit/img")
        Call<UserImgorIntroorInterestsResponse> updateMyNewImg(@Part MultipartBody.Part user_img);

//        @FormUrlEncoded
//        @POST("/mypage/edit/introduce")
//        Call<UserImgorIntroResponse> updateMyIntro(@Field("user_introduce") String user_introduce);
        @FormUrlEncoded
        @POST("/mypage/edit/introduce")
        Call<UserImgorIntroorInterestsResponse> updateMyIntro(@Field("user_introduce") String user_introduce);
    }
}
