package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class activity_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_login);


//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//               try {
//                   Thread.sleep(2000);
//
//                   Intent intent = new Intent(getApplicationContext(),Activity_login.class);
//                   startActivity(intent);
//                   finish();
//               }catch (Exception e)
//               {
//                   e.printStackTrace();
//               }
//            }
//        });


        SharedPreferences preferences = getSharedPreferences("cookie",MODE_PRIVATE);

        Boolean status_login = preferences.getBoolean("logged_in",false);

        if (status_login.booleanValue())
        {

            Intent intent = new Intent(activity_Splash.this,logo_splash.class);
            startActivity(intent);
            finish();

        }else
        {
            Intent intent = new Intent(activity_Splash.this,Activity_login.class);
            startActivity(intent);
            finish();

        }




        SharedPreferences.Editor editor = getSharedPreferences("isLoggedIn",MODE_PRIVATE).edit();

        editor.putBoolean("loggedIn",false);

        editor.apply();




        SharedPreferences.Editor fed = getSharedPreferences("isFirstTime",MODE_PRIVATE).edit();


        fed.putBoolean("installed",true);

        fed.commit();


    }












}
