package com.example.again;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

// 모임 생성 데이터

public class MoimData {
    public String getMeeting_interest() {
        return meeting_interest;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public String getMeeting_description() {
        return meeting_description;
    }

//    public Date getMeeting_time() {
//        return meeting_time;
//    }

    public String getMeeting_time() { return meeting_time; }

    public String getMeeting_location() {
        return meeting_location;
    }

    public int getMeeting_recruitment() {
        return meeting_recruitment;
    }

    public int getAge_limit_min() {
        return age_limit_min;
    }

    public int getAge_limit_max() {
        return age_limit_max;
    }

    public void setMeeting_interest(String meeting_interest) {
        this.meeting_interest = meeting_interest;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public void setMeeting_description(String meeting_description) {
        this.meeting_description = meeting_description;
    }

//    public void setMeeting_time(Date meeting_time) {
//        this.meeting_time = meeting_time;
//    }
    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public void setMeeting_location(String meeting_location) {
        this.meeting_location = meeting_location;
    }

    public void setMeeting_recruitment(int meeting_recruitment) {
        this.meeting_recruitment = meeting_recruitment;
    }

    public void setAge_limit_min(int age_limit_min) {
        this.age_limit_min = age_limit_min;
    }

    public void setAge_limit_max(int age_limit_max) {
        this.age_limit_max = age_limit_max;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    @SerializedName("meeting_id")
    int meeting_id;
    @SerializedName("meeting_interest")
    String meeting_interest;
    @SerializedName("meeting_name")
    String meeting_name;
    @SerializedName("meeting_description")
    String meeting_description;
    @SerializedName("meeting_time")
//    Date meeting_time;
    String meeting_time;
    @SerializedName("meeting_location")
    String meeting_location;
    @SerializedName("meeting_recruitment")
    int meeting_recruitment;
    @SerializedName("age_limit_min")
    int age_limit_min;
    @SerializedName("age_limit_max")
    int age_limit_max;
    @SerializedName("meeting_img")
    Bitmap meeting_img;

    public void setMeeting_img(Bitmap meeting_img) {
        this.meeting_img = meeting_img;
    }

    public Bitmap getMeeting_img() {
        return meeting_img;
    }

    public MoimData(String meeting_interest, String meeting_name, String meeting_description, String meeting_time, String meeting_location,
                    int meeting_recruitment, int age_limit_min, int age_limit_max) {

        this.meeting_interest = meeting_interest;
        this.meeting_name = meeting_name;
        this.meeting_description = meeting_description;
        this.meeting_time = meeting_time;
        this.meeting_location = meeting_location;
        this.meeting_recruitment = meeting_recruitment;
        this.age_limit_min = age_limit_min;
        this.age_limit_max = age_limit_max;
//        this.meeting_img = meeting_img;
    }

    class MoimResponse{
        int state;
        String message;
        List<MoimData> data;

        public int getStatus() { return state; }
        public String getMessage() { return message; }
    }

    public interface ServiceApi{
        @Multipart //이거하면 에러남. 이거 해주고나면 데이터 넣을 때도 인코딩 해줘야하는 것 같음. 근데 데이터 잘 가니까 그거 어떻게 화면에 표시하게 할지가 고민. 그리고 데이터가 너무 길어서 짤려가니까 이것도 압축하는 방법이 필요할 것 같음.
        @POST("/create")
        Call<MoimResponse> createMoim(
//                @Body MoimData data
                @Part("meeting_interest") RequestBody meeting_interest,
                @Part("meeting_name") RequestBody meeting_name,
                @Part("meeting_description") RequestBody meeting_description,
                @Part("meeting_time") RequestBody meeting_time,
                @Part("meeting_location") RequestBody meeting_location,
                @Part("meeting_recruitment") RequestBody meeting_recruitment,
                @Part("age_limit_min") RequestBody age_limit_min,
                @Part("age_limit_max") RequestBody age_limit_max,
                @Part MultipartBody.Part meeting_img
                //@Part("image") RequestBody image // i have thried them both but they didnt work
                //@Part("image\"; filename=\"pp.jpg\" ") RequestBody image
        );

        @POST("/create")
        Call<MoimResponse> createImageNullMoim(@Body MoimData data);

        @GET("/fullCategory")
        Call<MoimResponse> getData();

        @GET("/weeting/{id}")
        Call<MoimResponse> getDetailMoim(@Path("id") int meeting_id);
    }

}

