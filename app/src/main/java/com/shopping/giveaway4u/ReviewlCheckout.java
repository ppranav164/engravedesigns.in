package com.shopping.giveaway4u;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReviewlCheckout extends AppCompatActivity implements View.OnClickListener {


    public static final String TAG = "PayUMoney : ";
    private boolean isDisableExitConfirmation = false;
    private String userMobile, userEmail;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private SharedPreferences userDetailsPreference;
    private Button payNowButton;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private SharedPreferences.Editor userDataEditor;
    private SharedPreferences getUserData;
    Map<String,String> optionValues;
     JSONArray Jsondata,Totals;
    ArrayList<list_totals> totaldata = new ArrayList<>();
    ListView listView;
    config_hosts hosts = new config_hosts();
     Dialog dialog;

     TextView breakupsbtn;

     LinearLayout showUpbreakups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umoney);

        settings = getSharedPreferences("settings", MODE_PRIVATE);

        payNowButton = (Button) findViewById(R.id.pay_now_button);

        payNowButton.setText("PLACE ORDER");

        breakupsbtn = findViewById(R.id.breakupbutton);
        showUpbreakups = findViewById(R.id.showUpbreakups);
        payNowButton.setOnClickListener(this);


        listView = findViewById(R.id.listviews);

        dialog = new Dialog(this); // Context, this, etc.

        userDataEditor = getSharedPreferences("user_data",MODE_PRIVATE).edit();


        getUserData = getSharedPreferences("user_data",MODE_PRIVATE);

        String options = getUserData.getString("optionList",null);

        Toast.makeText(getApplicationContext(),""+optionValues,Toast.LENGTH_LONG).show();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        confirmOrder();

        openDialog();




    }


    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }


    public void confirmOrder()
    {

        String confirmURL = hosts.confirmOrder;
        new syncCookie(getApplicationContext(), confirmURL, new info() {
            @Override
            public void getInfo(String data) {


                Log.d("ReviewcheckoutCnfOrder",data);
                setCartData(data);
            }
        }).execute();
    }



    public void success()
    {

        //It will wipe off cookies that were made during in  checkout

        //Use it to reset reset

        String successAPI = hosts.successOrder;

        new syncCookie(getApplicationContext(), successAPI, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Order Success",data);

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

            }
        }).execute();

    }



    public void viewbreackups(View view)
    {


        if (listView.getVisibility() == View.VISIBLE)
        {
            listView.setVisibility(View.GONE);
            showUpbreakups.setBackgroundColor(getResources().getColor(R.color.white));
        }else
        {
            listView.setVisibility(View.VISIBLE);
        }


    }






    public void setCartData(String data)
    {

        try {


            JSONObject object = new JSONObject(data);

            JSONArray array = object.getJSONArray("products");

            JSONArray totals = object.getJSONArray("totals");

            setJsonData(array,totals);


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void setJsonData(JSONArray array, JSONArray totals)
    {
        this.Jsondata = array;

        this.Totals = totals;



        RecyclerView recyclerView = findViewById(R.id.payNow_recyclerview);

        recycleradapter_pay_now adapter = new recycleradapter_pay_now(getApplicationContext(),Jsondata,Totals);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setAdapter(adapter);

        recyclerView.smoothScrollToPosition(0);
        recyclerView.setFocusable(true);
        recyclerView.setNestedScrollingEnabled(false);

        adapter.notifyDataSetChanged();



        try {


            for (int i=0; i < Totals.length(); i++)
            {
                JSONObject object = Totals.getJSONObject(i);

                String titles = object.getString("title");
                String texts = object.getString("text");

                totaldata.add(new list_totals(titles,texts));

            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }


        CustomAdapter ladapter = new CustomAdapter(getApplicationContext(),R.layout.listviews,totaldata);

        listView = findViewById(R.id.listviews);

        listView.setAdapter(ladapter);

        ladapter.notifyDataSetChanged();

        listView.invalidateViews();

        closeDialog();

        int lastindex = totaldata.size();

        list_totals m = totaldata.get(lastindex - 1);

        String tot = m.title + ":" + m.text;

        breakupsbtn.setText(tot);

    }




    @Override
    public void onClick(View v) {


            switch (v.getId()) {
                case R.id.pay_now_button:
                    payNowButton.setEnabled(false);
                    cashOnDelivery();
                    break;

            }

    }



    public void cashOnDelivery()
    {

        OrderCOD();

        success();

        new syncInfo(getApplicationContext(), new info() {
            @Override
            public void getInfo(String data) {

                try {

                    JSONObject jsonObject = new JSONObject(data);

                    String items = jsonObject.getString("text_items");

                    //Textview here to set count to cart basket
//                    notify = menutabs.findViewById(R.id.notify_badge);
//
//                    notify.setText(items);

                    Log.e("total",items);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();



        notifyThis("Giveaway4u","Thank you for purchasing with us");

    }


    public void notifyThis(String title, String message) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Breaking News")
                .setContentText(title)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true);

        notification.build().flags |= Notification.FLAG_AUTO_CANCEL;

        Uri sounds = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//                notification.setSound(sounds);
//                notification.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//                notification.setLights(Color.RED, 3000, 3000);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class); //to open an activity on touch notification

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


        resultIntent.putExtra("orders","orders");

        int requestID = (int) System.currentTimeMillis();

        PendingIntent resultPendingIntent = PendingIntent
                .getActivity(getApplicationContext(),requestID,resultIntent,0);

        notification.setContentIntent(resultPendingIntent);

        int idv = 1;

        //notificationManager.notify(String.valueOf(idv),i,notification.build());

        notificationManager.notify("mine",idv,notification.build());


    }





    public void OrderCOD()
    {
        // order COMPLETE > COD before redirect success
        String cod = hosts.OrderCOD;
        new syncCookie(getApplicationContext(), cod, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Order Status",data);

            }
        }).execute();
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
