package com.project.eldalell.user.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Shop;
import com.project.eldalell.user.Fragments.CategoriesFragment;
import com.project.eldalell.user.Fragments.ShopsFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

import androidx.navigation.Navigation;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private ArrayList<Shop> shops;
    private Activity activity;
    private RequestQueue requestQueue;

    public ShopAdapter(ArrayList<Shop> shops, Activity activity, RequestQueue requestQueue) {
        this.shops = shops;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_row, viewGroup, false);
        return new ShopViewHolder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, final int i) {
        final Shop shop = new Shop();

        shop.setShopDetails(shops.get(i).getShopDetails());
        shop.setShopName(shops.get(i).getShopName());
        shop.setShopLogo(shops.get(i).getShopLogo());
        shop.setStatework(shops.get(i).isStatework());
        shop.setShopRate(shops.get(i).getShopRate());
        shop.setDelivery_time(shops.get(i).getDelivery_time());

        holder.tvDetailsShop.setText(shop.getShopDetails());
        Glide.with(activity)
                .load(shops.get(i).getShopLogo())
                .centerCrop()
                .placeholder(R.drawable.carfor)
                .into(holder.imgShopLogo);
        holder.tvShopName.setText(shop.getShopName());
        if (shop.isStatework()) {
            holder.tvstateWork.setText("Open");
        } else {
            holder.tvstateWork.setText("Closed");
            holder.tvstateWork.setTextColor(Color.RED);
        }
        holder.tvShopRate.setText("Rating: " + shop.getShopRate());
        holder.tvTimeDel.setText(shop.getDelivery_time()+" mint");

        holder.cvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shop.isStatework()) {
                    MainActivity.shopname = shop.getShopName();
                    MainActivity.shop = shops.get(i);
                    ShopsFragment.inShops = false;
                    CategoriesFragment.ProdectTypes = CategoriesFragment.getProdectTypes(requestQueue,activity);
                    CategoriesFragment.products = CategoriesFragment.getProducts(requestQueue,activity);
                    Navigation.findNavController(v).navigate(R.id.action_shopsFragment_to_categoriesFragment);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShopLogo;
        TextView tvShopName, tvShopRate, tvDetailsShop, tvTimeDel, tvstateWork;
        CardView cvShop;

        ShopViewHolder(View itemView) {
            super(itemView);
            imgShopLogo = itemView.findViewById(R.id.imgShopLogo);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvDetailsShop = itemView.findViewById(R.id.tvDetailsShop);
            tvstateWork = itemView.findViewById(R.id.tvstateWork);
            tvTimeDel = itemView.findViewById(R.id.tvTimeDel);
            tvShopRate = itemView.findViewById(R.id.tvShopRate);
            cvShop = itemView.findViewById(R.id.cvShop);
        }
    }
}
