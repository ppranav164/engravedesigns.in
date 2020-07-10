package com.shopping.giveaway4u;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link searchContents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link searchContents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchContents extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String paramt = "";

    RecyclerView recyclerView;

    Dialog dialog;

    LinearLayout filterlayout;

    Button sortButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public searchContents() {
        // Required empty public constructor
    }

    public searchContents(String query)
    {
        paramt += "search="+query;
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
    public static searchContents newInstance(String param1, String param2) {
        searchContents fragment = new searchContents();
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

        filterlayout = view.findViewById(R.id.filters);

        sortButton = view.findViewById(R.id.sort);

        dialog = new Dialog(getContext());

        openDialog();

        loadData(paramt);

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

        if (filterlayout.getVisibility() != View.VISIBLE)
        {
            filterlayout.setVisibility(View.VISIBLE);
        }else {

            filterlayout.setVisibility(View.INVISIBLE);
        }

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

        new syncSearch(getContext(), params, new info() {
            @Override
            public void getInfo(String data) {
                setsearchdata(data);
            }
        }).execute();

    }



    public void setsearchdata(String info)
    {


        try {

            JSONObject object = new JSONObject(info);

            JSONArray array = object.getJSONArray("products");

            setsetsearchInfo(array);


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


        productsBySearch_adapter recadapter = new productsBySearch_adapter(getContext(),array);

        recyclerView.setAdapter(recadapter);

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
