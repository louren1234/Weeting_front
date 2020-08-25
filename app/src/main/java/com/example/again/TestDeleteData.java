package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TestDeleteData {
    @SerializedName("meeting_id")
    int meeting_id;

    public TestDeleteData(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    class MoimDetailDataResponse{
        int state;
        String message;

        public int getStatus() { return state; }
        public String getMessage() { return message; }
    }

    public interface serviceApi {
        @POST("/myWeetingDelete")
        Call<TestDeleteData.MoimDetailDataResponse> deleteMoim(@Body TestDeleteData data);
    }
}
