package com.project.eldalell.user.Adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Classes.Product;
import com.project.eldalell.user.Fragments.CategoriesFragment;
import com.project.eldalell.user.Fragments.ReviewOrderFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Product> products;
    private LayoutInflater thisInflater;
    // Gets the context so it can be used later
    int totalOrdernum = 0;
    ArrayList<Order> orders;

    public ProductAdapter(Context mContext, ArrayList<Product> products , ArrayList<Order> orders) {
        this.mContext = mContext;
        this.products = products;
        this.thisInflater = LayoutInflater.from(mContext);
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView tvProductQuantity, tvProductName, tvProductPrice;
        final ImageView imgProduct, imgMinus;
        final CardView itemCard;

        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.category_row, parent, false);
            tvProductQuantity = convertView.findViewById(R.id.tvProductQuantity);
            tvProductName = convertView.findViewById(R.id.tvProductName);
            tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
            imgProduct = convertView.findViewById(R.id.imgProduct);
            imgMinus = convertView.findViewById(R.id.imgMinus);
            itemCard = convertView.findViewById(R.id.itemCard);


            tvProductName.setText(products.get(position).getProductName());
            tvProductPrice.setText(products.get(position).getProductPrice() + " EGP");

            Glide.with(mContext)
                    .load(products.get(position).getImgProduct())
                    .centerCrop()
                    .placeholder(R.drawable.shay)
                    .into(imgProduct);

            if (orders!=null){
                for (Order order: orders){
                    if (products.get(position).getProductName().equals(order.getOrderName())) {
                        products.get(position).setNum(order.getOrderQuantity());
                        totalOrdernum = totalOrdernum + order.getOrderQuantity();
                        imgMinus.setVisibility(View.VISIBLE);
                        tvProductQuantity.setVisibility(View.VISIBLE);
                        tvProductQuantity.setText("X" + order.getOrderQuantity());
                        CategoriesFragment.tvNomTotal.setText(totalOrdernum + "");
                    }
                }

            }

            if (ReviewOrderFragment.fromReview) {
                CategoriesFragment.orders = ReviewOrderFragment.orders;

                for (Order order : CategoriesFragment.orders) {

                    if (products.get(position).getProductName().equals(order.getOrderName())) {
                        products.get(position).setNum(order.getOrderQuantity());
                        totalOrdernum = totalOrdernum + order.getOrderQuantity();
                        imgMinus.setVisibility(View.VISIBLE);
                        tvProductQuantity.setVisibility(View.VISIBLE);
                        tvProductQuantity.setText("X" + order.getOrderQuantity());
                        CategoriesFragment.tvNomTotal.setText(totalOrdernum + "");
                    }

                }

                CategoriesFragment.relativeEndOrder.setVisibility(View.VISIBLE);
                CategoriesFragment.tvPriceTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                ReviewOrderFragment.fromReview = false;
                
            }

            itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = products.get(position).getNum();
                    if (num == 0) {
                        tvProductQuantity.setVisibility(View.VISIBLE);
                        CategoriesFragment.relativeEndOrder.setVisibility(View.VISIBLE);


                        imgMinus.setVisibility(View.VISIBLE);
                        num++;
                        totalOrdernum++;
                        tvProductQuantity.setText("X" + num);
                        CategoriesFragment.orders.add(new Order(1,
                                products.get(position).getProductPrice(),
                                products.get(position).getProductName(),products.get(position).getId()));
                        CategoriesFragment.tvNomTotal.setText(totalOrdernum + "");
                        CategoriesFragment.tvPriceTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                        products.get(position).setNum(num);
                    } else {
                        num++;
                        totalOrdernum++;
                        modifyQuntity(products.get(position).getProductName(), num);
                        tvProductQuantity.setText("X" + num);
                        products.get(position).setNum(num);
                        CategoriesFragment.tvNomTotal.setText(totalOrdernum + "");
                        CategoriesFragment.tvPriceTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                    }
                }
            });
            imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = products.get(position).getNum();
                    num--;
                    totalOrdernum--;
                    if (totalOrdernum == 0) {
                        CategoriesFragment.relativeEndOrder.setVisibility(View.GONE);
                    }
                    if (num == 0) {
                        removeOrder(products.get(position).getProductName());
                        tvProductQuantity.setVisibility(View.INVISIBLE);
                        imgMinus.setVisibility(View.INVISIBLE);
                        products.get(position).setNum(num);
                    } else {
                        modifyQuntity(products.get(position).getProductName(), num);
                        tvProductQuantity.setText("X" + num);
                        products.get(position).setNum(num);
                    }
                    CategoriesFragment.tvNomTotal.setText(totalOrdernum + "");
                    CategoriesFragment.tvPriceTotal.setText(String.format("%.2f", calcTotalPrice()) + " EGP");
                }
            });


        }


        return convertView;
    }

    float calcTotalPrice() {
        float total = 0;
        for (Order order : CategoriesFragment.orders) {
            total = total + order.calcTotalOrderPrice();
        }
        return total;
    }

    void removeOrder(String name) {

        for (int j = 0; j < CategoriesFragment.orders.size(); j++) {
            if (CategoriesFragment.orders.get(j).getOrderName().equals(name)) {
                CategoriesFragment.orders.remove(j);
                break;
            }
        }
    }

    void modifyQuntity(String name, int quantity) {
        for (int i = 0; i < CategoriesFragment.orders.size(); i++) {
            if (CategoriesFragment.orders.get(i).getOrderName().equals(name)) {
                CategoriesFragment.orders.get(i).setOrderQuantity(quantity);
                break;
            }
        }
    }
}
