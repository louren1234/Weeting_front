package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class SearchData {

    @SerializedName("search")
    String search;
    @SerializedName("location")
    String location;
    @SerializedName("meeting_name")
    String meeting_name;
    @SerializedName("meeting_location")
    String meeting_location;
    @SerializedName("meeting_time")
    String meeting_time;
    @SerializedName("meeting_recruitment")
    String meeting_recruitment;
    @SerializedName("meeting_img")
    String meeting_img;
    @SerializedName("meeting_id")
    int meeting_id;

    public SearchData(String location, int meeting_id, String meeting_name, String meeting_location, String meeting_time, String meeting_recruitment, String meeting_img) {
        this.location = location;
        this.meeting_id = meeting_id;
        this.meeting_name = meeting_name;
        this.meeting_location = meeting_location;
        this.meeting_time = meeting_time;
        this.meeting_recruitment = meeting_recruitment;
        this.meeting_img = meeting_img;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public String getMeeting_location() {
        return meeting_location;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public String getMeeting_recruitment() {
        return meeting_recruitment;
    }

    public String getMeeting_img() {
        return meeting_img;
    }




    public SearchData(String search, String location) {
        this.search = search;
        this.location = location;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    class searchResponse{
        int state;
        String message;
        List<SearchData> data;

        public int getState() {return state;}
        public String getMessage() {return message;}
    }

    public interface serviceApi{
        @POST("/searchbyLocation")
        Call<searchResponse> getSearchbyLocation(@Body SearchData data);

        @POST("/search")
        Call<searchResponse> getSearchEntire(@Field("search") String search);
    }
}
