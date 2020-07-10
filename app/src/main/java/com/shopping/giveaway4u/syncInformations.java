package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class syncInformations extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new syncInfo().execute();
    }



    public class syncInfo extends AsyncTask<String,String,String>
    {


        config_hosts hosts;

        private Context context;

        int proid;

        info info;


        String API_URL = "http://192.168.1.108/index.php?route=api/counts";




        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            info.getInfo(s);

            Toast.makeText(getApplicationContext(),"Sync Informations",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder builder = new StringBuilder();

            SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

            String token = sharedpref.getString("token","nil");


            try {

                URL url = new URL(API_URL);

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




}
