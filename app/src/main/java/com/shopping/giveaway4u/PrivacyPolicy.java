package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyPolicy extends AppCompatActivity {

    String url = "http://192.168.1.108/index.php?route=information/information/agree&information_id=3";

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.policyweb);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}