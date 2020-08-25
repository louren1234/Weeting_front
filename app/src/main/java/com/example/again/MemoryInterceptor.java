//package com.example.again;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class MemoryInterceptor implements Interceptor {
//    private Context context;
//
//    public MemoryInterceptor(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Request.Builder requestBuilder = request.newBuilder();
//        String printUrl = convertNumericValue(request.url().encodedPathSegments());
//
//        Log.i("MemoryInterceptor", "printUrl : "+printUrl);
//        SharedPreferences preferences = context.getSharedPreferences(MemoryUtils.PREFERENCES, Context.MODE_PRIVATE);
//
//
//        // cookie 가져오기
//        Set<String> getCookies = preferences.getStringSet("cookies", new HashSet<>());
//
//        // cookie 셋팅
//        for (String cookie : getCookies) {
//            requestBuilder.addHeader("Cookie", cookie);
//        }
//        Response setResponse = chain.proceed(requestBuilder.build());
//
//
//        // cookie를 SharedPreferences에 넣어주기
//        Response getResponse = chain.proceed(chain.request());
//        if (!getResponse.headers("Set-Cookie").isEmpty()) {
//            HashSet<String> cookies = new HashSet<>();
//            for (String header : getResponse.headers("Set-Cookie")) {
//                cookies.add(header);
//            }
//            // Preference에 cookies를 넣어주는 작업을 수행
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putStringSet("cookies", cookies);
//            editor.commit();
//
//            return getResponse;
//        }
//
//        return setResponse;
//    }
//
//    private String convertNumericValue(List<String> encodedPathSegments) {
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String encodedPathSegment : encodedPathSegments) {
//            encodedPathSegment = encodedPathSegment.replaceAll("^[0-9]{1,}$", "*");
//            stringBuilder.append("/" + encodedPathSegment);
//        }
//        return stringBuilder.toString();
//    }
//}
