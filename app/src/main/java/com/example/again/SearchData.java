package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class SearchData {

    @SerializedName("search")
    String search;

    @SerializedName("location")
    String location;

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
    }
}
