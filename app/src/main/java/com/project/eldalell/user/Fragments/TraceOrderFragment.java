package com.project.eldalell.user.Fragments;

import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Adapter.OrderReviewAdapter;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.R;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class TraceOrderFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public TraceOrderFragment() {
  }


  public static TraceOrderFragment newInstance(String param1, String param2) {
    TraceOrderFragment fragment = new TraceOrderFragment();
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

    public static CircleImageView imgStep1, imgStep2, imgStep3;
    TextView tvStep1, tvStep2, tvStep3, tvTitle, tvSelectedAddress;
    public static TextView tvOrderID;
  ImageView imgBackBar, search;
    public static RecyclerView rvOrderPreview;
  Toolbar mainToolBar;
  public static ArrayList<Order> orders;
  public static TextView tvSubTotalTrace, tvTotalReviewPriceTrace;
  public NavController navController;
    RequestQueue requestQueue;
    public static ProgressBar traceOrderFragmentProgress;
    public static LinearLayout traceOrderFragmentLayout;
    Activity activity = getActivity();
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_trace_order, container, false);
      requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
    rvOrderPreview = v.findViewById(R.id.rvOrderPreview);
    tvSubTotalTrace = v.findViewById(R.id.tvSubTotalTrace);
    tvSelectedAddress = getActivity().findViewById(R.id.tvSelectedAddress);
    tvTotalReviewPriceTrace = v.findViewById(R.id.tvTotalReviewPriceTrace);
      traceOrderFragmentLayout = v.findViewById(R.id.traceOrderFragmentLayout);
      traceOrderFragmentProgress = v.findViewById(R.id.traceOrderFragmentProgress);
    imgStep1 = v.findViewById(R.id.imgStep1);
    imgStep2 = v.findViewById(R.id.imgStep2);
    imgStep3 = v.findViewById(R.id.imgStep3);
    tvStep1 = v.findViewById(R.id.tvStep1);
    tvStep2 = v.findViewById(R.id.tvStep2);
    tvStep3 = v.findViewById(R.id.tvStep3);
    navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    tvTitle = getActivity().findViewById(R.id.tvTitleMain);
    imgBackBar = getActivity().findViewById(R.id.imgBackBar);
    search = getActivity().findViewById(R.id.search);
    mainToolBar = getActivity().findViewById(R.id.mainToolBar);
      tvOrderID = v.findViewById(R.id.tvOrderID);
    tvSelectedAddress.setVisibility(View.GONE);
    tvTitle.setText("Order Progress");
    tvTitle.setVisibility(View.VISIBLE);
    imgBackBar.setVisibility(View.VISIBLE);
    search.setVisibility(View.GONE);
    mainToolBar.setVisibility(View.VISIBLE);

    MainActivity.setDrawerState(false);

    imgBackBar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.popBackStack(R.id.traceOrderFragment, true);
        navController.navigate(R.id.upcomingFragment);
        imgBackBar.setVisibility(View.GONE);
        MainActivity.setDrawerState(true);
        UpcomingFragment.fromUpcoming = true;
      }
    });

    Drawable background = getContext().getResources().getDrawable(R.drawable.btn_filter_shape);
      Drawable back = getContext().getResources().getDrawable(R.drawable.step_unfinish);
    imgStep1.setBackground(background);


      if (OrderHistoryFragment.fromHistory) {
          imgStep2.setBackground(background);
          imgStep3.setBackground(background);
          OrderHistoryFragment.fromHistory = false;
      } else if (UpcomingFragment.inUpComing) {
          if (orders.get(0).isOrderState()) {
              imgStep2.setBackground(background);
              imgStep3.setBackground(back);
          } else {
              imgStep2.setBackground(back);
              imgStep3.setBackground(back);
          }
          UpcomingFragment.inUpComing = false;
      } else {
          traceOrderFragmentProgress.setVisibility(View.GONE);
          traceOrderFragmentLayout.setVisibility(View.VISIBLE);
          rvOrderPreview.setLayoutManager(new LinearLayoutManager(getActivity()));
          OrderReviewAdapter adapter = new OrderReviewAdapter(getActivity(), orders, false);
          rvOrderPreview.setAdapter(adapter);
          tvOrderID.setText("Order #" + orders.get(0).getOrderID());
      }

      activity = getActivity();

      PusherOptions options = new PusherOptions();
      options.setCluster("eu");

      Pusher pusher = new Pusher("837136e6b742d51c4fd6", options);
      Channel channel = pusher.subscribe("boolean");
      channel.bind("BolleanEvent", new SubscriptionEventListener() {
          @Override
          public void onEvent(String channelName, String eventName, final String data) {
              activity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          JSONObject object = new JSONObject(data);
                          String id = object.getString("id");
                          if (id.equals(orders.get(0).getOrderID())) {
                              String delivery_man_accepted = object.getString("delivery_man_accepted");
                              if (delivery_man_accepted.equals("1")) {
                                  Drawable background = activity.getResources().getDrawable(R.drawable.btn_filter_shape);
                                  imgStep2.setBackground(background);
                              }
                          }
                      } catch (JSONException e) {
                          e.printStackTrace();
                          try {
                              JSONObject object = new JSONObject(data);
                              String id = object.getString("id");
                              if (id.equals(orders.get(0).getOrderID())) {
                                  String order_done = object.getString("order_done");
                                  if (order_done.equals("1")) {
                                      Drawable background = activity.getResources().getDrawable(R.drawable.btn_filter_shape);
                                      imgStep3.setBackground(background);
                                  }
                              }
                          } catch (JSONException e1) {

                          }
                      }

                  }
              });

          }
      });

      pusher.connect();
    return v;
  }

    public static void getInvoice(RequestQueue requestQueue, final Context context, final ArrayList<Order> orders) {
        Connection connection = new Connection();
        for (int i = 0; i < orders.size(); i++) {
            final int finalI = i;
            StringRequest request = new StringRequest(Request.Method.GET, connection.getGetItemswithItem() + orders.get(i).getItem_shop_id(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject o = new JSONObject(response);
                                JSONArray items = o.getJSONArray("item");
                                JSONObject item = items.getJSONObject(0);
                                orders.get(finalI).setOrderName(item.getString("item_name"));
                                orders.get(finalI).setOrderPrice(Float.parseFloat(item.getString("new_retail_price")));

                                rvOrderPreview.setLayoutManager(new LinearLayoutManager(context));
                                OrderReviewAdapter adapter = new OrderReviewAdapter((Activity) context, orders, false);
                                rvOrderPreview.setAdapter(adapter);
                                tvOrderID.setText("Order #" + orders.get(0).getOrderID());
                                traceOrderFragmentProgress.setVisibility(View.GONE);
                                traceOrderFragmentLayout.setVisibility(View.VISIBLE);

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
        }
        if (imgStep2 != null) {
            if (UpcomingFragment.fromUpcoming) {
                if (orders.get(0).isOrderState()) {
                    Drawable back = context.getResources().getDrawable(R.drawable.step_unfinish);
                    Drawable backe = context.getResources().getDrawable(R.drawable.btn_filter_shape);
                    TraceOrderFragment.imgStep2.setBackground(backe);
                    TraceOrderFragment.imgStep3.setBackground(back);
                } else if (!orders.get(0).isOrderState()) {
                    Drawable back = context.getResources().getDrawable(R.drawable.step_unfinish);
                    TraceOrderFragment.imgStep2.setBackground(back);
                    TraceOrderFragment.imgStep3.setBackground(back);

                }
            } else {
                Drawable backe = context.getResources().getDrawable(R.drawable.btn_filter_shape);
                TraceOrderFragment.imgStep2.setBackground(backe);
                TraceOrderFragment.imgStep3.setBackground(backe);
            }
        }


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