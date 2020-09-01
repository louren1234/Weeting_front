package com.example.again;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebChromeClientSet extends WebChromeClient {

    Dialog dialog1;

    private Activity mActivity = null;

    public WebChromeClientSet(Activity activity){
        this.mActivity = activity;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        // Dialog Create Code
        WebView newWebView = new WebView(mActivity);

        WebSettings webSettings = newWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dialog1 = new Dialog(mActivity);
        dialog1.setContentView(newWebView);

        ViewGroup.LayoutParams params = dialog1.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog1.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog1.show();
        newWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(WebView window) {

            }
        });
        ((WebView.WebViewTransport) resultMsg.obj).setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;


//        WebSettings webSettings = newWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        final Dialog dialog = new Dialog(mActivity);
//        dialog.setContentView(newWebView);
//
//        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//        dialog.show();
//        newWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onCloseWindow(WebView window) {
//                dialog.dismiss();
//            }
//        });
//
//        // WebView Popup에서 내용이 안보이고 빈 화면만 보여 아래 코드 추가
//        newWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                view.loadUrl("http://52.35.235.199:3000/daum_address.php");
//                return true;
//            }
//        });
//
//        ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
//        resultMsg.sendToTarget();
//        return true;

    }
}
