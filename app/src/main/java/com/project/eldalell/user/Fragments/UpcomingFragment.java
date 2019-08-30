package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Adapter.HistoryAdapter;
import com.project.eldalell.user.Adapter.UpcomingAdapter;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.History;
import com.project.eldalell.user.Classes.Upcoming;
import com.project.eldalell.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UpcomingFragment extends Fragment {
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";


  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public UpcomingFragment() {
  }

  public static UpcomingFragment newInstance(String param1, String param2) {
    UpcomingFragment fragment = new UpcomingFragment();
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

  TextView tvTitle, tvHistory;
  RecyclerView rvUpcoming;
  ArrayList<Upcoming> upcomingList;
  Toolbar mainbar;
  ImageView imgBackBar;
  public static boolean fromUpcoming = false, inUpComing = false;
  RequestQueue requestQueue;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
    requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    tvTitle = getActivity().findViewById(R.id.tvTitleMain);
    rvUpcoming = v.findViewById(R.id.rvOrdersUpcoming);
    tvHistory = v.findViewById(R.id.tvHistory);
    mainbar = getActivity().findViewById(R.id.mainToolBar);
    mainbar.setVisibility(View.VISIBLE);
    MainActivity.setDrawerState(true);
    imgBackBar = getActivity().findViewById(R.id.imgBackBar);
    imgBackBar.setVisibility(View.GONE);

    tvHistory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(v).popBackStack(R.id.upcomingFragment, true);
        Navigation.findNavController(v).navigate(R.id.orderHistoryFragment);
      }
    });

    upcomingList = getUpCommingOrders(requestQueue, getContext());



    tvTitle.setText("Orders");

    return v;
  }

  private ArrayList<Upcoming> getUpCommingOrders(final RequestQueue requestQueue, final Context context) {
    final ArrayList<Upcoming> upcomings = new ArrayList<>();
    String UserID = MainActivity.user.getId();

    final Connection connection = new Connection();

    StringRequest request = new StringRequest(Request.Method.GET, connection.getUpcomingOrders() + UserID,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  JSONObject object = new JSONObject(response);
                  JSONArray Orders = object.getJSONArray("Orders");
                  for (int i = 0; i < Orders.length(); i++) {
                    JSONObject order = Orders.getJSONObject(i);
                    Upcoming upcoming = new Upcoming();
                    upcoming.setImage(connection.getAdminHostIP() + "/public" + order.getString("logo"));
                    upcoming.setOrderID(order.getString("id"));
                    upcoming.setOrderDate(order.getString("updated_at"));
                    upcoming.setShopName(order.getString("shop_name"));
                    upcoming.setOrderStatus(true);
                    upcoming.setShopID(order.getString("shop_id"));
                    upcoming.setNote(order.getString("note_for_delivery"));
                    if (order.getString("delivery_man_accepted").equalsIgnoreCase("0")) {
                      upcoming.setOrderStatus(false);
                    } else if (order.getString("delivery_man_accepted").equalsIgnoreCase("1")) {
                      upcoming.setOrderStatus(true);
                    }

                    upcomings.add(upcoming);
                  }
                  rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
                  UpcomingAdapter adapter = new UpcomingAdapter(upcomings, getActivity(), requestQueue);
                  rvUpcoming.setAdapter(adapter);
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
    return upcomings;
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
