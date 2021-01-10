package com.shopping.gway_4u;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class categoriesAdapter extends RecyclerView.Adapter <categoriesAdapter.MyViewHolder>  {


    Context ctx;
    JSONArray categories;
    config_hosts hosts = new config_hosts();
    HashMap<String,String> keyvalue = new HashMap<>();
    ArrayList<String> currentProductId = new ArrayList<>(); // products listed by recyclerview
    HashMap<String,String> keyvals = new HashMap<>();
    ArrayList<String> wishlists;


    public categoriesAdapter(Context context, JSONArray categories)
    {
        this.ctx = context;
        this.categories = categories;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


       try {

           JSONObject objects = categories.getJSONObject(pos);
           String name = objects.getString("name");
           String icon = objects.getString("icon");

           holder.textView.setText(name);
           holder.imageView.setTag(name);
           Picasso.get().load(icon).into(holder.imageView);

           holder.imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   menuSearch(v.getTag().toString());
               }
           });

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }



    public boolean menuSearch(String tag)
    {

        ctx.startActivity(new Intent(ctx,searchProduct.class).putExtra("tag",tag));
        return true;
    }

    @Override
    public int getItemCount() {
        return categories.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;

        TextView textView;

        public MyViewHolder( View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.iconcontainer);

            textView = itemView.findViewById(R.id.icontext);

        }

    }


}
