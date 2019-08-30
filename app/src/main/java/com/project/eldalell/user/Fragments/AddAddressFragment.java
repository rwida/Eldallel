package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Address;
import com.project.eldalell.user.Classes.City;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Districts;
import com.project.eldalell.user.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddAddressFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddAddressFragment() {
    }

    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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

    public static Address address;
    public static boolean inAddAddress = false,fromOrder=false,fromEdit=false;
    Button btnAddAddress;
    TextView tvAddAddress ;
    EditText etAddressSubject,etAddressStreet,etAddressBuildingNumber
    ,etAddressFloorNumber,etAddressApartmentNumber;
    AutoCompleteTextView etAddressCity,etAddressDistrict;
    ArrayList<City> cities;
    ArrayList<Districts> distric;
    ArrayList<String> districts,citys;
    RequestQueue requestQueue;
    ProgressBar addAddressFragmentProgress;
    LinearLayout addAddressFragmentLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_address, container, false);

        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

        inAddAddress = true;
        btnAddAddress = v.findViewById(R.id.btnAddAddress);
        tvAddAddress = v.findViewById(R.id.tvAddAddress);
        etAddressSubject = v.findViewById(R.id.etAddressSubject);
        etAddressCity = v.findViewById(R.id.etAddressCity);
        etAddressDistrict = v.findViewById(R.id.etAddressDistrict);
        etAddressStreet = v.findViewById(R.id.etAddressStreet);
        etAddressBuildingNumber = v.findViewById(R.id.etAddressBuildingNumber);
        etAddressFloorNumber = v.findViewById(R.id.etAddressFloorNumber);
        etAddressApartmentNumber = v.findViewById(R.id.etAddressApartmentNumber);
        addAddressFragmentProgress = v.findViewById(R.id.addAddressFragmentProgress);
        addAddressFragmentLayout = v.findViewById(R.id.addAddressFragmentLayout);
        citys = new ArrayList<>();
        cities = getCityies(requestQueue,getContext());

        etAddressCity.setKeyListener(null);
        etAddressDistrict.setKeyListener(null);

        if (fromEdit){
            etAddressCity.setText(address.getCity());
            etAddressDistrict.setText(address.getDistrict());
            etAddressApartmentNumber.setText(address.getApartmentNumber());
            etAddressFloorNumber.setText(address.getFloorNumber());
            etAddressBuildingNumber.setText(address.getBuildingNumber());
            etAddressStreet.setText(address.getStreet());
            etAddressSubject.setText(address.getAddressSubject());
            tvAddAddress.setText("Edit Address");
            btnAddAddress.setText("Edit");
            btnAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((etAddressSubject.getText().toString().isEmpty())&&(etAddressCity.getText().toString().isEmpty())
                            &&(etAddressDistrict.getText().toString().isEmpty())&&(etAddressStreet.getText().toString().isEmpty())
                            &&(etAddressBuildingNumber.getText().toString().isEmpty())&&(etAddressApartmentNumber.getText().toString().isEmpty())
                            &&(etAddressFloorNumber.getText().toString().isEmpty())){
                        Toast.makeText(getContext(), "Please Fill All Data", Toast.LENGTH_SHORT).show();
                    }else{
                        editAddress(requestQueue,v);
                    }
                }
            });
            fromEdit = false;
        }else{

        etAddressCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < cities.size(); i++) {
                    if (etAddressCity.getText().toString().equals(cities.get(i).getCity_name())) {
                        String cityId = cities.get(i).getId();
                        distric = getDistricts(requestQueue, cityId);
                    }
                }
                etAddressDistrict.setText("");
            }
        });


        etAddressCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etAddressCity.showDropDown();
                }
            }
        });


        etAddressCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddressCity.showDropDown();
            }
        });
        etAddressDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddressDistrict.showDropDown();
            }
        });
        etAddressDistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etAddressDistrict.showDropDown();
            }
        });


        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etAddressSubject.getText().toString().isEmpty())&&(etAddressCity.getText().toString().isEmpty())
                &&(etAddressDistrict.getText().toString().isEmpty())&&(etAddressStreet.getText().toString().isEmpty())
                &&(etAddressBuildingNumber.getText().toString().isEmpty())&&(etAddressApartmentNumber.getText().toString().isEmpty())
                &&(etAddressFloorNumber.getText().toString().isEmpty())){
                    Toast.makeText(getContext(), "Please Fill All Data", Toast.LENGTH_SHORT).show();
                }else {
                    addAddress(requestQueue,v);
                }
            }
        });

        }
        return v;
    }

    private void editAddress(RequestQueue requestQueue, final View v) {
        final String AddressSubject = etAddressSubject.getText().toString();
        final String UserID = MainActivity.user.getId();
        final String Street = etAddressStreet.getText().toString();
        final String BuildingNumber = etAddressBuildingNumber.getText().toString();
        final String FloorNumber = etAddressFloorNumber.getText().toString();
        final String ApartmentNumber = etAddressApartmentNumber.getText().toString();
        final String AddressID = address.getId();
        Connection connection = new Connection();
        StringRequest request = new StringRequest(Request.Method.POST, connection.getUpdateAddress() + AddressID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Address Edited Successful", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).popBackStack(R.id.addAddressFragment,true);
                        Navigation.findNavController(v).navigate(R.id.addressesFragment);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("address_subject",AddressSubject);
                params.put("street",Street);
                params.put("buliding_number",BuildingNumber);
                params.put("floor",FloorNumber);
                params.put("apartment_number",ApartmentNumber);
                params.put("user_id",UserID);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void addAddress(RequestQueue requestQueue, final View v) {
        final String AddressSubject = etAddressSubject.getText().toString();
        final String UserID = MainActivity.user.getId();
        String District = etAddressDistrict.getText().toString();
        String District_id = "";
        for (Districts district : distric){
            if (district.getDistrict_name().equals(District)){
                District_id = district.getId();
                break;
            }
        }
        final String finalDistrict_id = District_id;
        final String Street = etAddressStreet.getText().toString();
        final String BuildingNumber = etAddressBuildingNumber.getText().toString();
        final String FloorNumber = etAddressFloorNumber.getText().toString();
        final String ApartmentNumber = etAddressApartmentNumber.getText().toString();
        Connection connection = new Connection();

        StringRequest request = new StringRequest(Request.Method.POST, connection.getAddAddress(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Address Added Successful", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(v).popBackStack(R.id.addAddressFragment,true);
                        Navigation.findNavController(v).navigate(R.id.addressesFragment);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("address_subject",AddressSubject);
                params.put("street",Street);
                params.put("buliding_number",BuildingNumber);
                params.put("floor",FloorNumber);
                params.put("apartment_number",ApartmentNumber);
                params.put("district_id", finalDistrict_id);
                params.put("user_id",UserID);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private ArrayList<Districts> getDistricts(RequestQueue requestQueue, String cityID) {
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
                        etAddressDistrict.setAdapter(arrayAdapter1);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Api Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
        return districts1;
    }

    private ArrayList<City> getCityies(RequestQueue requestQueue, final Context context) {
        final ArrayList<City> cities = new ArrayList<>();
        Connection connection = new Connection();
        StringRequest request = new StringRequest(Request.Method.GET, connection.getViewCities(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject city = array.getJSONObject(i);
                        City currentCity = new City();
                        currentCity.setId(city.getString("id"));
                        currentCity.setCity_name(city.getString("city_name"));
                        citys.add(currentCity.getCity_name());
                        cities.add(currentCity);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,citys);
                    etAddressCity.setAdapter(adapter);
                    addAddressFragmentProgress.setVisibility(View.GONE);
                    addAddressFragmentLayout.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
