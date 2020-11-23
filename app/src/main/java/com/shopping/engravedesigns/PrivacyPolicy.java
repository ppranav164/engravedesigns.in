package com.shopping.engravedesigns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicy extends AppCompatActivity {

    config_hosts hosts = new config_hosts();

    String URL = hosts.privacyPolicy;

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.policyweb);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

    }
}