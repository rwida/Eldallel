package com.project.eldalell.user.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.project.eldalell.user.Adapter.ShopAdapter;
import com.project.eldalell.user.Adapter.ShopTypeAdapter;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Shop;
import com.project.eldalell.user.Classes.ShopType;
import com.project.eldalell.user.CustomComponent.SearchDialog;
import com.project.eldalell.user.R;


import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ShopsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShopsFragment() {

    }


    public static ShopsFragment newInstance(String param1, String param2) {
        ShopsFragment fragment = new ShopsFragment();
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

    public static RecyclerView rvShopsType;
    public static RecyclerView rvShops;
    public static ArrayList<ShopType> shopTypes;
    public static ArrayList<Shop> shops;
    Toolbar mainToolBar;
    TextView tvSelectedAddress, tvTitle;
    ImageView search, imgBackBar;
    RequestQueue requestQueue;
    public NavController navController;
    public static Boolean inShops = false;
    public static ProgressBar shopsFragmentProgress;
    public static LinearLayout shopsFragmentLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shops, container, false);


        MainActivity.setDrawerState(false);
        ReviewOrderFragment.fromReview = false;

        inShops = true;
        shopsFragmentProgress = v.findViewById(R.id.shopsFragmentProgress);
        shopsFragmentLayout = v.findViewById(R.id.shopsFragmentLayout);
        tvSelectedAddress = getActivity().findViewById(R.id.tvSelectedAddress);
        search = getActivity().findViewById(R.id.search);
        tvTitle = getActivity().findViewById(R.id.tvTitleMain);
        imgBackBar = getActivity().findViewById(R.id.imgBackBar);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        imgBackBar.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        tvSelectedAddress.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        rvShopsType = v.findViewById(R.id.rvShopsType);
        rvShops = v.findViewById(R.id.rvShops);

        mainToolBar = getActivity().findViewById(R.id.mainToolBar);
        mainToolBar.setVisibility(View.VISIBLE);


        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog();
            }
        });

        imgBackBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack(R.id.shopsFragment, true);
                navController.navigate(R.id.addressFragment);
                imgBackBar.setVisibility(View.GONE);
                MainActivity.setDrawerState(true);
                inShops = false;
            }
        });

        tvSelectedAddress.setText(MainActivity.district.getDistrict_name());





        return v;
    }

    public static ArrayList<Shop> getShops (final RequestQueue requestQueue , final Context context){
        final ArrayList<Shop> shops = new ArrayList<>();
        final Connection connection = new Connection();
        String districtID = MainActivity.district.getId();

        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetShopsFromDistrict() + districtID
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray shop = jsonObject.getJSONArray("Shop");
                    for (int i=0; i<shop.length();i++)
                    {
                        Shop currentShop = new Shop();
                        JSONObject object = shop.getJSONObject(i);
                        currentShop.setId(object.getString("id"));
                        currentShop.setShopName(object.getString("shop_name"));
                        currentShop.setShopDetails(object.getString("description"));
                        currentShop.setShopLogo(connection.getAdminHostIP() + "/public" + object.getString("logo"));
                        currentShop.setDelivery_cost(Float.parseFloat(object.getString("delivery_cost")));
                        currentShop.setDelivery_time(Integer.parseInt(object.getString("delivery_time")));
                        currentShop.setShop_type_id(object.getString("shop_type_id"));
                        currentShop.setDistrict_id(object.getString("district_id"));
                        currentShop.setVendor_id(object.getString("vendor_id"));
                        if (object.getString("working_statue").equals("1")){
                            currentShop.setStatework(true);
                        }else if (object.getString("working_statue").equals("0")){
                            currentShop.setStatework(false);
                        }


                        shops.add(currentShop);
                    }
                }catch (JSONException e){

                }
                rvShops.setLayoutManager(new LinearLayoutManager(context));
                ShopAdapter Shopadapter = new ShopAdapter(shops, (Activity) context,requestQueue);
                rvShops.setAdapter(Shopadapter);

                shopsFragmentProgress.setVisibility(View.GONE);
                shopsFragmentLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShopsFragment.shops = getShops(requestQueue,context);
            }
        });

        requestQueue.add(request);
        return shops;
    }

    public static ArrayList<ShopType> getShopTypes(final RequestQueue requestQueue, final Context context) {
        final ArrayList<ShopType> shopTypes = new ArrayList<>();
        final Connection connection = new Connection();
        shopTypes.add(new ShopType("All",
                connection.getAdminHostIP()+"/images/b.xml"
                , "0"));

        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetShopType()
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray shopType = new JSONArray(response);
                    for (int i = 0; i < shopType.length(); i++) {
                        ShopType currentShopType = new ShopType();
                        JSONObject type = shopType.getJSONObject(i);
                        currentShopType.setImage(connection.getAdminHostIP() + "/public" + type.getString("image"));
                        currentShopType.setId(type.getString("id"));
                        currentShopType.setShop_type_name(type.getString("shop_type_name"));
                        shopTypes.add(currentShopType);
                    }


                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                rvShopsType.setLayoutManager(layoutManager);
                ShopTypeAdapter adapter = new ShopTypeAdapter(shopTypes, (Activity) context,requestQueue);
                rvShopsType.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                ShopsFragment.shopTypes = getShopTypes(requestQueue,context);
            }
        });
        requestQueue.add(request);
        return shopTypes;
    }

    public void searchDialog() {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getActivity().getSupportFragmentManager(), "search Dialog");
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