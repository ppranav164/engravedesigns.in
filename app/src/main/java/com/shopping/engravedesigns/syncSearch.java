package com.shopping.engravedesigns;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncSearch extends AsyncTask<String,String,String>
{


    config_hosts hosts = new config_hosts();

    private Context context;

    info info;

    int proid;

    String params;

    String API_URL = hosts.searchBy;

    public syncSearch(Context cxt,String param, info info)
    {

        this.context = cxt;

        this.info = info;

        this.params = param;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        info.getInfo(s);

    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");
        

        try {

            URL url = new URL(API_URL+params);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setDoOutput(true);

            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

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













