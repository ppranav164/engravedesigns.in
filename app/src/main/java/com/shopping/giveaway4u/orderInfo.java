package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class orderInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        getSupportActionBar().setTitle("ORDER INFO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

}