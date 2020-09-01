package com.example.again;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SearchMoimAddress extends AppCompatActivity {
    private TextView textAddress;
    private WebView addressWebView, addressWebViewPop;
    private WebSettings webSettings;
    private FrameLayout webViewFrame;
    private Handler handler;



//    protected ProxyWebChromeClient proxyWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_address);

        textAddress = findViewById(R.id.setAddress);
        webViewFrame = findViewById(R.id.webViewFrame);
        addressWebView = findViewById(R.id.webViewForAddress);
        addressWebViewPop = findViewById(R.id.webViewForAddress);

        initWeb();

        handler = new Handler();
    }

    public void initWeb() {
        addressWebView = findViewById(R.id.webViewForAddress);
        webSettings = addressWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        addressWebView.setWebChromeClient(new WebChromeClientSet(this));
        addressWebView.addJavascriptInterface(new AndroidBridge(), "Testapp");
        addressWebView.loadUrl("http://52.35.235.199:3000/daum_address.php");
        addressWebView.loadUrl("javascript:daum");

        addressWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
//        addressWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onCreateWindow(final WebView view, boolean dialog, boolean isUserGesture, Message resultMsg) {
//                addressWebViewPop = new WebView(view.getContext());
//                addressWebViewPop.getSettings().setJavaScriptEnabled(true);
//                addressWebViewPop.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                addressWebViewPop.getSettings().setSupportMultipleWindows(true);
//                addressWebViewPop.getSettings().setDomStorageEnabled(true);
//                addressWebViewPop.setWebChromeClient(new WebChromeClient(){
//                    @Override
//                    public void onCloseWindow(WebView window){
//                        webViewFrame.removeView(window);
//                        window.destroy();
//                    }
//                });
//
//                addressWebViewPop.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public void onReceivedSslError(WebView view,
//                                                   SslErrorHandler handler, SslError error) {
//                        handler.proceed();
//                    }
//                });
//                addressWebViewPop.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
//                webViewFrame.addView(addressWebViewPop);
//                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                transport.setWebView(addressWebViewPop);
//                resultMsg.sendToTarget();
//                return true;
//            }
//        });
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
