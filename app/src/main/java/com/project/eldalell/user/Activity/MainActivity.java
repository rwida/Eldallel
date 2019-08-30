package com.project.eldalell.user.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Districts;
import com.project.eldalell.user.Classes.Shop;
import com.project.eldalell.user.Classes.User;
import com.project.eldalell.user.Fragments.AddAddressFragment;
import com.project.eldalell.user.Fragments.CategoriesFragment;
import com.project.eldalell.user.Fragments.OrderSetupFragment;
import com.project.eldalell.user.Fragments.ReviewOrderFragment;
import com.project.eldalell.user.Fragments.ShopsFragment;
import com.project.eldalell.user.Fragments.UpcomingFragment;
import com.project.eldalell.user.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public NavController navController;
    private static ActionBarDrawerToggle drawerToggle;
    @SuppressLint("StaticFieldLeak")
    private static DrawerLayout drawerLayout;
    public static String shopname;
    public static Shop shop;
    public static Districts district;
    public static final String MY_PREFS_NAME = "tokenPref";
    RequestQueue requestQueue;
    public static User user;
    @SuppressLint("StaticFieldLeak")
    public static NavigationView nvView;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvHeaderUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar mainToolBar = findViewById(R.id.mainToolBar);
        nvView = findViewById(R.id.nvView);
        View Hview = nvView.inflateHeaderView(R.layout.nav_header);
        tvHeaderUserName = Hview.findViewById(R.id.tvHeaderUserName);

        setSupportActionBar(mainToolBar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolBar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        nvView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navHome:
                        navController.popBackStack(R.id.addressFragment, true);
                        navController.navigate(R.id.addressFragment);
                        break;

                    case R.id.navAccountInfo:
                        navController.popBackStack(R.id.accountInfoFragment, true);
                        navController.navigate(R.id.accountInfoFragment);
                        break;

                    case R.id.navOrderHestory:
                        navController.popBackStack(R.id.orderHistoryFragment, true);
                        navController.navigate(R.id.orderHistoryFragment);
                        break;

                    case R.id.navSavedAddress:
                        navController.popBackStack(R.id.addressesFragment, true);
                        navController.navigate(R.id.addressesFragment);
                        break;

                    case R.id.navSignUpVendor:
                        navController.popBackStack(R.id.vendorFragment, true);
                        navController.navigate(R.id.vendorFragment);
                        break;

                    case R.id.navLogOut:
                        Menu nav_Menu = MainActivity.nvView.getMenu();
                        nav_Menu.findItem(R.id.seeYou).setVisible(false);
                        nav_Menu.findItem(R.id.navAccountInfo).setVisible(false);
                        nav_Menu.findItem(R.id.navSavedAddress).setVisible(false);
                        nav_Menu.findItem(R.id.navOrderHestory).setVisible(false);
                        tvHeaderUserName.setVisibility(View.GONE);
                        user = null;
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("token", null);
                        editor.apply();
                        navController.popBackStack();
                        navController.navigate(R.id.addressFragment);
                        break;

                }
                return true;
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token != null) {
            user = getUserAPI(token, MainActivity.this, requestQueue, nvView, tvHeaderUserName);

        }

    }

    public static User getUserAPI(final String token, final Context context,
                                  final RequestQueue requestQueue, final NavigationView nvView, final TextView tvHeaderUserName) {
        final User currentUser = new User();
        final Connection connection = new Connection();
        final Menu nav_Menu = nvView.getMenu();
        StringRequest request = new StringRequest(Request.Method.GET, connection.getGetAuthUser(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject success = object.getJSONObject("success");
                    currentUser.setId(success.getString("id"));
                    currentUser.setFirst_name(success.getString("first_name"));
                    currentUser.setLast_name(success.getString("last_name"));
                    currentUser.setEmail(success.getString("email"));
                    currentUser.setPhone_number(success.getString("phone_number"));
                    currentUser.setBirthday(success.getString("birthday"));
                    currentUser.setGender(success.getString("gender"));
                    currentUser.setBlock(success.getString("block"));
                    nav_Menu.findItem(R.id.seeYou).setVisible(true);
                    nav_Menu.findItem(R.id.navAccountInfo).setVisible(true);
                    nav_Menu.findItem(R.id.navSavedAddress).setVisible(true);
                    nav_Menu.findItem(R.id.navOrderHestory).setVisible(true);
                    tvHeaderUserName.setVisibility(View.VISIBLE);
                    tvHeaderUserName.setText(user.getFirst_name() + " " + user.getLast_name());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                MainActivity.user = getUserAPI(token, context, requestQueue, nvView, tvHeaderUserName);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");

                return headers;
            }
        };
        requestQueue.add(request);
        return currentUser;
    }

    public static void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.syncState();

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.syncState();
        }
    }

    @Override
    public void onBackPressed() {
        if (CategoriesFragment.inCategories) {
            exitByBackKey();
        } else if (OrderSetupFragment.orderSubmitted) {
            navController.popBackStack(R.id.traceOrderFragment, true);
            navController.navigate(R.id.upcomingFragment);
            OrderSetupFragment.orderSubmitted = false;
            UpcomingFragment.fromUpcoming = true;
        } else if (ShopsFragment.inShops) {
            navController.popBackStack(R.id.shopsFragment, true);
            navController.navigate(R.id.addressFragment);
            ShopsFragment.inShops = false;
        } else if (ReviewOrderFragment.fromReview){
            CategoriesFragment.products = CategoriesFragment.getProducts(requestQueue,MainActivity.this);
            CategoriesFragment.ProdectTypes = CategoriesFragment.getProdectTypes(requestQueue,MainActivity.this);
            navController.popBackStack(R.id.reviewOrderFragment, true);
            navController.navigate(R.id.categoriesFragment);
        }
        else {
            super.onBackPressed();
        }
        if (UpcomingFragment.fromUpcoming) {
            navController.popBackStack();
            navController.navigate(R.id.addressFragment);
            UpcomingFragment.fromUpcoming = false;
        }
        if (AddAddressFragment.inAddAddress){
            navController.popBackStack(R.id.addAddressFragment,true);
            navController.navigate(R.id.addressesFragment);
            AddAddressFragment.fromEdit = false;

        }


    }

    protected void exitByBackKey() {

        AlertDialog alertBox = new AlertDialog.Builder(MainActivity.this)
                .setMessage("Do you want to Go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        CategoriesFragment.inCategories = false;
                        navController.popBackStack(R.id.categoriesFragment, true);
                        navController.navigate(R.id.shopsFragment);
                        shopname = null;
                        ShopsFragment.shops = ShopsFragment.getShops(requestQueue,MainActivity.this);
                        ShopsFragment.shopTypes = ShopsFragment.getShopTypes(requestQueue,MainActivity.this);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .show();
    }

    public static void changeInfo() {
        tvHeaderUserName.setVisibility(View.VISIBLE);
        tvHeaderUserName.setText(user.getFirst_name() + " " + user.getLast_name());
    }

}
