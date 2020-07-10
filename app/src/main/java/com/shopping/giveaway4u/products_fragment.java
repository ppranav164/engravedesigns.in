package com.shopping.giveaway4u;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SyncInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Result;

public class products_fragment extends Fragment {

    private static final int RESULT_OK = 1 ;
    View view;

    View entireView;

    ImageButton wishlist;

    TextView textView,priceView,stockView,cid,offer,dfilename;

    LinearLayout linearLayout;

    Button UploadBtn;

    TextView descp;

    ImageView previewImg;

    RecyclerView recyclerView;

    PhotoView photoView;

    Context context;

    specFragment specFragment;

    JSONArray reviewsJson;

    private ImageView imageView,upimageshow;

    private Bitmap bitmap;

    private Uri filePath;

    EditText editText;

    TextView codd;

    String uploadCodes;


     int product_option_id;

    FrameLayout frameLayout;

    TabLayout tabLayout;

    Button cartBtn;

    public static   String descriptions,reviews,tabTexts;


    String fulldata;

    boolean isFileRequired;

    boolean isCheckRequired;

    boolean isChecked;

    boolean error;

    TextView notify;

    Cart_details cart_details;

    private String cartCount;


    View menutabs;

    LinearLayout radioGroup;

    RadioButton[] radioButtons = new RadioButton[5];

    RadioGroup[] radioGroups = new RadioGroup[5];

    RadioButton rbutton;

    TextView radiotxt;

    boolean success;

    TextView[] radioHeader = new TextView[5];

    EditText qtext;


    ImageButton plus,minus;

    int num=0;

    int limit;

    String link;

    config_hosts hosts  = new config_hosts();

    ArrayList<String> products_id = new ArrayList<>();


    int product_options_id = 0; //227 for file options[227] = code // gf44gr44e434ww435df345234523452345

    recycleradapter_cart cart;


    Map<Integer,String> keyValue = new HashMap<>();

    String param = "";


    HashMap<Integer,String> uploadKeys = new HashMap<>();



    RadioGroup customRadio;

    LinearLayout uploadlayout;

    Button[] uploadButton  = new Button[5];

    LinearLayout[] linearLayouts = new LinearLayout[5];

    TextView[] codeView = new TextView[5];

    TextView textViewcode;

    String options ="";

    ImageView imageViews;


    LinearLayout previewLayout;


    boolean hasPermission;


    public products_fragment() {

           //    default

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.layout_products,container,false);


        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //super.onCreateOptionsMenu(menu, inflater);

        //getActivity().getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);


        menutabs = MenuItemCompat.getActionView(menuItem);


    }






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        final View view = getView();



        qtext = view.findViewById(R.id.quantity);

        qtext.setText(String.valueOf(limit));


        quanityButton(view);


        cartBtn = view.findViewById(R.id.addToCart);


        uploadlayout = view.findViewById(R.id.uploadbox);


        previewLayout = view.findViewById(R.id.preview);


        frameLayout = view.findViewById(R.id.framwla);

        tabLayout = view.findViewById(R.id.mytabs);

        TabLayout.Tab firstTab = tabLayout.newTab();

        TabLayout.Tab secTab = tabLayout.newTab();

        TabLayout.Tab ThTab = tabLayout.newTab();

        firstTab.setText("Description");

        ThTab.setText("Reviews");

        tabLayout.addTab(firstTab);
        tabLayout.addTab(ThTab);

        tabLayout.getTabAt(0).select();

        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.framwla,new specFragment());

        transaction.commit();

        final Boolean[] clicked = {false};





         setWishlist(view);


         checkPermissionForReadExtertalStorage();


         hasPermission = checkPermissionForReadExtertalStorage();



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                Fragment fragment = null;

                int id = tab.getPosition();

                if (id == 0)
                {
                    fragment = new specFragment(descriptions);

                }else if (id == 1)
                {
                    fragment = new reviewsFragment(reviewsJson,getContext());

                    if (reviewsJson.isNull(0)){
                        Snackbar.make(view,"No reviews found for this product !",Snackbar.LENGTH_SHORT).show();
                    }

                }


                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

                transaction.replace(R.id.framwla,fragment);


                transaction.commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Bundle bundle = getArguments();

        if (bundle !=null)
        {
            String res = bundle.getString("_id");

            //Toast.makeText(getContext(),"Id is "+res,Toast.LENGTH_LONG).show();

            new SyncProduct(getContext(), new products() {
                @Override
                public void loadProductInfo(String data) {

                    fulldata = data;


                    setProductInfo(data);

                    try {

                        JSONObject object = new JSONObject(data);

                        setUpCart(object ,cartBtn);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                }
            },res).execute();

        }




        isWishlistExists();


        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //Toast.makeText(getContext(),"Products Fragment",Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStackImmediate();
                        return true;
                    }
                }
                return false;
            }
        });





        wishlist = view.findViewById(R.id.wishlistB);



    }




    public void setWishlist(View view)
    {


        wishlist = view.findViewById(R.id.wishlistB);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = wishlist.getTag().toString();


                switch (tag)
                {
                    case "yellow" :  wishlist.setBackgroundResource(R.drawable.wishlistp);
                                     wishlist.setTag("red");
                                     addWishlist();
                                     break;

                    case "red"    :  wishlist.setBackgroundResource(R.drawable.wishlist);
                                     wishlist.setTag("yellow");
                                     removeWishlist();
                                     break;
                }


            }
        });


    }




    public void isWishlistExists()
    {


        wishlist.setBackgroundResource(R.drawable.wishlist);

        wishlist.setTag("yellow");


        new sync_get_wishlist(getContext(), new wishlist() {
            @Override
            public void loadWishlist(String data) {
                try {

                    JSONObject object = new JSONObject(data);

                    JSONArray products = object.getJSONArray("products");

                    for (int i=0; i < products.length(); i++)
                    {
                       JSONObject id = products.getJSONObject(i);

                       String product_id = id.getString("product_id");

                       listWishlists(product_id);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();



    }



    public void listWishlists(String products)
    {
        products_id.add(products);

        cid = view.findViewById(R.id.cid);

        String id = cid.getText().toString();


        for (int i=0; i < products_id.size(); i++)
        {

            String data = products_id.get(i);

            if (data.contains(id))
            {
                wishlist.setBackgroundResource(R.drawable.wishlistp);
                wishlist.setTag("red");
            }
        }

    }




    public void addWishlist()
    {

        cid = view.findViewById(R.id.cid);

        int id = Integer.parseInt(cid.getText().toString());

        link = hosts.wishlist_remove+id;

        new syncWishlist(getContext(),id).execute();

        Snackbar snackbar = Snackbar.make(view,"Added to Wishlist",Snackbar.LENGTH_LONG);

        View snackView = snackbar.getView();

        snackView.setBackgroundColor(Color.parseColor("#008c48"));

        snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));

        snackbar.setAction("View Wishlist", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager manager = getFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.mainframeL,new WishlistFragment(),"products");
                transaction.addToBackStack("products");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

            }
        });

        snackbar.show();

    }



    public void removeWishlist()
    {

        cid = view.findViewById(R.id.cid);

        String id = cid.getText().toString();

        link = hosts.wishlist_remove+id;

        new syncRemoveWishlist(getContext(),link,"GET").execute();

        Snackbar snackbar = Snackbar.make(view,"Removed successfully",Snackbar.LENGTH_SHORT);

        snackbar.show();



    }




    public void fileChooser(int requestCode)
    {

       if (hasPermission !=false)
       {
           Intent intent = new Intent();

           intent.setType("image/*");

           intent.setAction(Intent.ACTION_GET_CONTENT);

           startActivityForResult(Intent.createChooser(intent,"Select a Image"),requestCode);

       }else {


           Toast.makeText(getContext(),"You don not have granted permission access storage",Toast.LENGTH_SHORT).show();

          try {
              requestPermissionForReadExtertalStorage();

              hasPermission = true;

          }catch (Exception e)
          {
              e.printStackTrace();
          }


       }



    }






    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();

            //dfilename = view.findViewById(R.id.filename);

            String selectedFilePath = FilePath.getPath(getContext(),data.getData());

            String[] parts = selectedFilePath.split("/");

            final String fileName = parts[parts.length - 1];

            int plus = 1;

            LinearLayout.LayoutParams previewparam = new LinearLayout.LayoutParams(300,300);

            previewparam.setMargins(10,10,10,10);


            ImageView imageView = new ImageView(getContext());
            imageView.setId(plus+requestCode);
            imageView.setLayoutParams(previewparam);
            previewLayout.addView(imageView);
            ImageView screen = (ImageView) previewLayout.findViewById(plus+requestCode);
            screen.setImageURI(filePath);


            //dfilename.setText(fileName);

            codd = view.findViewById(R.id.codes);

            new syncUpload_image(getContext(),filePath, new upload() {
                @Override
                public void status(String status) {

                    try {

                        JSONObject object = new JSONObject(status);
                        String code = object.getString("code");
                        codd.setText(code);
                        uploadCodes = code;
                        keyValue.put(requestCode,code);
                        Log.e("final",keyValue.toString());
                        if (code == null)
                        {
                            Toast.makeText(getContext(),"Unsupported File or Something is wrong with image",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();


            cid = view.findViewById(R.id.cid);


        }


    }



    public void setUploadCode(int requestCode,String code)
    {
        StringBuilder builder = new StringBuilder();

        String data = "option["+requestCode+"]="+code+"&";

        builder.append(data);

    }



    public  void setDescriptions(String data)
    {
        descriptions = data;
    }

    public void setTabTexts(String data)
    {
        tabTexts = data;
    }

    public void setReviewData(JSONArray array)
    {

        this.reviewsJson = array;
    }


    
   public void setProductInfo(String data)

   {


       try {

           JSONObject object = new JSONObject(data);

           parseData(object);



       }catch (Exception e)
       {
           e.printStackTrace();
       }



   }



   public void parseData(JSONObject objects)
   {





       try {


           String mfg = objects.getString("heading_title");

           String price = objects.getString("price");

           String stock = objects.getString("stock");

           String descps = objects.getString("description");

           String reviewTabtxt = objects.getString("tab_review");

           JSONArray review_data = objects.getJSONArray("reviews");

           String image = objects.getString("thumb");

           String pid = objects.getString("product_id");

           String cutPrice = objects.getString("special");

           String minimum = objects.getString("minimum");

           limit = Integer.parseInt(minimum);


           setReviewData(review_data);

           setDescriptions(descps);

           setTabTexts("loooooo");


           photoView = view.findViewById(R.id.previewer);

           textView = view.findViewById(R.id.display);

           priceView = view.findViewById(R.id.priceView);

           stockView = view.findViewById(R.id.stock);

           descp = view.findViewById(R.id.desp);

           cid = view.findViewById(R.id.cid);

           offer = view.findViewById(R.id.special);







           //photoView.setImageResource(R.drawable.logo);

           Picasso.get().load(image).into(photoView);


           cid.setText(pid);

           textView.setText(mfg);

           if (cutPrice == "false"){

               priceView.setText(price);

               offer.setVisibility(View.GONE);

           }else {

               priceView.setText(price);
               offer.setText(cutPrice);

               Paint paint = new Paint();
               paint.setColor(Color.parseColor("#ff0000"));
               paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

               priceView.setPaintFlags(paint.getFlags());

           }






           cartBtn = getView().findViewById(R.id.addToCart);


           if (stock.matches("Out Of Stock"))
           {

               stockView.setText("Not Available");

               stockView.setTextColor(getResources().getColor(R.color.red));
               cartBtn.setEnabled(false);
               cartBtn.setBackgroundColor(getResources().getColor(R.color.grey));
               cartBtn.setText(stock);

               cartBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(getContext(),"Not available",Toast.LENGTH_LONG).show();
                   }
               });
           }else {

               stockView.setText(stock);
           }



          if (descps.contains("&nbsp;"))
          {
              descps = descps.replace("&nbsp;"," ");
          }


           descp.setText(descps);


           Map<String,String> radiokeys = new HashMap<>();

           radioGroup = view.findViewById(R.id.radios);

           JSONArray optionsArray = objects.getJSONArray("options");

           ArrayList<String> radioidlistId = new ArrayList<>();

           ArrayList<String> radiolistTitile = new ArrayList<>();


           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,

                   LinearLayout.LayoutParams.WRAP_CONTENT);

           LinearLayout.LayoutParams groupmargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,

                   LinearLayout.LayoutParams.WRAP_CONTENT);




           for (int l=0; l < optionsArray.length(); l++)
          {
              JSONObject optionObj = optionsArray.getJSONObject(l);

              String type = optionObj.getString("type");

              String title = optionObj.getString("title");

              String optionId = optionObj.getString("id");

              radiotxt = view.findViewById(R.id.radioid);

              radiotxt.setText(optionId);

              radioHeader[l] = new TextView(getContext());
              radioHeader[l].setText(title);
              radioHeader[l].setLayoutParams(groupmargin);
              radioHeader[l].setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL);



              radioGroup.addView(radioHeader[l]);



              radioGroups[l] = new RadioGroup(getContext());
              radioGroups[l].setTag(title);
              radioGroups[l].setId(Integer.parseInt(optionId));

              radioGroup.addView(radioGroups[l]);





              LinearLayout.LayoutParams radioparams = new LinearLayout.LayoutParams(300,LinearLayout.LayoutParams.WRAP_CONTENT);

              radioparams.setMargins(0,100,0,0);




              JSONArray items = optionObj.getJSONArray("items");

              for (int p=0; p < items.length(); p++)
              {
                  JSONObject itemObj = items.getJSONObject(p);

                  String id = itemObj.getString("id");

                  String value = itemObj.getString("value");



                  radioButtons[p] = new RadioButton(getContext());
                  radioButtons[p].setButtonDrawable(android.R.color.transparent);
                  radioButtons[p].setBackgroundResource(R.drawable.radio_background);
                  radioButtons[p].setLayoutParams(params);
                  radioButtons[p].setPadding(20,20,20,20);

                  radioButtons[p].setId(Integer.parseInt(id));
                  radioButtons[p].setTag(id);
                  radioButtons[p].setText(value);
                  radioButtons[0].setChecked(true);
                  radioButtons[p].setLayoutParams(radioparams);
                  radioGroups[l].addView(radioButtons[p]);
                  radioButtons[p].setGravity(Gravity.CENTER);


                  int key = radioGroups[l].getId();

                  String values = String.valueOf(radioGroups[l].getCheckedRadioButtonId());


                  keyValue.put(key,values);

                  isChecked = true;


                  radioGroups[l].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                      @Override
                      public void onCheckedChanged(RadioGroup group, int checkedId) {


                          int who = group.getId();

                          String rbId = String.valueOf(checkedId);

                          keyValue.put(who,rbId);

                          isChecked = true;

                      }
                  });




              }


              if (type.equals("radio"))
              {

                  int total = radiokeys.size();
                  isCheckRequired = true;
                  Iterator it = radiokeys.entrySet().iterator();


              }



          }




           linearLayout = view.findViewById(R.id.uploadbox);



          for (int i=0; i < objects.length(); i++)
          {

              JSONObject optb = objects.getJSONObject("upload");

              JSONArray array = optb.getJSONArray("file");

              for (int k=0; k<array.length(); k++)
              {

                  JSONObject jsonObject = array.getJSONObject(k);

                  String type = jsonObject.getString("type");

                  String uploadId = jsonObject.getString("product_option_id");

                  String upload_title = jsonObject.getString("name");

                  int upload_id = Integer.parseInt(uploadId);

                  uploadKeys.put(upload_id,upload_title);


                  if (type.equals("file"))
                  {
                      linearLayout.setVisibility(View.VISIBLE);

                      isFileRequired = true;

                      product_options_id = Integer.parseInt(uploadId);

                  }


              }


          }


          LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          Iterator iterator = uploadKeys.entrySet().iterator();

          int index =-1;

           TextView[] texts = new TextView[2];

           texts[0] = new TextView(getContext());

           texts[0].setText("File Upload :");

           uploadlayout.addView(texts[0]);

          while (iterator.hasNext())
          {

              index++;

              Map.Entry pair = (Map.Entry) iterator.next();

              String value = pair.getValue().toString();

              int id = Integer.parseInt(pair.getKey().toString());

              linearLayouts[index] = new LinearLayout(getContext());
              linearLayouts[index].setLayoutParams(params1);
              linearLayouts[index].setId(id);

              uploadlayout.addView(linearLayouts[index]);


              uploadButton[index] = new Button(getContext());
              uploadButton[index].setTag(id);
              uploadButton[index].setText(value);
              uploadButton[index].setBackgroundResource(R.drawable.upload_active);



              uploadlayout.addView(uploadButton[index]);


              uploadButton[index].setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                     String opt = v.getTag().toString();

                     int code = Integer.parseInt(opt);

                     fileChooser(code);

                  }
              });


          }



           recyclerView = view.findViewById(R.id.thumb_recyclerview);

           recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, LinearLayoutManager.HORIZONTAL,false));



       }catch (Exception e)
       {
           e.printStackTrace();
       }



   }



   public void setUpCart(final JSONObject data, final View view)

   {





       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               entireView = getView();


               qtext = entireView.findViewById(R.id.quantity);


               Log.e("Cart ",keyValue.toString());



              if (success !=false)
              {
                  redirectHome();
              }

               int cart_pro_id = 0;

               int quantity = Integer.parseInt(qtext.getText().toString());


               radiotxt = entireView.findViewById(R.id.radioid);

               String id = radiotxt.getText().toString();


               if (isCheckRequired == true && isChecked != true)
               {

                   error = true;

                   Toast.makeText(getContext(),"Please check color option",Toast.LENGTH_SHORT).show();

                   Log.e("isChecked",String.valueOf(isChecked));
                   Log.e("chech required",String.valueOf(isCheckRequired));

               }

               else if (isFileRequired != false && uploadCodes == null) {


                   Snackbar.make(view,"Please Upload a File/Image",Snackbar.LENGTH_SHORT).show();

                   error = true;

               }else {

                   error = false;
               }




               try {

                      cart_pro_id = data.getInt("product_id");

                      StringBuilder stringBuilder = new StringBuilder();

                      Iterator iterator = keyValue.entrySet().iterator();


                      while (iterator.hasNext())
                      {
                          Map.Entry pair = (Map.Entry) iterator.next();

                          int key = Integer.parseInt( pair.getKey().toString());

                          String values = pair.getValue().toString();

                          keyValue.put(key,values);

                          String params = "option["+key+"]="+values+"&";

                          stringBuilder.append(params);

                      }


                      options = stringBuilder.toString();

                      options += "product_id="+cart_pro_id+"&quantity="+quantity;


               }catch (Exception e)
               {
                   e.printStackTrace();
               }


               Log.e("isChecked",String.valueOf(isChecked));
               Log.e("chech required",String.valueOf(isCheckRequired));


               if (error != true)

               {
                   new syncCartAdd(getContext(),options, new addedToCart() {
                       @Override
                       public void loadCarts(String data) {


                           try {

                               JSONObject messageobj = new JSONObject(data);

                               String message = messageobj.getString("status");

                               Log.e("Server Response",message);



                               if (message == "success")
                               {
                                  success = true;


                               }

                           }catch (Exception e)
                           {
                               e.printStackTrace();
                           }

                       }
                   }).execute();


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

                   Snackbar.make(view,"Item added to cart",Snackbar.LENGTH_SHORT).show();

                   redirectHome();



               }


           }

       });



   }



   public void redirectHome()

   {
       Fragment fragment = new fragment_main();

       FragmentManager manager = getFragmentManager();

       FragmentTransaction transaction = manager.beginTransaction();

       transaction.replace(R.id.mainframeL,fragment,"home");
       transaction.addToBackStack("home");
       transaction.commit();

   }



   public void quanityButton(View views)

   {


       plus = views.findViewById(R.id.plus);

       minus = views.findViewById(R.id.minus);

       qtext = views.findViewById(R.id.quantity);

       qtext.setText(String.valueOf(1));


       plus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               num++;

               setPlus();

               Toast.makeText(getContext(),"Minimum "+limit,Toast.LENGTH_SHORT).show();

           }
       });


       minus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               setMinus();
           }
       });

   }






   public void setPlus()

   {

       entireView = getView();

       qtext = entireView.findViewById(R.id.quantity);

       if (num > 0)
       {
           num = Integer.parseInt(qtext.getText().toString());

           num++;
       }

       if (num > 10)
       {
           num = 10;

           Toast.makeText(getContext(),"You have reached quanity limit",Toast.LENGTH_SHORT).show();
       }

       qtext.setText(String.valueOf(num));

   }




    public void setMinus()

    {

        entireView = getView();

        qtext = entireView.findViewById(R.id.quantity);

        int currentValue = Integer.parseInt(qtext.getText().toString());

        int subs = currentValue - 1;

        if (subs < 1)
        {
            subs = limit;

            //set to 1 if a user is going to decrement less than 1
        }

        qtext.setText(String.valueOf(subs));

    }


    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            return result == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
