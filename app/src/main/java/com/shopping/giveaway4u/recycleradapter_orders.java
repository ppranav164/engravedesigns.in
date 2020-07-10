package com.shopping.giveaway4u;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_orders extends RecyclerView.Adapter <recycleradapter_orders.MyViewHolder>  {


    Context ctx;

    config_hosts hosts = new config_hosts();

    String link;

    View view;


    JSONArray array;

    ArrayList<String> product_name = new ArrayList<>();

    ArrayList<String> model_no = new ArrayList<>();

    ArrayList<String> stock_avl = new ArrayList<>();

    ArrayList<String> special = new ArrayList<>();

    ArrayList<String> price_tag = new ArrayList<>();

    ArrayList<String> prod_id = new ArrayList<>();

    ArrayList<String> thumbnails = new ArrayList<>();

    ArrayList<String> removeLink = new ArrayList<>();

    ArrayList<String> orderId = new ArrayList<>();

    ArrayList<String> dateOfOrder = new ArrayList<>();

    ArrayList<String> brand = new ArrayList<>();


    public recycleradapter_orders(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

       try {

          for (int i=0; i< array.length(); i++)
          {
              JSONObject object = array.getJSONObject(i);

              String ida = object.getString("product_id");

              String name = object.getString("name");

              String stock = object.getString("status");

              String price = object.getString("total");

              String images = object.getString("image");

              String order_id = object.getString("order_id");

              String dated = object.getString("date_added");

              String status = object.getString("status");

              String branddetail = object.getString("brand");

              product_name.add(name);
              stock_avl.add(stock);
              price_tag.add(price);
              prod_id.add(ida);
              thumbnails.add(images);

              orderId.add(order_id);
              dateOfOrder.add(dated);
              brand.add(branddetail);


          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }




    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        holder.name.setText(brand.get(pos));

        holder.dateviewTv.setText("Order Date : "+dateOfOrder.get(pos));

        holder.orderIdtv.setText("Order ID : #"+orderId.get(pos));

        holder.stockTv.setText(stock_avl.get(pos));

        holder.priceTv.setText(price_tag.get(pos));

        holder.shipto.setText(product_name.get(pos));

        Picasso.get().load(thumbnails.get(pos)).into(holder.imageView);



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_id = prod_id.get(holder.getAdapterPosition());

                Bundle bundle = new Bundle();

                bundle.putString("_id",product_id);

                Fragment fragment = new products_fragment();

                fragment.setArguments(bundle);

                FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.mainframeL,fragment);
                transaction.commit();


            }
        });

    }




    public void syncRemove(String id)
    {


         link = hosts.wishlist_remove+id;

         Log.e("remove wishlist id",id);

         Log.e("link wishlist remove",link);

         new syncRemoveWishlist(ctx,link,"GET").execute();


    }



    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv,shipto,orderIdtv,dateviewTv;

        ImageView imageView,cart,remove;

        LinearLayout linearLayout;




        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            shipto = itemView.findViewById(R.id.customer);


            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            cart = itemView.findViewById(R.id.cart);

            linearLayout = itemView.findViewById(R.id.llot);

            orderIdtv = itemView.findViewById(R.id.orderid);

            dateviewTv = itemView.findViewById(R.id.datedoforder);


        }

    }


}
