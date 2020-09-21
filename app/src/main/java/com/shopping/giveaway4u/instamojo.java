package com.shopping.giveaway4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.instamojo.android.Instamojo;

import java.util.Map;


public class instamojo extends AppCompatActivity {


    String orderId;

    WebView webView;

    config_hosts config_hosts = new config_hosts();

    String url = config_hosts.instamojo;


    AlertDialog.Builder alertDialog;

    LinearLayout linearLayout;

    private Handler handler;

    private Runnable runnable;

    SharedPreferences.Editor editor;

    boolean paymentSuccess = false;

    Button donebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instamojo);

        Bundle extras = getIntent().getExtras();

        orderId = extras.getString("cart_order_id");

        Log.w("order_id",orderId);

        onNewIntent(getIntent());

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                Log.e("running","running handler");

               if (getSuccessOrder() == true)
               {
                   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                   donebtn.setVisibility(View.VISIBLE);
                   //showSuccess();
                   paymentSuccess = true;
                   handler.removeCallbacks(runnable);
               }

               handler.postDelayed(runnable,1000);
            }


        };

        handler.postDelayed(runnable,1000);



        alertDialog = new AlertDialog.Builder(this);

        getSupportActionBar().setTitle("Debit/Credtcard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        webView = (WebView) findViewById(R.id.webview_layout);
        donebtn = findViewById(R.id.donebtn);

        SharedPreferences sharedpref = getSharedPreferences("cookie", Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");

        linearLayout = findViewById(R.id.alertBox);


        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());

        CookieManager manager = CookieManager.getInstance();

        manager.setAcceptCookie(true);

        manager.removeSessionCookie();

        manager.setCookie(config_hosts.BASE_URL,"OCSESSID="+token);

        cookieSyncManager.sync();


        String cookie = manager.getCookie(config_hosts.BASE_URL);


        Log.d("Cookie Call", "cookie ------>"+cookie);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);

        String successURLExisits = webView.getUrl();

        Log.e("successURLExisits",successURLExisits);

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }


    public boolean  getSuccessOrder()
    {
        SharedPreferences verifyPrefer = getSharedPreferences("VERIFY_ORDER",MODE_PRIVATE);

        if (verifyPrefer.getString("ORDER_ID",null) != null)
        {
            String oid = verifyPrefer.getString("ORDER_ID",null);

            Log.w("message",oid);

            return true;
        }

        return  false;
    }


    public void showSuccess()
    {

        webView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        Fragment fragment = new success("Thank your for puchasing with us");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.alertBox,fragment);
        transaction.commit();

    }



    @Override
    public boolean onSupportNavigateUp(){

        handler.removeCallbacks(runnable);
        backToMain();
        return true;
    }


    public void backToMain()
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {

       if (paymentSuccess == true)
       {
           handler.removeCallbacks(runnable);
           backToMain();
       }else {

           showDialog("Warning","Are you sure you want to cancel payment?");
       }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();

    }

    public void showDialog(String title, String message)
    {

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                handler.removeCallbacks(runnable);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("Cancel",null);

        alertDialog.show();

    }









}


