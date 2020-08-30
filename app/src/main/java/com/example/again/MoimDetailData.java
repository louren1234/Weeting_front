package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

// 모임 클릭했을 때 상세 페이지 데이터

public class MoimDetailData {

    @SerializedName("fk_meeting_interest")
    int fk_meeting_interest;
    @SerializedName("meeting_name")
    String meeting_name;
    @SerializedName("meeting_description")
    String meeting_description;
    @SerializedName("meeting_location")
    String meeting_location;
    @SerializedName("meeting_time")
    String meeting_time;
    @SerializedName("meeting_recruitment")
    int meeting_recruitment;
    @SerializedName("age_limit_min")
    int age_limit_min;
    @SerializedName("age_limit_max")
    int age_limit_max;
    @SerializedName("meeting_img")
    String meeting_img;
    @SerializedName("present_members")
    int present_members;
    @SerializedName("captain_nick_name")
    String captain_nick_name;
    // meeting_members
    @SerializedName("user_nick_name")
    String user_nick_name;
    @SerializedName("user_img")
    String user_img;
    @SerializedName("user_introduce")
    String user_introduce;

    public MoimDetailData(int fk_meeting_interest, String meeting_name, String meeting_description, String meeting_location, String meeting_time,
                          int meeting_recruitment, int age_limit_min, int age_limit_max, String meeting_img, int present_members, String captain_nick_name) {
        this.fk_meeting_interest = fk_meeting_interest;
        this.meeting_name = meeting_name;
        this.meeting_description = meeting_description;
        this.meeting_location = meeting_location;
        this.meeting_time = meeting_time;
        this.meeting_recruitment = meeting_recruitment;
        this.age_limit_min = age_limit_min;
        this.age_limit_max = age_limit_max;
        this.meeting_img = meeting_img;
        this.present_members = present_members;
        this.captain_nick_name = captain_nick_name;
    }

    public MoimDetailData(String user_nick_name, String user_img, String user_introduce) {
        this.user_nick_name = user_nick_name;
        this.user_img = user_img;
        this.user_introduce = user_introduce;
    }

    public int getFk_meeting_interest() {
        return fk_meeting_interest;
    }

    public void setFk_meeting_interest(int fk_meeting_interest) {
        this.fk_meeting_interest = fk_meeting_interest;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getMeeting_description() {
        return meeting_description;
    }

    public void setMeeting_description(String meeting_description) {
        this.meeting_description = meeting_description;
    }

    public String getMeeting_location() {
        return meeting_location;
    }

    public void setMeeting_location(String meeting_location) {
        this.meeting_location = meeting_location;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public int getMeeting_recruitment() {
        return meeting_recruitment;
    }

    public void setMeeting_recruitment(int meeting_recruitment) {
        this.meeting_recruitment = meeting_recruitment;
    }

    public int getAge_limit_min() {
        return age_limit_min;
    }

    public void setAge_limit_min(int age_limit_min) {
        this.age_limit_min = age_limit_min;
    }

    public int getAge_limit_max() {
        return age_limit_max;
    }

    public void setAge_limit_max(int age_limit_max) {
        this.age_limit_max = age_limit_max;
    }

    public String getMeeting_img() {
        return meeting_img;
    }

    public void setMeeting_img(String meeting_img) {
        this.meeting_img = meeting_img;
    }

    public int getPresent_members() {
        return present_members;
    }

    public void setPresent_members(int present_members) {
        this.present_members = present_members;
    }

    public String getCaptain_nick_name() {
        return captain_nick_name;
    }

    public void setCaptain_nick_name(String captain_nick_name) {
        this.captain_nick_name = captain_nick_name;
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

    class MoimDetailDataResponse{
        int state;
        String message;
        int is_member;
        String meeting_interest;
        List<MoimDetailData> data;
        List<MoimDetailData> meeting_members;

        public int getStatus() { return state; }
        public String getMessage() { return message; }
        public List<MoimDetailData> getData() { return data; }
        public List<MoimDetailData> getMeeting_members() { return meeting_members; }

        public int getIs_member() { return is_member; }
        public String getMeeting_interest() { return meeting_interest; }
    }

    public interface serviceApi {
        @GET("/weetingDetail/{meeting_id}")
        Call<MoimDetailDataResponse> getMoimDetail(@Path("meeting_id") int meeting_id);

        @FormUrlEncoded
        @POST("/participate")
        Call<MoimDetailDataResponse> participateMoim(@Field("meeting_id") int meeting_id);

        @FormUrlEncoded
        @POST("/withdraw")
        Call<MoimDetailDataResponse> withdrawMoim(@Field("meeting_id") int meeting_id);

//        @POST("/myWeetingUpdate")
//        Call<MoimDetailDataResponse> updateMoim();
//
//        @Multipart
//        @POST("/myWeetingDelete")
//        Call<MoimDetailDataResponse> deleteMoim(@Path("meeting_id") RequestBody meeting_id);
    }
}
