package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.City;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Districts;
import com.project.eldalell.user.R;

import java.util.ArrayList;

import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.project.eldalell.user.Fragments.ShopsFragment.getShopTypes;


public class AddressFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddressFragment() {
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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

    AutoCompleteTextView etSelectCity, etSelectDistrict;
    Button btnSearch;
    public static ArrayList<String> citys, districts;
    ImageView imgBackBar, search;
    TextView tvTitle, tvSelectedAddress;
    Toolbar mainTool;
    public static ArrayList<City> cities;
    ArrayList<Districts> distric;
    RequestQueue requestQueue;
    ProgressBar addressFragmentProgress;
    LinearLayout addressFragmentLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address, container, false);

        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

        addressFragmentLayout = v.findViewById(R.id.addressFragmentLayout);
        MainActivity.setDrawerState(true);
        addressFragmentProgress = v.findViewById(R.id.addressFragmentProgress);
        etSelectCity = v.findViewById(R.id.etSelectCity);
        etSelectDistrict = v.findViewById(R.id.etSelectDistrict);
        btnSearch = v.findViewById(R.id.btnSearch);
        mainTool = getActivity().findViewById(R.id.mainToolBar);
        mainTool.setVisibility(View.VISIBLE);
        imgBackBar = getActivity().findViewById(R.id.imgBackBar);
        tvTitle = getActivity().findViewById(R.id.tvTitleMain);
        tvSelectedAddress = getActivity().findViewById(R.id.tvSelectedAddress);
        search = getActivity().findViewById(R.id.search);

        search.setVisibility(View.GONE);
        imgBackBar.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvSelectedAddress.setVisibility(View.GONE);

        tvTitle.setText(R.string.chooseAddress);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(etSelectDistrict.getText().toString().isEmpty()) &&
                        districts.contains(etSelectDistrict.getText().toString())) {
                    for (int i =0; i<distric.size();i++)
                    {
                        if (distric.get(i).getDistrict_name().equals(etSelectDistrict.getText().toString()))
                        {
                            MainActivity.district = distric.get(i);
                        }
                    }

                    Navigation.findNavController(v).navigate(R.id.shopsFragment);
                    ShopsFragment.shopTypes = getShopTypes(requestQueue, getContext());
                    ShopsFragment.shops = ShopsFragment.getShops(requestQueue,getContext());
                } else {
                    Toast.makeText(getActivity(), "Please Select a correct Address", Toast.LENGTH_SHORT).show();
                }
            }
        });


        citys = new ArrayList<>();
        districts = new ArrayList<>();

        etSelectCity.setKeyListener(null);
        etSelectDistrict.setKeyListener(null);
        cities = getCites(requestQueue);
        etSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < cities.size(); i++) {
                    if (etSelectCity.getText().toString().equals(cities.get(i).getCity_name())) {
                        String cityId = cities.get(i).getId();
                        distric = getDistricts(requestQueue, cityId);
                    }
                }
                etSelectDistrict.setText("");
            }
        });


        etSelectCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSelectCity.showDropDown();
                }
            }
        });
        etSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSelectCity.showDropDown();
            }
        });
        etSelectDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSelectDistrict.showDropDown();
            }
        });
        etSelectDistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSelectDistrict.showDropDown();
            }
        });

        return v;
    }

    private ArrayList<Districts> getDistricts(final RequestQueue requestQueue, final String cityID) {
        final ArrayList<Districts> districts1 = new ArrayList<>();
        Connection connection = new Connection();
        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetDistrict() + cityID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray Dist = object.getJSONArray("District");
                            for (int i = 0; i < Dist.length(); i++) {
                                JSONObject district = Dist.getJSONObject(i);
                                Districts currentDistrict = new Districts();
                                currentDistrict.setCity_id(district.getString("city_id"));
                                currentDistrict.setDistrict_name(district.getString("district_name"));
                                currentDistrict.setId(district.getString("id"));
                                districts1.add(currentDistrict);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        districts = new ArrayList<>();
                        for (int j = 0; j < distric.size(); j++) {
                            districts.add(distric.get(j).getDistrict_name());
                        }
                        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, districts);
                        etSelectDistrict.setAdapter(arrayAdapter1);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getDistricts(requestQueue, cityID);
                Toast.makeText(getContext(), "Api Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
        return districts1;
    }

    private ArrayList<City> getCites(final RequestQueue requestQueue) {
        final ArrayList<City> cities = new ArrayList<>();
        Connection connection = new Connection();
        StringRequest request = new StringRequest(Request.Method.GET, connection.getViewCities(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray Cities = new JSONArray(response);
                    for (int i = 0; i < Cities.length(); i++) {
                        JSONObject city = Cities.getJSONObject(i);
                        City currentCity = new City();
                        currentCity.setCity_name(city.getString("city_name"));
                        currentCity.setId(city.getString("id"));
                        cities.add(currentCity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < cities.size(); i++) {
                    citys.add(cities.get(i).getCity_name());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, citys);
                etSelectCity.setAdapter(arrayAdapter);
                addressFragmentProgress.setVisibility(View.GONE);
                addressFragmentLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                AddressFragment.cities = getCites(requestQueue);
            }
        });
        requestQueue.add(request);
        return cities;
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
