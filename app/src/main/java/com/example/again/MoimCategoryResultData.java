package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MoimCategoryResultData {

    @SerializedName("meeting_id")
    int meeting_id;
    @SerializedName("meeting_name")
    String meeting_name;
    @SerializedName("meeting_img")
    String meeting_img;
    @SerializedName("meeting_location")
    String meeting_location;
    @SerializedName("meeting_time")
    String meeting_time;
    @SerializedName("meeting_recruitment")
    int meeting_recruitment;

    public MoimCategoryResultData(int meeting_id, String meeting_name, String meeting_img, String meeting_location, String meeting_time, int meeting_recruitment) {
        this.meeting_id = meeting_id;
        this.meeting_name = meeting_name;
        this.meeting_img = meeting_img;
        this.meeting_location = meeting_location;
        this.meeting_time = meeting_time;
        this.meeting_recruitment = meeting_recruitment;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getMeeting_img() {
        return meeting_img;
    }

    public void setMeeting_img(String meeting_img) {
        this.meeting_img = meeting_img;
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

    class MoimCategoryResultDataResponse{
        int state;
        String message;
        List<MoimCategoryResultData> data;

        public int getStatus() {return state;}
        public String getMessage() {return message;}
    }

    public interface serviceApi {
        @GET("/weetings")
        Call<MoimCategoryResultDataResponse> getAllMoim();

        @GET("weetings/{category}")
        Call<MoimCategoryResultDataResponse> getCategoryResultMoim(@Path("category") String category);

        @GET("/myWeeting")
        Call<MoimCategoryResultDataResponse> getMyMoim();
    }
}
