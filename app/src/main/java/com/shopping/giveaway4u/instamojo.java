package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.instamojo.android.Instamojo;


public class instamojo extends AppCompatActivity {


    String orderId;

    WebView webView;

    config_hosts config_hosts = new config_hosts();

    String url = config_hosts.instamojo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instamojo);

        Bundle extras = getIntent().getExtras();

        orderId = extras.getString("order_id");

        webView = (WebView) findViewById(R.id.webview_layout);

        SharedPreferences sharedpref = getSharedPreferences("cookie", Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");


        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());

        CookieManager manager = CookieManager.getInstance();

        manager.setAcceptCookie(true);

        manager.removeSessionCookie();

        manager.setCookie("http://192.168.1.108","OCSESSID="+token);

        cookieSyncManager.sync();


        String cookie = manager.getCookie("http://192.168.1.108");


        Log.d("Cookie Call", "cookie ------>"+cookie);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);

    }


}


