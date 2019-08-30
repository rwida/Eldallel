package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Adapter.OrderReviewAdapter;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.R;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.content.Context.MODE_PRIVATE;


public class ReviewOrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewOrderFragment() {
    }

    public static ReviewOrderFragment newInstance(String param1, String param2) {
        ReviewOrderFragment fragment = new ReviewOrderFragment();
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

    RecyclerView rvOrder;
    public static ArrayList<Order> orders;
    ImageView imgBackReview;
    public NavController navController;
    Button btnProceedOrder;
    public static TextView tvTotalReviewPrice, tvDeliveryCoast, tvSubTotal;
    TextView tvTitleNext;
    public static boolean fromReview = false;
    Toolbar mainbar;
    public static final String MY_PREFS_NAME = "tokenPref";
    RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review_order, container, false);
        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        fromReview = true;

        rvOrder = v.findViewById(R.id.rvOrder);
        imgBackReview = v.findViewById(R.id.imgBackReview);
        btnProceedOrder = v.findViewById(R.id.btnProceedOrder);
        tvTotalReviewPrice = v.findViewById(R.id.tvTotalReviewPrice);
        tvDeliveryCoast = v.findViewById(R.id.tvDeliveryCoast);
        tvSubTotal = v.findViewById(R.id.tvSubTotal);
        tvTitleNext = v.findViewById(R.id.tvTitleNext);
        mainbar = getActivity().findViewById(R.id.mainToolBar);

        AddAddressFragment.fromOrder=false;

        mainbar.setVisibility(View.GONE);
        tvTitleNext.setText("Your Order");

        tvDeliveryCoast.setText(MainActivity.shop.getDelivery_cost()+" EGP");
        tvTotalReviewPrice.setText(String.format("%.2f", (calcTotalPrice() + 10f)) + " EGP");
        tvSubTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        imgBackReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesFragment.products = CategoriesFragment.getProducts(requestQueue,getContext());
                CategoriesFragment.ProdectTypes = CategoriesFragment.getProdectTypes(requestQueue,getContext());
                navController.popBackStack(R.id.reviewOrderFragment, true);
                Navigation.findNavController(v).navigate(R.id.categoriesFragment);

            }
        });

        btnProceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Check if Login First
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String token = prefs.getString("token", null);
                if (token != null) {
                    Navigation.findNavController(v).navigate(R.id.orderSetupFragment);
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_reviewOrderFragment_to_loginFragment);
                }
            }
        });

        orders = CategoriesFragment.orders;

        rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderReviewAdapter adapter = new OrderReviewAdapter(getActivity(), orders, true);
        rvOrder.setAdapter(adapter);
        return v;
    }

    float calcTotalPrice() {
        float total = 0;
        for (Order order : CategoriesFragment.orders) {
            total = total + order.calcTotalOrderPrice();
        }
        return total;
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
