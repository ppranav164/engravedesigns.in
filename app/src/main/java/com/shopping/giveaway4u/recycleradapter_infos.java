package com.shopping.giveaway4u;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class recycleradapter_infos extends RecyclerView.Adapter <recycleradapter_infos.MyViewHolder>  {


    Context ctx;

    JSONArray array;

    ArrayList<String> product_name = new ArrayList<>();

    ArrayList<String> model_no = new ArrayList<>();

    ArrayList<String> price_tag = new ArrayList<>();

    ArrayList<String> prod_id = new ArrayList<>();

    ArrayList<String> thumbnails = new ArrayList<>();

    ArrayList<String> quantitys = new ArrayList<>();


    public recycleradapter_infos(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

       try {

          for (int i=0; i< array.length(); i++)
          {
              JSONObject object = array.getJSONObject(i);
              String name = object.getString("name");
              String model = object.getString("model");
              String price = object.getString("price");
              String quantity = object.getString("quantity");


              product_name.add(name);
              model_no.add(model);
              price_tag.add(price);
              quantitys.add(quantity);
          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }





    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infos,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        holder.name.setText(product_name.get(pos));
        holder.modelTv.setText(model_no.get(pos));
        holder.priceTv.setText(price_tag.get(pos));


    }






    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv;

        ImageView imageView,cart,remove;

        LinearLayout linearLayout;




        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            cart = itemView.findViewById(R.id.cart);

            remove = itemView.findViewById(R.id.remove);

            linearLayout = itemView.findViewById(R.id.llot);

        }

    }


}
