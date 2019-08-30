package com.project.eldalell.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Address;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.R;

import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderSetupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderSetupFragment() {
    }


    public static OrderSetupFragment newInstance(String param1, String param2) {
        OrderSetupFragment fragment = new OrderSetupFragment();
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

    EditText etNote;
    TextView tvTitleNext, tvChooseAddress, tvAddAddress, tvCountryDetails,tvHome ,tvGovernmentDetails, tvDistrictDetails, tvStreetDetails, tvBuildingDetails, tvFloorDetails, tvApartmentDetails;
    ImageView imgBackReview;
    Button btnSubmit;
    public static Address currentAddress;
    public static boolean fromOrderSetup, orderSubmitted = false;
    Toolbar mainbar;
    RequestQueue requestQueue;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_setup, container, false);
        etNote = v.findViewById(R.id.etNote);
        tvTitleNext = v.findViewById(R.id.tvTitleNext);
        tvAddAddress = v.findViewById(R.id.tvAddAddress);
        tvHome = v.findViewById(R.id.tvHome);
        tvChooseAddress = v.findViewById(R.id.tvChooseAddress);
        imgBackReview = v.findViewById(R.id.imgBackReview);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        tvCountryDetails = v.findViewById(R.id.tvCountryDetails);
        tvGovernmentDetails = v.findViewById(R.id.tvGovernmentDetails);
        tvDistrictDetails = v.findViewById(R.id.tvDistrictDetails);
        tvStreetDetails = v.findViewById(R.id.tvStreetDetails);
        tvBuildingDetails = v.findViewById(R.id.tvBuildingDetails);
        tvFloorDetails = v.findViewById(R.id.tvFloorDetails);
        tvApartmentDetails = v.findViewById(R.id.tvApartmentDetails);
        mainbar = getActivity().findViewById(R.id.mainToolBar);
        mainbar.setVisibility(View.GONE);
        AddAddressFragment.fromOrder=true;
        if (currentAddress != null) {
            tvGovernmentDetails.setText(currentAddress.getCity());
            tvDistrictDetails.setText(currentAddress.getDistrict());
            tvStreetDetails.setText(currentAddress.getStreet());
            tvHome.setText(currentAddress.getAddressSubject());
            tvBuildingDetails.setText(currentAddress.getBuildingNumber());
            tvFloorDetails.setText(currentAddress.getFloorNumber());
            tvApartmentDetails.setText(currentAddress.getApartmentNumber());
        } else {
            tvCountryDetails.setText("");
            tvGovernmentDetails.setText("");
            tvDistrictDetails.setText("");
            tvStreetDetails.setText("");
            tvHome.setText("");
            tvBuildingDetails.setText("");
            tvFloorDetails.setText("");
            tvApartmentDetails.setText("");

        }
        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        imgBackReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.orderSetupFragment, true);
                Navigation.findNavController(v).navigate(R.id.reviewOrderFragment);

            }
        });

        tvTitleNext.setText("Order Setup");
        etNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etNote.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        tvChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addressesFragment);
                fromOrderSetup = true;
            }
        });

        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addAddressFragment);
                fromOrderSetup = true;
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAddress!=null)
                {
                submitOrder(v);

            }else {
                    Toast.makeText(getContext(), "Please Fill All Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }

    float calcTotalPrice() {
        float total = 0;
        for (Order order : CategoriesFragment.orders) {
            total = total + order.calcTotalOrderPrice();
        }
        return total;
    }


    private void submitOrder(final View v) {
        final ArrayList<Order> orders =CategoriesFragment.orders;
        final Connection connection = new Connection();
        final String UserID = MainActivity.user.getId();
        final String ShopID = MainActivity.shop.getId();
        final String AddressID = currentAddress.getId();
        final float SubTotal = calcTotalPrice();
        float DeliveryCoast = MainActivity.shop.getDelivery_cost();
        final float Total = SubTotal + DeliveryCoast;
        String note_for_delivery = etNote.getText().toString();
        if (note_for_delivery.isEmpty()){
            note_for_delivery="No Note";
        }
        final String finalNote_for_delivery = note_for_delivery;
        StringRequest request = new StringRequest(Request.Method.POST, connection.getAddOrder(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final String orderID = jsonObject.getString("id");
                            int i = 0;
                            for (final Order order :orders){
                                CategoriesFragment.orders.get(i).setOrderID(orderID);
                                i++;
                                StringRequest request1 = new StringRequest(Request.Method.POST, connection.getAddInvoice(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> param = new HashMap<>();

                                    param.put("quantity",String.valueOf(order.getOrderQuantity()));
                                    param.put("item_shop_id",order.getItem_shop_id());
                                    param.put("order_id",orderID);

                                    return  param;
                                }
                            };
                            requestQueue.add(request1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        orderSubmitted = true;
                        TraceOrderFragment.orders = CategoriesFragment.orders;
                        Navigation.findNavController(v).popBackStack(R.id.traceOrderFragment, true);
                        Navigation.findNavController(v).popBackStack(R.id.reviewOrderFragment, true);
                        Navigation.findNavController(v).popBackStack(R.id.categoriesFragment, true);
                        Navigation.findNavController(v).popBackStack(R.id.shopsFragment, true);
                        Navigation.findNavController(v).navigate(R.id.traceOrderFragment);
                        AddAddressFragment.fromOrder=false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> parameter = new HashMap<>();

                parameter.put("sub_total",String.valueOf(SubTotal));
                parameter.put("total",String.valueOf(Total));
                parameter.put("user_id",UserID);
                parameter.put("shop_id",ShopID);
                parameter.put("address_id",AddressID);
                parameter.put("note_for_delivery", finalNote_for_delivery);


                return parameter;
            }



        };

        requestQueue.add(request);

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
