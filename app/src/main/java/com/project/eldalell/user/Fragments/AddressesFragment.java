package com.project.eldalell.user.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Adapter.AddressesAdapter;
import com.project.eldalell.user.Classes.Address;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddressesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddressesFragment() {
    }


    public static AddressesFragment newInstance(String param1, String param2) {
        AddressesFragment fragment = new AddressesFragment();
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

    public static RecyclerView rvAddresses;
    public static ArrayList<Address> AddressesList;
    TextView tvTitle, tvTitleNext,tvAddAddress;
    ImageView search, imgBackBar, imgBackReview;
    Toolbar toolbar, secondToolBar;
    RequestQueue requestQueue;
    public static ProgressBar addressesFragmentProgress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addresses, container, false);

        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

        rvAddresses = v.findViewById(R.id.rvAddresses);
        tvTitleNext = v.findViewById(R.id.tvTitleNext);
        imgBackReview = v.findViewById(R.id.imgBackReview);
        tvAddAddress = v.findViewById(R.id.tvAddAddress);
        addressesFragmentProgress = v.findViewById(R.id.addressesFragmentProgress);

        tvTitle = getActivity().findViewById(R.id.tvTitleMain);
        search = getActivity().findViewById(R.id.search);
        imgBackBar = getActivity().findViewById(R.id.imgBackBar);
        toolbar = getActivity().findViewById(R.id.mainToolBar);
        secondToolBar = v.findViewById(R.id.reviewToolBar);

        if (OrderSetupFragment.fromOrderSetup || (AddAddressFragment.inAddAddress&& AddAddressFragment.fromOrder)) {
            toolbar.setVisibility(View.GONE);
            MainActivity.setDrawerState(false);
            secondToolBar.setVisibility(View.VISIBLE);
            imgBackReview.setVisibility(View.VISIBLE);
            tvTitleNext.setVisibility(View.VISIBLE);
            tvTitleNext.setText("Addresses");


            imgBackReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderSetupFragment.fromOrderSetup = false;
                    Navigation.findNavController(v).popBackStack(R.id.addressesFragment, true);
                    Navigation.findNavController(v).navigate(R.id.orderSetupFragment);

                }
            });
            AddAddressFragment.inAddAddress=false;
        } else {
            MainActivity.setDrawerState(true);
            secondToolBar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            imgBackBar.setVisibility(View.GONE);
            tvTitle.setText("Addresses");
            search.setVisibility(View.GONE);
        }

        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.addressesFragment,true);
                Navigation.findNavController(v).navigate(R.id.addAddressFragment);
            }
        });

        AddressesList = getAddresses(requestQueue,getActivity());


        return v;
    }

    public static ArrayList<Address> getAddresses(final RequestQueue requestQueue, final Context activity) {
        final ArrayList<Address> addresses = new ArrayList<>();
        final Connection connection = new Connection();
        try {
            final String UserID = MainActivity.user.getId();
            StringRequest request = new StringRequest(Request.Method.GET, connection.getGetAddresses() + UserID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray address = jsonObject.getJSONArray("Address");
                                if(address != null && address.length() > 0 ){
                                for (int i=0;i<address.length();i++){
                                    final JSONObject object = address.getJSONObject(i);
                                    final Address currentAddress = new Address();
                                    currentAddress.setId(object.getString("id"));
                                    currentAddress.setAddressSubject(object.getString("address_subject"));
                                    currentAddress.setStreet(object.getString("street"));
                                    currentAddress.setBuildingNumber(object.getString("buliding_number"));
                                    currentAddress.setFloorNumber(object.getString("floor"));
                                    currentAddress.setApartmentNumber(object.getString("apartment_number"));
                                    currentAddress.setDistrict_id(object.getString("district_id"));
                                    currentAddress.setUser_id(UserID);

                                    StringRequest request1 = new StringRequest(Request.Method.GET, connection.getGetCityFromDistrict() + currentAddress.getDistrict_id()
                                            , new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject object1 = new JSONObject(response);
                                                JSONObject city = object1.getJSONObject("City");
                                                currentAddress.setCity(city.getString("city_name"));
                                                String cityID = city.getString("id");
                                                StringRequest request2 = new StringRequest(Request.Method.GET, connection.getGetDistrict() + cityID,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject object = new JSONObject(response);
                                                                    JSONArray Dist = object.getJSONArray("District");
                                                                    for (int j = 0;j<Dist.length();j++){
                                                                        if (Dist.getJSONObject(j).getString("id").equals(currentAddress.getDistrict_id())){
                                                                            currentAddress.setDistrict(Dist.getJSONObject(j).getString("district_name"));
                                                                            addresses.add(currentAddress);
                                                                            AddressesFragment.rvAddresses.setLayoutManager(new LinearLayoutManager(activity));
                                                                            AddressesAdapter adapter = new AddressesAdapter(addresses, (Activity) activity,requestQueue);
                                                                            AddressesFragment.rvAddresses.setAdapter(adapter);
                                                                            AddressesFragment.addressesFragmentProgress.setVisibility(View.GONE);
                                                                            AddressesFragment.rvAddresses.setVisibility(View.VISIBLE);
                                                                            break;
                                                                        }
                                                                    }
                                                                }catch (JSONException e){

                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                                requestQueue.add(request2);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });

                                    requestQueue.add(request1);


                                }
                                }else{
                                    Toast.makeText(activity, "no Addresses", Toast.LENGTH_SHORT).show();
                                    AddressesFragment.addressesFragmentProgress.setVisibility(View.GONE);
                                    AddressesFragment.rvAddresses.setVisibility(View.VISIBLE);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, "Error Yad", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AddressesList = getAddresses(requestQueue,activity);
                }
            });
            requestQueue.add(request);
        }catch (Exception e){

        }

        return addresses;
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
