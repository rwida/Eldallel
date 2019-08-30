package com.project.eldalell.user.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Adapter.ProdectTypeAdapter;
import com.project.eldalell.user.Adapter.ProductAdapter;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Classes.ProdectType;
import com.project.eldalell.user.Classes.Product;
import com.project.eldalell.user.R;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CategoriesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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

    public static RecyclerView rvCategories;
    public static ArrayList<ProdectType> ProdectTypes;
    public static ArrayList<Product> products;
    TextView tvTitle;
    public static TextView tvNomTotal, tvPriceTotal, tvSelectedAddress;
    Toolbar mainToolBar;
    ImageView imgBack, imgShopIcon;
    public static GridView gvCategories;
    public static ArrayList<Order> orders;
    public static RelativeLayout relativeEndOrder;
    public NavController navController;
    public static FloatingActionButton floatingActionButton;
    public static LinearLayout.LayoutParams marginFilter;
    public static boolean inCategories = false;
    RequestQueue requestQueue;
    public static boolean typeChange = false;
    public static LinearLayout categoriesFragmentLayout;
    public static ProgressBar categoriesFragmentProgress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        inCategories = true;

        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        if (ReviewOrderFragment.fromReview){

        }else{
            orders = new ArrayList<>();
        }

        categoriesFragmentLayout = v.findViewById(R.id.categoriesFragmentLayout);
        categoriesFragmentProgress = v.findViewById(R.id.categoriesFragmentProgress);

        tvPriceTotal = v.findViewById(R.id.tvPriceTotal);
        tvNomTotal = v.findViewById(R.id.tvNomTotal);
        imgShopIcon = v.findViewById(R.id.imgShopIcon);

        tvSelectedAddress = getActivity().findViewById(R.id.tvSelectedAddress);

        tvSelectedAddress.setVisibility(View.GONE);

        mainToolBar = getActivity().findViewById(R.id.mainToolBar);
        mainToolBar.setVisibility(View.VISIBLE);
        tvTitle = getActivity().findViewById(R.id.tvTitleMain);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(MainActivity.shopname);
        imgBack = getActivity().findViewById(R.id.imgBackBar);
        relativeEndOrder = v.findViewById(R.id.relativeEndOrder);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        relativeEndOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.reviewOrderFragment);
                inCategories = false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitByBackKey();

            }
        });

        rvCategories = v.findViewById(R.id.rvCategories);


        gvCategories = v.findViewById(R.id.gvCategories);

        return v;
    }

    public static ArrayList<Product> getProducts(final RequestQueue requestQueue, final Context context)
    {
        final ArrayList<Product> products = new ArrayList<>();
        final Connection connection = new Connection();
        final String ShopId = MainActivity.shop.getId();

        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetShopItem() + ShopId
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray items = jsonObject.getJSONArray("ItemsShop");
                    for (int i=0;i<items.length();i++){
                        JSONObject item = items.getJSONObject(i);
                        final Product currentProduct = new Product();
                        currentProduct.setId(item.getString("id"));
                        currentProduct.setProductExist(item.getInt("item_quantity"));
                        currentProduct.setProductPrice(Float.parseFloat(item.getString("new_retail_price")));
                        currentProduct.setItem_id(item.getString("item_id"));
                        currentProduct.setShop_id(ShopId);

                        getItemsInfo(requestQueue,context,currentProduct,products);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CategoriesFragment.products = getProducts(requestQueue,context);
            }
        });

        requestQueue.add(request);


        return products;
    }
    public static Product getItemsInfo(final RequestQueue requestQueue, final Context context, final Product currentProduct
            , final ArrayList<Product> products){

        final Connection connection = new Connection();
        StringRequest request1 = new StringRequest(Request.Method.GET, connection.getGetItems() + currentProduct.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject item = object.getJSONObject("Item");

                            currentProduct.setProductName(item.getString("item_name"));
                            currentProduct.setImgProduct(connection.getAdminHostIP() + "/public" + item.getString("image"));
                            currentProduct.setItem_category_id(item.getString("item_category_id"));
                            products.add(currentProduct);
                            if (ReviewOrderFragment.fromReview){
                                ProductAdapter adapter1 = new ProductAdapter(context, products, orders);
                                CategoriesFragment.gvCategories.setAdapter(adapter1);
                            }else {
                                ProductAdapter adapter1 = new ProductAdapter(context, products, orders);
                                CategoriesFragment.gvCategories.setAdapter(adapter1);
                            }
                            CategoriesFragment.categoriesFragmentProgress.setVisibility(View.GONE);
                            CategoriesFragment.categoriesFragmentLayout.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    getItemsInfo(requestQueue,context,currentProduct,products);
            }
        });
        requestQueue.add(request1);
        return currentProduct;
    }
    public static ArrayList<ProdectType> getProdectTypes(final RequestQueue requestQueue, final Context context){
        final ArrayList<ProdectType> prodectsTypes = new ArrayList<>();
        final Connection connection = new Connection();
        String shopID = MainActivity.shop.getId();

        prodectsTypes.add(new ProdectType("0","All",""));
        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetProductType(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray prodectTypes = new JSONArray(response);
                            for (int i=0; i<prodectTypes.length();i++)
                            {
                                JSONObject type = prodectTypes.getJSONObject(i);
                                ProdectType prodectType = new ProdectType();
                                prodectType.setId(type.getString("id"));
                                prodectType.setImage(connection.getAdminHostIP() + "/public" + type.getString("image"));
                                prodectType.setItem_category_name(type.getString("item_category_name"));

                                prodectsTypes.add(prodectType);
                            }
                        }catch (JSONException e){

                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                        CategoriesFragment.rvCategories.setLayoutManager(layoutManager);
                        ProdectTypeAdapter adapter = new ProdectTypeAdapter(ProdectTypes, (Activity) context,requestQueue);
                        CategoriesFragment.rvCategories.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CategoriesFragment.ProdectTypes = getProdectTypes(requestQueue,context);
            }
        });
        requestQueue.add(request);
        return prodectsTypes;
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


    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(getContext())
                .setMessage("Do you want to Go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        navController.popBackStack(R.id.categoriesFragment, true);
                        navController.navigate(R.id.shopsFragment);
                        MainActivity.shopname = null;
                        mainToolBar.setVisibility(View.VISIBLE);
                        inCategories = false;
                        ShopsFragment.shops = ShopsFragment.getShops(requestQueue,getContext());
                        ShopsFragment.shopTypes = ShopsFragment.getShopTypes(requestQueue,getContext());


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}
