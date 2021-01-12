package com.shopping.gway_4u;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link more_product.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link more_product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class more_product extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String paramt = "";

    config_hosts hosts = new config_hosts();

    RecyclerView recyclerView;

    Dialog dialog;

    LinearLayout filterlayout;

    Button sortButton;

    RadioGroup filtergroup;

    Context context;

    HashMap<String,String> params = new HashMap<>();

    boolean isUp = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    JSONArray product_array;

    public more_product() {
        // Required empty public constructor
    }

    public more_product(String query, Context actvity)
    {
        paramt = query;
        this.context = actvity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchContents.
     */
    // TODO: Rename and change types and number of parameters
    public static more_product newInstance(String param1, String param2) {
        more_product fragment = new more_product();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        View view = getView();

        dialog = new Dialog(getContext());
        openDialog();

        filterlayout = view.findViewById(R.id.filters);

        sortButton = view.findViewById(R.id.sort);

        filtergroup = view.findViewById(R.id.filtergroup);

        recyclerView = view.findViewById(R.id.vrecyclerview);

        loadData(paramt);

        filterlayout.setVisibility(View.INVISIBLE);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpFilters();
            }
        });

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


    public void showUpFilters()
    {

        if (!isUp)
        {
            Animation slideUp = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
            filterlayout.startAnimation(slideUp);
            filterlayout.setVisibility(View.VISIBLE);

            TranslateAnimation animatie = new TranslateAnimation(0,0,filterlayout.getHeight(),0);
            animatie.setDuration(500);
            animatie.setFillAfter(true);
            filterlayout.startAnimation(animatie);
            isUp = true;

        }else {

            isUp = false;
            Animation slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);

            TranslateAnimation animatie = new TranslateAnimation(0,0,0,filterlayout.getHeight());
            animatie.setDuration(500);
            animatie.setFillAfter(true);
            filterlayout.startAnimation(animatie);
            filterlayout.setVisibility(View.INVISIBLE);
        }


        filtergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = group.findViewById(checkedId);
                String tag = radioButton.getTag().toString();

                switch (tag)
                {
                    case "high" : params.put("sort","p.price");
                                  params.put("order","DESC");
                                  setFilter();
                                  showUpFilters();
                                  break;

                    case "low" : params.put("sort","p.price");
                                 params.put("order","ASC");
                                 setFilter();
                                 showUpFilters();
                                 break;


                    case "ratinghigh" : params.put("sort","rating");
                                        params.put("order","DESC");
                                        setFilter();
                                        showUpFilters();
                                        break;

                    case "ratinglow" : params.put("sort","rating");
                                       params.put("order","ASC");
                                       setFilter();
                                       showUpFilters();
                                       break;

                    case "default" :

                                       params.put("order","");
                                       params.put("sort","");

                                       setFilter();
                                       showUpFilters();
                                       break;
                }

            }
        });

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
        return inflater.inflate(R.layout.fragment_search_contents, container, false);
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


    public void loadData(String params)
    {
       switch (params)
       {
           case "featured" : showByFeatured(null);
           break;
           case "latest" : showByLatest(null);
           break;
           case  "special" : showBySpecial(null);
           break;
       }
    }



    public void showByFeatured(String defaultParam)
    {
        String URL = hosts.FEATURED_MORE;

        new syncAsyncTask(getContext(), "POST", URL,defaultParam, new jsonObjects() {
            @Override
            public void getObjects(String object) {
                Log.e("showByFeatured",object);
                setsearchdata(object);
            }
        }).execute();
    }

    public void showByLatest(String defaultParam)
    {
        String URL = hosts.LATEST_MORE;
        new syncAsyncTask(getContext(), "POST", URL, defaultParam, new jsonObjects() {
            @Override
            public void getObjects(String object) {
                Log.e("showByLatest",object);
                setsearchdata(object);
            }
        }).execute();
    }

    public void showBySpecial(String defaultParam)
    {
        String URL = hosts.SPECIAL_MORE;
        new syncAsyncTask(getContext(), "POST", URL,defaultParam, new jsonObjects() {
            @Override
            public void getObjects(String object) {
                Log.e("showBySpecial",object);
                setsearchdata(object);
            }
        }).execute();
    }



    public void setFilter()
    {
       String sort = params.get("sort");
       String order = params.get("order");
       Log.e("filter",sort+order);
       String keyvals = "sort="+sort+"&order="+order;
       Log.e("param",keyvals);
       filter(paramt,keyvals);
    }


    public void filter(String code,String params)
    {

        Log.e("filter code",code);

        switch (code)
        {
            case "featured" : showByFeatured(params);
                break;
            case "latest" : showByLatest(params);
                break;
            case  "special" : showBySpecial(params);
                break;
        }
    }



    public void setsearchdata(String info)
    {
        try {

            JSONObject object = new JSONObject(info);
            JSONArray array = object.getJSONArray("products");
            product_array = array;

            Log.e("product_array",array.toString());

            if (array.length() > 0)
            {
                setsetsearchInfo(array);
            }else {

                dialog.dismiss();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public void setsetsearchInfo(JSONArray array)

    {

        View view = getView();

        recyclerView  =  view.findViewById(R.id.vrecyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false));

        recyclerView.smoothScrollToPosition(0);


        productsBySearch_adapter recadapter = new productsBySearch_adapter(context,array);

        recyclerView.setAdapter(recadapter);

        recadapter.notifyDataSetChanged();

        closeDialog();


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
