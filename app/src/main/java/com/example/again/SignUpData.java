package com.example.again;

import android.graphics.Bitmap;
import android.media.Image;
import android.text.Html;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class SignUpData {
    @SerializedName("user_email")
    String user_email;
    @SerializedName("user_name")
    String user_name ;
    @SerializedName("user_passwd")
    String user_passwd;
    @SerializedName("user_nick_name")
    String user_nick_name;
    @SerializedName("user_birth")
    Date user_birth;
    @SerializedName("user_img")
    Bitmap img;


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_passwd() {
        return user_passwd;
    }

    public void setUser_passwd(String user_passwd) {
        this.user_passwd = user_passwd;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public void setUser_nick_name(String user_nick_name) {
        this.user_nick_name = user_nick_name;
    }

    public Date getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(Date user_birth) {
        this.user_birth = user_birth;
    }

    public SignUpData(String user_passwd, Date user_birth, String user_email, String user_name, String user_nick_name, Bitmap img) {
        this.user_passwd = user_passwd;
        this.user_birth = user_birth;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_nick_name = user_nick_name;
        this.img = img;
    }
    class SignUpResponse{
        int state;
        String message;

        public int getStatus() {
            return state;
        }

        public String getMessage() {
            return message;
        }
    }
    public interface ServiceApi{
        @POST("/login/join")
        Call<SignUpResponse> userSignUP(@Body SignUpData data);
        @POST("/login/login")
        Call<LoginResponse> userLogin(@Body LoginData data);
    }
}





