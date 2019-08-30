package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.History;
import com.project.eldalell.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrderHistoryFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public OrderHistoryFragment() {
  }

  public static OrderHistoryFragment newInstance(String param1, String param2) {
    OrderHistoryFragment fragment = new OrderHistoryFragment();
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

  RecyclerView rvOrders;
  ArrayList<History> ordersList;
  TextView tvTitle, tvUpcoming;
  RequestQueue requestQueue;
    public static boolean fromHistory = false;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_order_history, container, false);
    rvOrders = v.findViewById(R.id.rvOrders);
    tvTitle = getActivity().findViewById(R.id.tvTitleMain);
    tvUpcoming = v.findViewById(R.id.tvUpcoming);
    requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    tvUpcoming.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(v).popBackStack(R.id.orderHistoryFragment, true);
        Navigation.findNavController(v).navigate(R.id.upcomingFragment);
      }
    });


      tvTitle.setText("Orders");
    ordersList = getHistoryOrder(requestQueue, getContext());


    return v;
  }

  private ArrayList<History> getHistoryOrder(RequestQueue requestQueue, final Context context) {
    final ArrayList<History> histories = new ArrayList<>();
    String UserID = MainActivity.user.getId();

    final Connection connection = new Connection();

    StringRequest request = new StringRequest(Request.Method.GET, connection.getHistoryOrders() + UserID,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  JSONObject object = new JSONObject(response);
                  JSONArray Orders = object.getJSONArray("orders");
                  for (int i = 0; i < Orders.length(); i++) {
                    JSONObject order = Orders.getJSONObject(i);
                    History history = new History();
                      history.setImage(connection.getAdminHostIP() + "/public" + order.getString("logo"));
                    history.setOrderID(order.getString("id"));
                    history.setOrderDate(order.getString("updated_at"));
                    history.setShopName(order.getString("shop_name"));
                    history.setOrderStatus(true);
                    history.setShopID(order.getString("shop_id"));
                    history.setNote(order.getString("note_for_delivery"));
                    histories.add(history);
                  }
                  rvOrders.setLayoutManager(new LinearLayoutManager(context));
                  HistoryAdapter adapter = new HistoryAdapter(ordersList, getActivity());
                  rvOrders.setAdapter(adapter);
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
    return histories;
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
