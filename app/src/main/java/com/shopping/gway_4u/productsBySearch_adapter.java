package com.shopping.gway_4u;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class productsBySearch_adapter extends RecyclerView.Adapter <productsBySearch_adapter.MyViewHolder>  {


    Context ctx;


    JSONArray array;


    String TAG = "productsBySearch_adapter";


    public productsBySearch_adapter(Context context, JSONArray data)
    {
        this.ctx = context;

        this.array = data;

    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        Log.e("TAG",TAG);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbyproducts,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        ArrayList<String> urlist = new ArrayList<>();

        ArrayList<String> tlist = new ArrayList<>();

        ArrayList<String> plist = new ArrayList<>();

        final ArrayList<String> Llist = new ArrayList<>();

        final ArrayList<String> ids = new ArrayList<>();

       try {

           for (int i=0; i<array.length(); i++)
           {
               JSONObject idobj = array.getJSONObject(i);

               String prod = idobj.getString("thumb");

               String name = idobj.getString("name");

               String price = idobj.getString("price");

               String link = idobj.getString("href");

               String id = idobj.getString("product_id");

               urlist.add(prod);
               tlist.add(name.replace("&quot;",""));
               plist.add(price);
               ids.add(id);

               Llist.add(link);

           }

           //Toast.makeText(ctx,"Data "+urlist,Toast.LENGTH_LONG).show();

           Picasso.get().load(urlist.get(pos)).into(holder.imageView);

           holder.textView.setText(tlist.get(pos));

           holder.Tprice.setText(plist.get(pos));



       }catch (Exception e )
       {
           e.printStackTrace();
       }


        //Picasso.get().load(data[pos]).into( holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ids.get(holder.getPosition());
                showProduct(id);
            }
        });



    }


    public void showProduct(String id)
    {
        ctx.startActivity(new Intent(ctx,MainActivity.class).putExtra("tag",id));
    }


    @Override
    public int getItemCount() {


        return array.length();


    }





    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;

        TextView textView; TextView Tprice;

        RelativeLayout linearLayout;


        public MyViewHolder( View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.imageContainer);

            textView = itemView.findViewById(R.id.details);

            Tprice = itemView.findViewById(R.id.price);

            linearLayout = itemView.findViewById(R.id.lu);


        }

    }


}
