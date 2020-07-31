package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_Register extends AppCompatActivity implements View.OnClickListener {


    config_hosts hosts = new config_hosts();

    String URL = hosts.register;

    Button ccl;

    String FIRSTNAME,LASTNAME,EMAIL,PASSWORD,CPASSWORD,MOBILE;

    boolean AGREE;

    EditText fnmae,lname,email,password,cpassword,mobileno;

    Button regbutton;

    TextView policybtn,waringTv;

    CheckBox agreement;

    boolean error;


    //Errors

    String  ERROR_WARNING ="",
            ERROR_FIRSTNAME="",
            ERROR_LASTNAME ="",
            ERROR_EMAIL ="",
            ERROR_PASSWORD ="",
            ERROR_CONFIRM ="",
            ERROR_MOBILE ="";


    public static final String PHONE_PATTERN = "^[987]\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout__register);

        fnmae = findViewById(R.id.reg_fname);
        lname = findViewById(R.id.reg_lname);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_pass);
        cpassword = findViewById(R.id.reg_cpass);
        mobileno = findViewById(R.id.reg_mobile);
        regbutton = findViewById(R.id.reg_signup);
        policybtn = findViewById(R.id.reg_policy);
        agreement = findViewById(R.id.reg_agree);
        ccl = findViewById(R.id.Cancel);
        waringTv = findViewById(R.id.reg_warning);

        regbutton.setOnClickListener(this);
        ccl.setOnClickListener(this);
        policybtn.setOnClickListener(this);

        AGREE = agreement.isChecked();

    }


    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId)
        {
            case R.id.reg_signup : startRegistraion();
                break;

            case R.id.Cancel : Intent intent = new Intent(getApplicationContext(),Activity_login.class);
                               startActivity(intent);
                               finish();
                               break;
            case R.id.reg_policy : Intent priv = new Intent(getApplicationContext(),PrivacyPolicy.class);
                                   startActivity(priv);
                                   break;
        }

    }


    public void startRegistraion()
    {

        FIRSTNAME = fnmae.getText().toString();
        LASTNAME = lname.getText().toString();
        EMAIL = email.getText().toString();
        MOBILE = mobileno.getText().toString();
        PASSWORD = password.getText().toString();
        CPASSWORD = cpassword.getText().toString();


        if (formVerify() != false)
        {
            registerUser();
        }
    }


    public void registerUser()
    {

        String param = "firstname="+FIRSTNAME;

        param += "&lastname="+LASTNAME;

        param +="&email="+EMAIL;
        param +="&telephone="+MOBILE;
        param +="&password="+PASSWORD;
        param +="&confirm="+CPASSWORD;
        param +="&agree="+AGREE;
        param +="&newsletter=0";

        Log.e("reg",param);

        new syncRegistration(getApplicationContext(), "POST", URL,param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("reg re",object);

                try {

                    JSONObject object1 = new JSONObject(object);


                    if (object1.has("success"))
                    {
                        error = false;
                        showLogin();

                    }else {


                        if (object1.getString("error_warning").length() > 1) {
                            ERROR_WARNING = object1.getString("error_warning");
                            waringTv.setText(object1.getString("error_warning"));
                            waringTv.setTextColor(getResources().getColor(R.color.red));
                            error = true;
                        }

                        if (object1.getString("error_firstname").length() > 1) {
                            ERROR_FIRSTNAME = object1.getString("error_firstname");
                            fnmae.setError(ERROR_FIRSTNAME);
                            error = true;
                        }

                        if (object1.getString("error_lastname").length() > 1) {
                            ERROR_LASTNAME = object1.getString("error_lastname");
                            lname.setError(ERROR_LASTNAME);
                            error = true;
                        }

                        if (object1.getString("error_email").length() > 1) {
                            ERROR_EMAIL = object1.getString("error_email");
                            email.setError(ERROR_EMAIL);
                            error = true;
                        }


                        if (object1.getString("error_telephone").length() > 1) {
                            ERROR_MOBILE = object1.getString("error_telephone");
                            mobileno.setError(ERROR_MOBILE);
                            error = true;
                        }

                        if (object1.getString("error_password").length() > 1) {
                            ERROR_MOBILE = object1.getString("error_password");
                            password.setError(ERROR_PASSWORD);
                            error = true;
                        }

                        if (object1.getString("error_confirm").length() > 1) {
                            ERROR_CONFIRM = object1.getString("error_confirm");
                            cpassword.setError(ERROR_CONFIRM);
                            error = true;
                        }

                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }



    public void showLogin()
    {
        Intent intent = new Intent(getApplicationContext(),Activity_login.class);
        startActivity(intent);
        finish();
    }



    public boolean formVerify()
    {

        if (TextUtils.isEmpty(fnmae.getText().toString()))
        {
            fnmae.setError("First Name can't be empty");

            return false;
        }

        if (TextUtils.isEmpty(lname.getText().toString()))
        {
            lname.setError("Last Name can't be empty");

            return false;
        }


        if (TextUtils.isEmpty(lname.getText().toString()))
        {
            lname.setError("Last Name can't be empty");

            return false;
        }


        if (isValidEmail(email.getText().toString()) != true)
        {
            email.setError("Invalid Email Address");
            return false;
        }


        if (TextUtils.isEmpty(mobileno.getText().toString()))
        {
            mobileno.setError("Please Enter Mobile Number");
            return false;
        }


        if (TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError("Please Enter Password");
            return false;
        }

        if (TextUtils.isEmpty(cpassword.getText().toString()))
        {
            cpassword.setError("Please Enter Confim Password");
            return false;
        }


        String pass = password.getText().toString().trim();

        String cpasss = cpassword.getText().toString().trim();

        if (!cpasss.equals(pass))
        {
            cpassword.setError("Please confirm password");
            return  false;
        }


        if (agreement.isChecked() != true)
        {
            agreement.setError("You must agree to the privacy policy");
            return false;
        }


        return true;

    }




    public static boolean isValidEmail(String strEmail) {

        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }



    public static boolean isValidPhone(String phone) {

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();

    }




}
