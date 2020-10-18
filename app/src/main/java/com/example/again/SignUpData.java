package com.example.again;

import android.media.Image;

import com.example.again.LoginData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    @SerializedName("user_interests")
    String user_interests;
    @SerializedName("user_img")
    Image img;


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

    public SignUpData(String user_passwd, Date user_birth, String user_email, String user_name, String user_nick_name, Image img) {
        this.user_passwd = user_passwd;
        this.user_birth = user_birth;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_nick_name = user_nick_name;
        this.img = img;
    }
    class Response{
        int state;
        String message;
        String token;

        public int getStatus() {
            return state;
        }

        public String getMessage() {
            return message;
        }

        public String getToken(){
            return token;
        }
    }

    public interface ServiceApi{
        @POST("/login/join")
        @Multipart
        Call<Response> userSignUP(@Part("user_passwd") RequestBody user_passwd, @Part("user_birth") RequestBody user_birth,
                                  @Part("user_email") RequestBody user_email, @Part("user_name") RequestBody user_name,
                                  @Part("user_nick_name") RequestBody user_nick_name, @Part("user_interests") RequestBody user_interests, @Part MultipartBody.Part user_img

        );
        @POST("/login/login")
        Call<LoginResponse> userLogin(@Body LoginData data);

        @FormUrlEncoded
        @POST("/login/join/check/nickname")
        Call<Response> nickNameOverlap(@Field("user_nick_name") String user_nick_name);

        @FormUrlEncoded
        @POST("/login/join/check/email")
        Call<Response> emailOverlap(@Field("user_email") String user_email);

        @FormUrlEncoded
        @POST("/login/join/auth/email")
        Call<Response> emailCheck(@Field("user_email") String user_email);
    }
}





