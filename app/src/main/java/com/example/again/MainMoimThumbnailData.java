package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class MainMoimThumbnailData {
    @SerializedName("meeting_id")
    int meeting_id;
    @SerializedName("meeting_name")
    String meeting_name;
    @SerializedName("meeting_img")
    String meeting_img;

    public MainMoimThumbnailData(int meeting_id, String meeting_name, String meeting_img) {
        this.meeting_id = meeting_id;
        this.meeting_name = meeting_name;
        this.meeting_img = meeting_img;
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

    class MaimMoimThumbnailDataResponse {
        int status;
        String message;
        List<MainMoimThumbnailData> recommend_meetings;
        List<MainMoimThumbnailData> my_meetings;

        int getStatus(){ return status; }
        String getMessage(){ return message; }
        List<MainMoimThumbnailData> getRecommend_mettings(){ return recommend_meetings; }
        List<MainMoimThumbnailData> getMy_meetings(){ return my_meetings; }
    }

    public interface serviceApi {
        @GET("/main")
        Call<MaimMoimThumbnailDataResponse> getAfterHaveMoimMain();
    }
}
