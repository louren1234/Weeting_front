package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class MyMoimListData {
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
    @SerializedName("present_members")
    String present_members;

    public MyMoimListData(int meeting_id, String meeting_name, String meeting_img, String meeting_location, String meeting_time, String present_members) {
        this.meeting_id = meeting_id;
        this.meeting_name = meeting_name;
        this.meeting_img = meeting_img;
        this.meeting_location = meeting_location;
        this.meeting_time = meeting_time;
        this.present_members = present_members;
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

    public String getPresent_members() {
        return present_members;
    }

    public void setPresent_members(String present_members) {
        this.present_members = present_members;
    }

    class MyMoimListDataResponse {
        int state;
        String message;
        List<MyMoimListData> data;

        int getState(){ return state; }
        String getMessage(){ return message; }
        List<MyMoimListData> getData(){ return data; }
    }

    public interface serviceApi {
        @GET("/myWeeting")
        Call<MyMoimListDataResponse> getMyMoim();
    }
}
