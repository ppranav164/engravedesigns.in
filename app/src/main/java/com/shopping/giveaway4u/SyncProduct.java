package com.shopping.giveaway4u;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncProduct extends AsyncTask<String,String,String> {



    String api = "http://192.168.1.108/index.php?route=api/product&product_id=";

    private static String rest_url;


    private products mproducts;

    private static String itemId;

    private Context context;

    ProgressDialog dialog;



    public SyncProduct(Context cxt,products myproducts,String pro_id)
    {

        this.context = cxt;

        this.mproducts = myproducts;

        this.itemId = pro_id;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);



        if (s!=null)
        {
            mproducts.loadProductInfo(s);


            dialog.dismiss();
        }



    }



    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();


        try{

            URL link = new URL(api+itemId);

            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod("GET");

            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while (true) {
                String readLine = reader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }

                builder.append(line);
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }



        return builder.toString();
    }


}
