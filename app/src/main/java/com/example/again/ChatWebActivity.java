package com.example.again;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ChatWebActivity extends AppCompatActivity {
    private WebView webView;
    private Handler handler;
    String tempItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_web);
        // WebView 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }
    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
//        webView.addJavascriptInterface(new AndroidBridge(), "Weeting");
        webView.getSettings().setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webView.getSettings().setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webView.getSettings().setSupportZoom(true); // 화면 줌 허용 여부
        webView.getSettings().setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webView.getSettings().setDomStorageEnabled(true); // 로컬저장소 허용 여부

        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
        tempItem = sp.getString("name","");
        System.out.println(tempItem);
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load. php 파일 주소
        webView.loadUrl("http://52.35.235.199:3000/chats/66/"+tempItem);
    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Intent i = new Intent(LocationActivity.this, SignUpActivity.class);
//                    i.putExtra("location", txt_address.getText().toString());
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}