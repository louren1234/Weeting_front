package com.example.again;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchMoimAddress extends AppCompatActivity {
    private TextView textAddress;
    private WebView addressWebView;
    private Handler handler;

//    protected ProxyWebChromeClient proxyWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_address);

        textAddress = findViewById(R.id.setAddress);

        initWeb();

        handler = new Handler();
    }

    public void initWeb() {
        addressWebView = findViewById(R.id.webViewForAddress);
        addressWebView.getSettings().setJavaScriptEnabled(true);
        addressWebView.getSettings().setLoadWithOverviewMode(true);
        addressWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        addressWebView.getSettings().setSupportMultipleWindows(true);

        addressWebView.addJavascriptInterface(new AndroidBridge(), "Testapp");
        addressWebView.setWebChromeClient(new WebChromeClientSet());
        addressWebView.loadUrl("http://52.35.235.199:3000/daum_address.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textAddress.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    initWeb();
                }
            });
        }
    }


}
