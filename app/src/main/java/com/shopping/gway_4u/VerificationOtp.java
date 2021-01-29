package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerificationOtp extends AppCompatActivity implements TextWatcher, View.OnClickListener {


    String TAG = "VerificationOtp";
    private ProgressDialog dialog;
    config_hosts hosts = new config_hosts();
    String URL_RES = hosts.FORGOTTEN;
    String emailID;

    EditText fnumber,secnumber,thdnumber,foutnumber,femail;;
    Button clearOtp, submitbtn;
    TextView warningview,messages;
    String OTP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emailID = getIntent().getStringExtra("email");

        setContentView(R.layout.activity_verification_otp);
        Log.e("TAG",TAG);
        initializeViews();
    }


    public void initializeViews()
    {
         dialog = new ProgressDialog(VerificationOtp.this);
         dialog.setTitle("Verifying");
         dialog.setCancelable(false);

         femail = findViewById(R.id.femail);
         submitbtn = findViewById(R.id.submit_OTP);
         warningview = findViewById(R.id.warning);
         fnumber = findViewById(R.id.fnum);
         messages = findViewById(R.id.messages);
         secnumber = findViewById(R.id.snum);
         thdnumber = findViewById(R.id.tnum);
         foutnumber = findViewById(R.id.fonum);
         clearOtp = findViewById(R.id.clearOTP);

         fnumber.addTextChangedListener(this);
         secnumber.addTextChangedListener(this);
         thdnumber.addTextChangedListener(this);
         foutnumber.addTextChangedListener(this);
         clearOtp.setOnClickListener(this);


        if (emailID != null || emailID.isEmpty())
        {
            String msg = getString(R.string.text_verify, emailID);
            messages.setText(msg);
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (fnumber.getText().length() > 0)
        {

            secnumber.requestFocus();
            fnumber.setEnabled(false);

        }

        if (secnumber.getText().length() > 0)
        {

            thdnumber.requestFocus();
            secnumber.setEnabled(false);

        }

        if (thdnumber.getText().length() > 0)
        {
            foutnumber.requestFocus();
            thdnumber.setEnabled(false);
        }

        if (foutnumber.getText().length() > 0)
        {
            foutnumber.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }




    public void otp_error(String errorMessage)
    {
        warningview.setText(errorMessage);
    }



    public void clearOTP()
    {

        warningview.setText("");
        fnumber.setText(null);
        secnumber.setText(null);
        thdnumber.setText(null);
        foutnumber.setText(null);

        fnumber.setEnabled(true);
        secnumber.setEnabled(true);
        thdnumber.setEnabled(true);
        foutnumber.setEnabled(true);

        fnumber.requestFocus();

    }


    public void showDialog()
    {
        dialog.show();
        warningview.setText(null);
    }

    public void hideDialog()
    {
        dialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.clearOTP : clearOTP();
            break;
        }
    }
}