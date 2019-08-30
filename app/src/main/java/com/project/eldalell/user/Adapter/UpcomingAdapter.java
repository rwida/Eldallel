package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Classes.Upcoming;
import com.project.eldalell.user.Fragments.OrderHistoryFragment;
import com.project.eldalell.user.Fragments.TraceOrderFragment;
import com.project.eldalell.user.Fragments.UpcomingFragment;
import com.project.eldalell.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {
    private ArrayList<Upcoming> upcomings;
    private Activity activity;
    RequestQueue requestQueue;

    public UpcomingAdapter(ArrayList<Upcoming> upcomings, Activity activity, RequestQueue requestQueue) {
        this.upcomings = upcomings;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public UpcomingAdapter.UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View historyrow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_row
                , viewGroup, false);

        UpcomingAdapter.UpcomingViewHolder holder = new UpcomingAdapter.UpcomingViewHolder(historyrow);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.UpcomingViewHolder upcomingViewHolder, int i) {

        final Upcoming upcoming = new Upcoming();
        upcoming.setShopName(upcomings.get(i).getShopName());
        upcoming.setOrderDate(upcomings.get(i).getOrderDate());
        upcoming.setOrderID(upcomings.get(i).getOrderID());
        upcoming.setOrderStatus(upcomings.get(i).isOrderStatus());
        upcoming.setImage(upcomings.get(i).getImage());

        upcomingViewHolder.tvName.setText(upcoming.getShopName());
        upcomingViewHolder.tvDate.setText(upcoming.getOrderDate());
        upcomingViewHolder.tvID.setText(upcoming.getOrderID());
        if (upcoming.isOrderStatus()) {
            upcomingViewHolder.tvStatue.setText("Delivery Take Order");
            upcomingViewHolder.tvStatue.setTextColor(Color.GREEN);
        } else {
            upcomingViewHolder.tvStatue.setText("order Submitted");
            upcomingViewHolder.tvStatue.setTextColor(Color.RED);
        }
        Glide.with(activity)
                .load(upcoming.getImage())
                .centerCrop()
                .placeholder(R.drawable.carfor)
                .into(upcomingViewHolder.imgUpcoming);

        upcomingViewHolder.upcomingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TraceOrderFragment.orders = getOrders(requestQueue, activity, upcoming, view);
            }
        });

    }

    private ArrayList<Order> getOrders(final RequestQueue requestQueue, final Activity activity, final Upcoming upcoming, final View v) {
        final ArrayList<Order> orders = new ArrayList<>();
        Connection connection = new Connection();
        final StringRequest request = new StringRequest(Request.Method.GET, connection.getGetInvoice() + upcoming.getOrderID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray a = o.getJSONArray("InvoiceRow");
                            for (int i = 0; i < a.length(); i++) {
                                JSONObject object = a.getJSONObject(i);
                                Order current = new Order();
                                current.setOrderID(upcoming.getOrderID());
                                current.setItem_shop_id(object.getString("item_shop_id"));
                                current.setOrderQuantity(Integer.valueOf(object.getString("quantity")));
                                current.setOrderState(upcoming.isOrderStatus());
                                orders.add(current);
                            }

                            UpcomingFragment.inUpComing = true;
                            TraceOrderFragment.getInvoice(requestQueue, activity, orders);
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
        return upcomings.size();
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvID, tvStatue;
        CardView upcomingCard;
        ImageView imgUpcoming;

        public UpcomingViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvOrderUp);
            tvDate = itemView.findViewById(R.id.tvOrderDateAndTimeUp);
            tvID = itemView.findViewById(R.id.tvOrderIDUp);
            tvStatue = itemView.findViewById(R.id.tvOrderStatusUp);
            upcomingCard = itemView.findViewById(R.id.upcomingCard);
            imgUpcoming = itemView.findViewById(R.id.imgUpcoming);

        }
    }
}