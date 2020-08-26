package com.example.again;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("meeting_recruitment")
    String meeting_recruitment;
}
