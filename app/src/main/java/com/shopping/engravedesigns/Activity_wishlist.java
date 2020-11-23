package com.shopping.engravedesigns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity_wishlist extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_layout);



        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.drawable.logot);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);



    }


}
