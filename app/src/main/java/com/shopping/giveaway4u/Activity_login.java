package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class Activity_login extends AppCompatActivity {


    TextView accCr;

    EditText Email,Pass;

    Button loginB;

    private static String email;

    private static  String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.layout_login);


        accCr = findViewById(R.id.ClcreateAcc);

        Email = findViewById(R.id.Enemail);

        Pass = findViewById(R.id.Enpass);

        accCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Activity_Register.class);
                startActivity(intent);


            }
        });


        loginB = findViewById(R.id.Blogin);



        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Email.getText().toString();

                password = Pass.getText().toString();

                new syncLogin(getApplicationContext(), new login() {

                    Boolean isLogged_in = false;

                    @Override
                    public void confirm(String message) {

                        try {

                            JSONObject object = new JSONObject(message);

                          if (object.has("success")){


                              SharedPreferences.Editor sharedpref = getSharedPreferences("cookie",MODE_PRIVATE).edit();

                              sharedpref.putString("token",object.getString("Set-Cookie"));

                              sharedpref.putString("username",object.getString("username"));

                              sharedpref.putString("address_id",object.getString("address_id"));

                              sharedpref.putBoolean("logged_in",true);

                              sharedpref.apply();

                              Intent intent = new Intent(Activity_login.this,MainActivity.class);
                              startActivity(intent);
                              finish();

                          }else {

                              Toast.makeText(getApplicationContext(),""+object.getString("warning"),Toast.LENGTH_LONG).show();
                          }


                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                },email,password).execute();


            }
        });



    }







}
