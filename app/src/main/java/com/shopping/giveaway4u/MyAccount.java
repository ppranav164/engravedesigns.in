package com.shopping.giveaway4u;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.Iterator;

public class MyAccount extends AppCompatActivity  {


    TabLayout tabLayout;
    SharedPreferences.Editor editor ;
    Dialog dialog;


    private  settingsTabAdapter settingsTabAdapter;
    private ViewPager mviewPager;
    boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(MyAccount.this);


        editor = getSharedPreferences("addresses",MODE_PRIVATE).edit();

        tabLayout = findViewById(R.id.mytabs);

        mviewPager = findViewById(R.id.framelayouts);

        settingsTabAdapter = new settingsTabAdapter(getSupportFragmentManager());
        setUpViewpager(mviewPager);

        tabLayout.setupWithViewPager(mviewPager);

        boolean isUpdated = getIntent().getBooleanExtra("updated",false);

        updated = isUpdated;

        Toast.makeText(getApplicationContext(),""+updated,Toast.LENGTH_SHORT).show();


    }


     private void setUpViewpager(ViewPager viewpager)
     {

         settingsTabAdapter adapter = new settingsTabAdapter(getSupportFragmentManager());
         adapter.addFragment(new editAccountInfo(),"Account Info");
         adapter.addFragment(new account_returns(),"Returns");
         adapter.addFragment(new AccountAddress(),"Address Book");
         viewpager.setAdapter(adapter);
         adapter.notifyDataSetChanged();

     }



    public void alerts(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }





    @Override
    public boolean onSupportNavigateUp(){

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        return true;
    }



}