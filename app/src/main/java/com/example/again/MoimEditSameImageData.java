package com.example.again;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class MoimEditSameImageData {
    @SerializedName("meeting_interest")
    String meeting_interest;
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
    @SerializedName("meeting_id")
    int meeting_id;

    public MoimEditSameImageData(String meeting_interest, String meeting_name, String meeting_description, String meeting_location, String meeting_time, int meeting_recruitment, int age_limit_min, int age_limit_max, int meeting_id) {
        this.meeting_interest = meeting_interest;
        this.meeting_name = meeting_name;
        this.meeting_description = meeting_description;
        this.meeting_location = meeting_location;
        this.meeting_time = meeting_time;
        this.meeting_recruitment = meeting_recruitment;
        this.age_limit_min = age_limit_min;
        this.age_limit_max = age_limit_max;
        this.meeting_id = meeting_id;
    }

    public String getMeeting_interest() {
        return meeting_interest;
    }

    public void setMeeting_interest(String meeting_interest) {
        this.meeting_interest = meeting_interest;
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

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    class MoimEditSameImageDataResponse{
        int state;
        String message;
        List<MoimEditSameImageData> data;

        int getState(){ return state; }
        String getMessage(){ return message; }
    }

    public interface serviceApi{
        @Multipart
        @POST("/myWeetingUpdate")
        Call<MoimEditSameImageData.MoimEditSameImageDataResponse> editSameImgMoim(@Part("meeting_interest") RequestBody meeting_interest,
                                                                                  @Part("meeting_name") RequestBody meeting_name,
                                                                                  @Part("meeting_description") RequestBody meeting_description,
                                                                                  @Part("meeting_location") RequestBody meeting_location,
                                                                                  @Part("meeting_time") RequestBody meeting_time,
                                                                                  @Part("meeting_recruitment") RequestBody meeting_recruitment,
                                                                                  @Part("age_limit_min") RequestBody age_limit_min,
                                                                                  @Part("age_limit_max") RequestBody age_limit_max,
                                                                                  @Part("meeting_id") RequestBody meeting_id);

//        @POST("/myWeetingUpdate")
//        Call<MoimEditSameImageData.MoimEditSameImageDataResponse> editSameImgMoim(@Path("meeting_interest") String meeting_interest,
//                                                                                  @Path("meeting_name") String meeting_name,
//                                                                                  @Path("meeting_description") String meeting_description,
//                                                                                  @Path("meeting_location") String meeting_location,
//                                                                                  @Path("meeting_time") String meeting_time,
//                                                                                  @Path("meeting_recruitment") int meeting_recruitment,
//                                                                                  @Path("age_limit_min") int age_limit_min,
//                                                                                  @Path("age_limit_max") int age_limit_max,
//                                                                                  @Path("meeting_id") int meeting_id);
    }
}
