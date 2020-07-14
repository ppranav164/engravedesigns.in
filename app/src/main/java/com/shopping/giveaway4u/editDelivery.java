package com.shopping.giveaway4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link editDelivery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link editDelivery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editDelivery extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView idview;

    Button saveaddressbtn;

    config_hosts hosts = new config_hosts();

    public editDelivery() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editDelivery.
     */
    // TODO: Rename and change types and number of parameters
    public static editDelivery newInstance(String param1, String param2) {
        editDelivery fragment = new editDelivery();
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
        return inflater.inflate(R.layout.fragment_edit_delivery, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        idview = view.findViewById(R.id.addressID);
        saveaddressbtn = view.findViewById(R.id.savenewAddress);


        SharedPreferences addressgroup = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String idee = addressgroup.getString("address_id",null);

        idview.setText(idee);

        saveAddress();

        getCountry();
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



    public void saveAddress()
    {

        saveaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             new syncSavePayment(getContext(), "", new info() {
                 @Override
                 public void getInfo(String data) {

                     Log.e("editDelivery",data);
                 }
             }).execute();
            }
        });

    }


    public  void  getCountry()
    {

        String country = hosts.country;

        new syncCountry(getContext(),country, new info() {
            @Override
            public void getInfo(String data) {

                Log.e("country",data);
            }
        }).execute();
    }




    public void alert(String message,int duration)
    {
        Toast.makeText(getContext(),message,duration).show();
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
