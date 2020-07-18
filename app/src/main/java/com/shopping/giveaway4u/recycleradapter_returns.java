package com.shopping.giveaway4u;


import android.content.Context;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class recycleradapter_returns extends RecyclerView.Adapter <recycleradapter_returns.MyViewHolder>  {


    Context ctx;
    String images;
    JSONArray array;
    config_hosts hosts = new config_hosts();

    ArrayList<String> return_id = new ArrayList<>();
    ArrayList<String> order_id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();
    ArrayList<String> date_added = new ArrayList<>();

    public recycleradapter_returns(Context context, JSONArray data)
    {
        this.ctx = context;
        this.array = data;

        try {

            for (int i=0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                return_id.add(object.getString("return_id"));
                order_id.add(object.getString("order_id"));
                name.add(object.getString("name"));
                status.add("status");
                date_added.add("date_added");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productsimages,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


    }




    @Override
    public int getItemCount() {


        return array.length();


    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        public MyViewHolder( View itemView) {

            super(itemView);

        }

    }


}
