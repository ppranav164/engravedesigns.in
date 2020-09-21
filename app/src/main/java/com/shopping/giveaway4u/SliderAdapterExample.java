package com.shopping.giveaway4u;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.sliderViewholders> {


    Context context;

    ArrayList<String> urlist = new ArrayList<>();

    ArrayList<String> productId = new ArrayList<>();

    JSONArray array;


    public SliderAdapterExample(Context context, JSONArray array) {

        this.context = context;

        this.array = array;

        try {

            for (int i=0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String title = object.getString("title");

                String image = object.getString("image");

                String product_id = object.getString("product_id");

                productId.add(product_id);
                urlist.add(image);

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public sliderViewholders onCreateViewHolder(ViewGroup parent) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageslider_layout, null);
        return new sliderViewholders(inflate);
    }

    @Override
    public void onBindViewHolder(sliderViewholders viewHolder, int position) {


        //Picasso.get().load(urlist.get(position)).into(viewHolder.imageView);

        viewHolder.imageView.setTag(productId.get(position));

        Glide.with(viewHolder.itemView).load(urlist.get(position))
                .into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = v.getTag().toString();

                Bundle bundle = new Bundle();

                bundle.putString("_id",id);

                Fragment fragment = new products_fragment();

                fragment.setArguments(bundle);

                FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.mainframeL,fragment,"product");
                transaction.addToBackStack("main");

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.commit();

            }
        });

    }

    @Override
    public int getCount() {
        return array.length();
    }

    public class sliderViewholders extends SliderViewAdapter.ViewHolder
    {

        ImageView imageView;

        public sliderViewholders(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageslr);
        }
    }



}
