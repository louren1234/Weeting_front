package com.example.again;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

public class RetrofitClient {
    private final static String BASE_URL = "http://52.35.235.199:3000";
    static Retrofit retrofit = null;
    private String authToken;

    public static Retrofit getClient(){

//        CookieHandler cookieHandler = new CookieManager();
//        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
//                new SharedPrefsCookiePersistor(context));

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .cookieJar(new JavaNetCookieJar(cookieHandler))
////                .retryOnConnectionFailure(true)
//                .readTimeout(100, TimeUnit.SECONDS)
//                .writeTimeout(100, TimeUnit.SECONDS)
////                .cookieJar(cookieJar)
//                .build();

//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieHandler cookieHandler = new CookieManager();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
//                .retryOnConnectionFailure(true)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
//                .cookieJar(cookieJar)
                .build();

//        clientBuilder.addInterceptor(loggingInterceptor);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // gson converter를 생성합니다. gson은 JSON 자바 클래스로 바꾸는데 사용됩니다.
                    .client(okHttpClient)
                    .build();

        }
        return retrofit;
    }


}
