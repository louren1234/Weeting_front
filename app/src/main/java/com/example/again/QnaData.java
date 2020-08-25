package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class QnaData {
    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @SerializedName("content")
    String content;

    public QnaData(String content) {
        this.content = content;
    }

    class QnaResponse{
        int state;
        String message;
        List<QnaData> body;

        public int getStatus(){ return state; }
        public String getMessage(){ return message; }
    }

    public interface serviceApi{
        @POST("/mypage/contact")
        Call<QnaResponse> createQna(@Body QnaData data);
    }
}
