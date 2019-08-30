package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Fragments.CategoriesFragment;
import com.project.eldalell.user.Fragments.ReviewOrderFragment;
import com.project.eldalell.user.Fragments.TraceOrderFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

public class OrderReviewAdapter extends RecyclerView.Adapter<OrderReviewAdapter.OrderReviewHolder> {

    Activity activity;
    ArrayList<Order> orders;
    boolean editable;

    public OrderReviewAdapter(Activity activity, ArrayList<Order> orders, boolean editable) {
        this.activity = activity;
        this.orders = orders;
        this.editable = editable;
    }

    @NonNull
    @Override
    public OrderReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View r = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_order_row, viewGroup, false);
        OrderReviewHolder orderReviewHolder = new OrderReviewHolder(r);
        return orderReviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderReviewHolder holder, final int i) {
        holder.tvOrderQuantity.setText(orders.get(i).getOrderQuantity() + "");
        holder.tvOrderPrice.setText(orders.get(i).calcTotalOrderPrice() + " EGP");
        holder.tvOrderName.setText(orders.get(i).getOrderName());



    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderReviewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderName, tvOrderPrice, tvOrderQuantity;
        Button btnCancelOrder, btnUpQuantity, btnMinusOrder;

        public OrderReviewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderQuantity = itemView.findViewById(R.id.tvOrderQuantity);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
            btnMinusOrder = itemView.findViewById(R.id.btnMinusOrder);
            btnUpQuantity = itemView.findViewById(R.id.btnUpQuantity);

            if (!editable) {
                btnCancelOrder.setVisibility(View.GONE);
                btnMinusOrder.setVisibility(View.GONE);
                btnUpQuantity.setVisibility(View.GONE);
                TraceOrderFragment.tvSubTotalTrace.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                TraceOrderFragment.tvTotalReviewPriceTrace.setText(String.format("%.2f", (calcTotalPrice() + 10f)) + " EGP");

            }

            btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                    ReviewOrderFragment.tvSubTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                    ReviewOrderFragment.tvTotalReviewPrice.setText(String.format("%.2f", (calcTotalPrice() + 10f)) + " EGP");
                }
            });

            btnMinusOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    int quantity = orders.get(pos).getOrderQuantity();
                    quantity--;
                    tvOrderQuantity.setText(quantity + "");
                    orders.get(pos).setOrderQuantity(quantity);
                    tvOrderPrice.setText(String.format("%.2f", orders.get(pos).calcTotalOrderPrice()) + " EGP");
                    if (quantity <= 0) {
                        delete(pos);
                    }
                    ReviewOrderFragment.tvSubTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                    ReviewOrderFragment.tvTotalReviewPrice.setText(String.format("%.2f", (calcTotalPrice() + 10f)) + " EGP");
                }
            });
            btnUpQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    int quantity = orders.get(i).getOrderQuantity();
                    quantity++;
                    tvOrderQuantity.setText(quantity + "");
                    orders.get(i).setOrderQuantity(quantity);
                    tvOrderPrice.setText(String.format("%.2f", orders.get(i).calcTotalOrderPrice()) + " EGP");
                    ReviewOrderFragment.tvSubTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                    ReviewOrderFragment.tvTotalReviewPrice.setText(String.format("%.2f", (calcTotalPrice() + 10f)) + " EGP");
                }
            });

        }
    }

    public void delete(int position) { //removes the row
        orders.remove(position);
        notifyItemRemoved(position);
    }

    float calcTotalPrice() {
        float total = 0;
        if (editable) {
            for (Order order : CategoriesFragment.orders) {
                total = total + order.calcTotalOrderPrice();
            }
        } else {
            for (Order order : TraceOrderFragment.orders) {
                total = total + order.calcTotalOrderPrice();
            }
        }
        return total;
    }

}
