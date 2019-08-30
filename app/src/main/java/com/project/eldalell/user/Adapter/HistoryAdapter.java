package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.History;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Fragments.OrderHistoryFragment;
import com.project.eldalell.user.Fragments.TraceOrderFragment;
import com.project.eldalell.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<History> HistoryList;
    private Activity activity;
    RequestQueue requestQueue;

    public HistoryAdapter(ArrayList<History> historyList, Activity activity) {
        HistoryList = historyList;
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View historyrow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.historyrow
                , viewGroup, false);

        HistoryViewHolder holder = new HistoryViewHolder(historyrow);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {

        final History curruntHistory = new History();
        curruntHistory.setShopName(HistoryList.get(i).getShopName());
        curruntHistory.setOrderDate(HistoryList.get(i).getOrderDate());
        curruntHistory.setOrderID(HistoryList.get(i).getOrderID());
        curruntHistory.setOrderStatus(HistoryList.get(i).isOrderStatus());
        curruntHistory.setImage(HistoryList.get(i).getImage());

        historyViewHolder.tvName.setText(curruntHistory.getShopName());
        historyViewHolder.tvDate.setText(curruntHistory.getOrderDate());
        historyViewHolder.tvID.setText(curruntHistory.getOrderID());
        historyViewHolder.tvStatue.setText("Successful");
        historyViewHolder.tvStatue.setTextColor(Color.GREEN);
        Glide.with(activity)
                .load(curruntHistory.getImage())
                .centerCrop()
                .placeholder(R.drawable.carfor)
                .into(historyViewHolder.imgLogo);
        historyViewHolder.History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TraceOrderFragment.orders = getOrders(requestQueue, activity, curruntHistory, view);

            }
        });

    }

    private ArrayList<Order> getOrders(final RequestQueue requestQueue,
                                       final Activity activity, final History history, final View v) {
        final ArrayList<Order> orders = new ArrayList<>();
        Connection connection = new Connection();
        final StringRequest request = new StringRequest(Request.Method.GET, connection.getGetInvoice() + history.getOrderID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray a = o.getJSONArray("InvoiceRow");
                            for (int i = 0; i < a.length(); i++) {
                                JSONObject object = a.getJSONObject(i);
                                Order current = new Order();
                                current.setOrderID(history.getOrderID());
                                current.setItem_shop_id(object.getString("item_shop_id"));
                                current.setOrderQuantity(Integer.valueOf(object.getString("quantity")));
                                orders.add(current);
                            }
                            TraceOrderFragment.getInvoice(requestQueue, activity, orders);
                            OrderHistoryFragment.fromHistory = true;
                            Navigation.findNavController(v).navigate(R.id.traceOrderFragment);
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
        return orders;
    }


    @Override
    public int getItemCount() {
        return HistoryList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvID, tvStatue;
        ImageView imgLogo;
        LinearLayout History;
        public HistoryViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvOrder);
            tvDate = itemView.findViewById(R.id.tvOrderDateAndTime);
            tvID = itemView.findViewById(R.id.tvOrderID);
            tvStatue = itemView.findViewById(R.id.tvOrderStatus);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            History = itemView.findViewById(R.id.History);

        }
    }
}
