package com.shopping.giveaway4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link checkOut.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link checkOut#newInstance} factory method to
 * create an instance of this fragment.
 */
public class checkOut extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ArrayList<list_totals> totaldata = new ArrayList<>();

    RadioGroup addressGroup;

    LinearLayout deliveryGroup;

    RadioButton radioButtonD;

    RadioButton radioButtonM;

    LinearLayout addressField;

    LinearLayout addressDeliveryfield;

    RadioGroup paymentGroup;

    RadioButton[] radioButton = new RadioButton[5];


    TextView defAddTv,defAddDel;

    config_hosts hosts = new config_hosts();

    private OnFragmentInteractionListener mListener;


    RadioGroup shippingOPtions;

    RadioButton[] shipping = new RadioButton[5];

    RadioButton delveryOption;

    RadioButton paymentOption;

    Button startButton;


    String orderId;

    TextView notify;

    View menutabs;


    SharedPreferences.Editor userDataEditor;

    Map<String,String> optionslist = new HashMap<>();

    PayUMoney payUMoney ;

    View view;

    TextView alertbox;

    ImageView iconview;

    TextView enterCoupons;

    Button breakupbutton;

    Dialog dialog;


    private String coupon_string = "";


    public checkOut() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        View view = getView();

        dialog = new Dialog(getContext());

        openDialog();

        alertbox = view.findViewById(R.id.messagealert);

        shippingOPtions = view.findViewById(R.id.shippingtOptions);

        paymentGroup = view.findViewById(R.id.paymentGroup);

        startButton = view.findViewById(R.id.startPay);

        breakupbutton = view.findViewById(R.id.breakupbutton);

        enterCoupons = view.findViewById(R.id.entercoupon);

        iconview = view.findViewById(R.id.iconAlert);

        defAddDel = view.findViewById(R.id.defaultAddressTextDel);

        deliveryGroup = view.findViewById(R.id.deliveryGroup);

        userDataEditor = userDataEditor = getContext().getSharedPreferences("user_data",Context.MODE_PRIVATE).edit();


        getBillingAddress();
        setBillingAddress();

        getShippingAddress();
        setShippingAddress();

        getShippingMethod();
        setShippingService();

        getPaymentMethod();
        setPaymentMethod();

        initializePayment();


        editTextLoader();

        loadcouponbox();

        showDeliveryeditable();

    }




    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //super.onCreateOptionsMenu(menu, inflater);

        //getActivity().getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        menutabs = MenuItemCompat.getActionView(menuItem);


    }




    public void showDeliveryeditable()
    {

        deliveryGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.mainframeL,new editDelivery());
                transaction.addToBackStack("checkout");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

            }
        });
    }








    public void getBillingAddress()
    {

        //Retrive main user address details

        new syncPayment(getContext(), new info() {
            @Override
            public void getInfo(String data) {

                try {


                    SharedPreferences preferences = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

                    String addressId = preferences.getString("address_id",null);

                    JSONObject object = new JSONObject(data);

                    JSONObject address = object.getJSONObject("addresses");

                    JSONObject fields = address.getJSONObject(addressId);

                    String fname = fields.getString("firstname");

                    String lname = fields.getString("lastname");

                    String address1 = fields.getString("address_1");

                    String city = fields.getString("city");

                    String zone = fields.getString("zone");

                    String country = fields.getString("country");

                    String postcode = fields.getString("postcode");

                    defAddTv.setText(fname +" "+ lname + System.lineSeparator() + address1 + System.lineSeparator() + city + System.lineSeparator() + zone + System.lineSeparator() + country +System.lineSeparator() + "PIN : " + postcode  );

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();

    }







    public void getShippingAddress()

    {
        //Retrive Shipping Address
        new syncShipping_address(getContext(), new info() {
            @Override
            public void getInfo(String data) {


                try {

                    SharedPreferences preferences = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

                    String addressId = preferences.getString("address_id",null);

                    JSONObject object = new JSONObject(data);

                    JSONObject address = object.getJSONObject("addresses");

                    JSONObject fields = address.getJSONObject(addressId);

                    String fname = fields.getString("firstname");

                    String lname = fields.getString("lastname");

                    String address1 = fields.getString("address_1");

                    String city = fields.getString("city");

                    String zone = fields.getString("zone");

                    String country = fields.getString("country");

                    String postcode = fields.getString("postcode");

                    defAddDel.setText(fname +" "+ lname + System.lineSeparator() + address1 + System.lineSeparator() + city + System.lineSeparator() + zone + System.lineSeparator() + country +System.lineSeparator() + "PIN : " + postcode  );

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }).execute();


    }



    public void setShippingAddress()
    {
        SharedPreferences preferences1 = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String addressId1 = preferences1.getString("address_id",null);


        String shipping = "shipping_address=existing&address_id="+addressId1;


        new syncSaveShippingAddress(getContext(), shipping, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("shipping Address",data);

            }
        }).execute();

    }





    public void getPaymentMethod()

    {




        String paymentExtension = hosts.paymentMethod;


        new syncCookie(getContext(), paymentExtension, new info() {
            @Override
            public void getInfo(String data) {


                try {

                    JSONObject jsonObject = new JSONObject(data);

                    JSONObject unknown  = null;

                    JSONObject payments = jsonObject.getJSONObject("payment_methods");

                    Iterator<String> KEYS  = payments.keys();

                    int index = -1;


                    while (KEYS.hasNext())
                    {

                        index++;

                        String keyValue = KEYS.next();

                        unknown = payments.getJSONObject(keyValue);

                        String code = unknown.getString("code");

                        String title = unknown.getString("title");

                        index++;
                        radioButton[index] = new RadioButton(getContext());
                        radioButton[index].setTag(code);
                        radioButton[index].setText(title);
                        paymentGroup.addView(radioButton[index]);

                        radioButton[1].setChecked(true);

                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }).execute();




        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View rb = paymentGroup.findViewById(checkedId);

                int selectedId = paymentGroup.getCheckedRadioButtonId();


                paymentOption = rb.findViewById(selectedId);


                String codeValue = paymentOption.getTag().toString();

                Toast.makeText(getContext(),""+codeValue,Toast.LENGTH_SHORT).show();

            }
        });

    }




    public void setPaymentMethod()
    {


        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radios = getView();

                RadioButton paymentButton = radios.findViewById(checkedId);


                switch (paymentButton.getTag().toString())
                {
                    case "instamojo" : sendOPtionsAsInstamojo();
                    break;

                    case "cod" : sendOptionsAsCOD();
                    break;
                }

            }
        });


    }



    public void sendOPtionsAsInstamojo()
    {
        String SelectPayment = "payment_method=instamojo&comment=&agree=1";
        new syncSavePaymentMethod(getContext(), SelectPayment, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Payment Set Instamojo",data);

                confirmOrder();

            }
        }).execute();
    }


    public void sendOptionsAsCOD()
    {
        String SelectPayment = "payment_method=cod&comment=&agree=1";
        new syncSavePaymentMethod(getContext(), SelectPayment, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Payment Set COD",data);

                confirmOrder();

            }
        }).execute();
    }




    public void getShippingMethod()

    {
        String shippingURL = hosts.shippingURL;

        final ArrayList<String> options = new ArrayList<>();

        new syncCookie(getContext(), shippingURL, new info() {
            @Override
            public void getInfo(String data) {


                Log.e("Shipping Method",data);

                try {

                    JSONObject object = new JSONObject(data);

                    JSONObject services = object.getJSONObject("shipping_methods");

                    JSONObject unkn = null;

                    JSONObject unknon2 = null;

                    Iterator<String> iterator = services.keys();

                    int index = -1;

                    while (iterator.hasNext())
                    {

                          index++;

                           String keyValue = (String) iterator.next();

                           unkn = services.getJSONObject(keyValue);

                           JSONObject quote = unkn.getJSONObject("quote");

                           String title = unkn.getString("title");

                           unknon2 = quote.getJSONObject(keyValue);

                           String code = unknon2.getString("code");

                           String rate = unknon2.getString("text");


                           shipping[index] = new RadioButton(getContext());

                           shipping[index].setTag(code);

                           shipping[index].setText(title +"-"+ rate);

                           shippingOPtions.addView(shipping[index]);

                           shipping[0].setChecked(true);

                           options.add(code);


                    }



                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();



        shippingOPtions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                View view = shippingOPtions.findViewById(checkedId);

                int optId = shippingOPtions.getCheckedRadioButtonId();


                delveryOption =  view.findViewById(optId);

                delveryOption.setChecked(true);

                String text = delveryOption.getTag().toString();

                Toast.makeText(getContext(),""+text,Toast.LENGTH_SHORT).show();
            }
        });


    }




    public void setShippingService()

    {
        shippingOPtions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View rad = getView();

                RadioButton shippingOP = rad.findViewById(checkedId);


               switch (shippingOP.getTag().toString())
               {
                   case "free.free" : sendShippingOptionAsFree();
                   break;

                   case "flat.flat" : sendShippingOptionsAsFlat();
                   break;
               }

            }
        });

    }




    public void sendShippingOptionAsFree()
    {

        String comment = "";

        String ship_method = "shipping_method=free.free&comment="+comment;

        new syncSaveShippingMethod(getContext(), ship_method, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("shipping Method Free",data);

                confirmOrder();
            }
        }).execute();

    }


    public void sendShippingOptionsAsFlat()
    {

        String comment = "";

        String ship_method = "shipping_method=flat.flat&comment="+comment;

        new syncSaveShippingMethod(getContext(), ship_method, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("shipping Method Flat",data);
                confirmOrder();
            }
        }).execute();

    }






    public void confirmOrder()
    {

        String confirmURL = hosts.confirmOrder;
        new syncCookie(getContext(), confirmURL, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Confirm Order",data);


                try {

                    JSONObject object = new JSONObject(data);

                    orderId = object.getString("order_id");

                    String orderId = object.getString("order_id");

                    JSONArray productsa = object.getJSONArray("products");





                    for (int i=0; i<productsa.length(); i++)
                    {
                        JSONObject loopsPro = productsa.getJSONObject(i);

                        String cart_id = loopsPro.getString("cart_id");

                        String product_id = loopsPro.getString("product_id");

                        String model = loopsPro.getString("model");

                        String recurring = loopsPro.getString("recurring");

                        String quantity = loopsPro.getString("quantity");

                        String subtract = loopsPro.getString("subtract");

                        String price = loopsPro.getString("price");

                        String total = loopsPro.getString("total");

                        JSONArray totalsArray = object.getJSONArray("totals");


                        JSONArray optionArray = loopsPro.getJSONArray("option");

                        for (int k=0; k< optionArray.length(); k++)
                        {
                           JSONObject jsonObject = optionArray.getJSONObject(k);

                            optionslist.put("name",jsonObject.optString("name"));
                            optionslist.put("value",jsonObject.optString("value"));
                        }


                        breakupbutton.setText("Loading...");

                        int lastindex = totalsArray.length()-1;

                        JSONObject totalvaue = totalsArray.getJSONObject(lastindex);

                        String totalvaues = totalvaue.getString("text");


                        breakupbutton.setText("Total :"+ totalvaues);

                        closeDialog();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }





    public void OrderCOD()
    {
        // order COMPLETE > COD before redirect success
        String cod = hosts.OrderCOD;
        new syncCookie(getContext(), cod, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Order Status",data);

            }
        }).execute();
    }









    public void success()
    {

        //It will wipe off cookies that were made during in  checkout

        //Use it to reset reset

        String successAPI = hosts.successOrder;

        new syncCookie(getContext(), successAPI, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Order Success",data);

                FragmentManager manager = getFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.mainframeL,new success("Thank you for purchasing with us you will receive a confirmation message")).commit();

            }
        }).execute();

    }




    public void setBillingAddress()
    {
        SharedPreferences preferences1 = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String addressId1 = preferences1.getString("address_id",null);


        String existing = "payment_address=existing&address_id="+addressId1;


        new syncSavePayment(getContext(), existing, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("Payment Address",data);

            }
        }).execute();

    }



    public void initializePayment()
    {

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view = getView();

                int value = paymentGroup.getCheckedRadioButtonId();

                paymentOption = view.findViewById(value);


                switch (paymentOption.getTag().toString())

                {
                    case "cod" : cashOnDelivery();
                    break;

                    case "instamojo" : paymentGateway();
                    break;

                }

            }
        });
    }




    public void cashOnDelivery()
    {

        OrderCOD();

        success();

        new syncInfo(getContext(), new info() {
            @Override
            public void getInfo(String data) {

                try {

                    JSONObject jsonObject = new JSONObject(data);

                    String items = jsonObject.getString("text_items");

                    //Textview here to set count to cart basket
                    notify = menutabs.findViewById(R.id.notify_badge);

                    notify.setText(items);

                    Log.e("total",items);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();

        setHasOptionsMenu(true);

        notifyThis("Giveaway4u","Thank you for purchasing with us");

    }




    public void paymentGateway()

    {

        Intent intent = new Intent(getContext(),PayUMoney.class);

        intent.putExtra("order_id",orderId);

        startActivity(intent);


    }




    public void editTextLoader()

    {




    }


    public void notifyThis(String title, String message) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(getContext());
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis()+4)
                .setSmallIcon(R.drawable.logosplash)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("INFO");

        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }





    public void loadcouponbox()
    {


        enterCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertWithInput();

            }
        });

    }



    public void entercoupons(final String code)
    {


        new syncCoupon(getContext(), code, new message() {
            @Override
            public void setMessage(String message) {

                setMessages(message);

                confirmOrder();

            }
        }).execute();

    }


    public void setMessages(String data)

    {


        //alertbox =  alertview.findViewById(R.id.messagealert);

        //iconview = alertview.findViewById(R.id.iconAlert);

        alertbox.setText("Applying Coupon ...");


        try {

            JSONObject mess = new JSONObject(data);


            if (mess.has("redirect"))
            {


                iconview.setImageResource(R.drawable.ic_done);

                alertbox.setText("Your coupon discount has been applied!");
                alertbox.setTextColor(Color.parseColor("#00e348"));

            }

            if (mess.has("error"))
            {

                String error = mess.getString("error");
                iconview.setImageResource(R.drawable.ic_error_black_24dp);
                alertbox.setText(error);
                alertbox.setTextColor(Color.parseColor("#eb0000"));
            }



        }catch (Exception e)
        {

        }

        new sync_get_cart(getContext(), new cart() {
            @Override
            public void loadCarts(String data) {

                CustomAdapter ladapter = new CustomAdapter(getContext(),R.layout.listviews,totaldata);

                ladapter.clear();

                UpdateTotal(data);


            }
        }).execute();



    }



    public void UpdateTotal(String data) {


        // coupon  2767017897

        view = getView();

        try {

            JSONObject object = new JSONObject(data);

            JSONArray array = object.getJSONArray("totals");

            for (int k=0; k < data.length(); k++)
            {
                JSONObject totals = array.getJSONObject(k);

                String title = totals.getString("title");

                String text = totals.getString("text");

                totaldata.add(new list_totals(title,text));

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        CustomAdapter ladapter = new CustomAdapter(getContext(),R.layout.listviews,totaldata);

//
//        listView = view.findViewById(R.id.listviews);
//
//        listView.setAdapter(ladapter);

    }



    public void showAlertWithInput()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Coupon");

// Set up the input
        final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                coupon_string = input.getText().toString();

                entercoupons(coupon_string);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment checkOut.
     */
    // TODO: Rename and change types and number of parameters
    public static checkOut newInstance(String param1, String param2) {
        checkOut fragment = new checkOut();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
