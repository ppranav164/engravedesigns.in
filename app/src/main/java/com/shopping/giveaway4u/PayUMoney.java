package com.shopping.giveaway4u;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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

public class PayUMoney extends AppCompatActivity implements View.OnClickListener {


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
        breakupsbtn = findViewById(R.id.breakupbutton);
        showUpbreakups = findViewById(R.id.showUpbreakups);
        payNowButton.setOnClickListener(this);


        listView = findViewById(R.id.listviews);

        dialog = new Dialog(this); // Context, this, etc.

        userDataEditor = getSharedPreferences("user_data",MODE_PRIVATE).edit();


        getUserData = getSharedPreferences("user_data",MODE_PRIVATE);

        String options = getUserData.getString("optionList",null);

        Toast.makeText(getApplicationContext(),""+optionValues,Toast.LENGTH_LONG).show();


        setUpUserDetails();

        selectSandBoxEnv();

        setupCitrusConfigs();

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


                Log.d("Confirm Order",data);
                setCartData(data);
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





    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    public static boolean isValidEmail(String strEmail) {

        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }



    public static boolean isValidPhone(String phone) {

        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }



    private void setUpUserDetails() {

        userDetailsPreference = getSharedPreferences(AppPreference.USER_DETAILS, MODE_PRIVATE);
        userEmail = userDetailsPreference.getString(AppPreference.USER_EMAIL, "");

        userMobile = userDetailsPreference.getString(AppPreference.USER_MOBILE, "");

    }

    private void selectSandBoxEnv() {

        //((BaseApplication) getApplicationContext()).setAppEnvironment(AppEnvironment.SANDBOX);
        new BaseApplication().setAppEnvironment(AppEnvironment.SANDBOX);
        editor = settings.edit();
        editor.putBoolean("is_prod_env", false);
        editor.apply();

        setupCitrusConfigs();
    }


    private void setupCitrusConfigs() {

//        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();


        AppEnvironment appEnvironment = new BaseApplication().getAppEnvironment();

        if (appEnvironment == AppEnvironment.PRODUCTION) {
            Toast.makeText(PayUMoney.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PayUMoney.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {

        userEmail = "ppranav_tr@icloud.com";
        userMobile = "8129361783";

            switch (v.getId()) {
                case R.id.pay_now_button:
                    payNowButton.setEnabled(false);
                    launchPayUMoneyFlow();
                    break;

            }

    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        //AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();

        AppEnvironment appEnvironment = AppEnvironment.SANDBOX;

        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }



    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();


        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {

            String bal = "10";

            amount = Double.parseDouble(bal);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        //String txnId = "TXNID720431525261327973";
        String phone = "8129361783";
        String productName = "iPhone 11";
        String firstName = "Pranav";
        String email = "ppranav_tr@icloud.com";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppEnvironment.SANDBOX;

        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            //    generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,PayUMoney.this, R.style.AppTheme_default,true);

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            payNowButton.setEnabled(true);
        }
    }


    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayUMoney.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            payNowButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(PayUMoney.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoney.this, R.style.AppTheme_default,true);

            }
        }
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
