package com.example.auser.androidnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailActivity extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //將在android內部插入外部網站
        wv=(WebView)findViewById(R.id.webView);
        String url=getIntent().getStringExtra("url");
        wv.getSettings().setJavaScriptEnabled(true);//現在網站都很多使用JavaScript,所以打開webview功能
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(url);


    }
}
