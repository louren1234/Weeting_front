package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 카테고리 종류 가져오는 데이터

public class MoimCategoryData {

    @SerializedName("interests_name")
    String interests_name;

    @SerializedName("interests_id")
    int interests_id;

    public MoimCategoryData(String interests_name, int interests_id) {
        this.interests_name = interests_name;
        this.interests_id = interests_id;
    }

    public String getInterests_name() {
        return interests_name;
    }

    public void setInterests_name(String interests_name) {
        this.interests_name = interests_name;
    }

    public int getInterests_id() {
        return interests_id;
    }

    public void setInterests_id(int interests_id) {
        this.interests_id = interests_id;
    }

    class MoimCategoryResponse {
        int state;
        String message;
        List<MoimCategoryData> data;

        public int getState() {return state;}
        public String getMessage() {return message;}
    }

    public interface serviceApi{
        @GET("/fullCategory")
        Call<MoimCategoryResponse> getSpinnerList();

        @GET("/weeting/:{category}")
        Call<MoimCategoryResponse> getCategoryList(@Path("category") String category);
    }
}
