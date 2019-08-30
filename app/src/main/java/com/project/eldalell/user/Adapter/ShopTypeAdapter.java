package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import com.project.eldalell.user.Classes.Shop;
import com.project.eldalell.user.Classes.ShopType;
import com.project.eldalell.user.Fragments.ShopsFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

public class ShopTypeAdapter extends RecyclerView.Adapter<ShopTypeAdapter.ShopTypeViewHolder> {

    private ArrayList<ShopType> shopTypes;
    private Activity activity;
    private RequestQueue requestQueue;
    public ShopTypeAdapter(ArrayList<ShopType> shopTypes, Activity activity, RequestQueue requestQueue) {
        this.shopTypes = shopTypes;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public ShopTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shops_row, viewGroup, false);
        ShopTypeViewHolder holder = new ShopTypeViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopTypeViewHolder holder, final int i) {

        Glide.with(activity)
                .load(shopTypes.get(i).getImage())
                .centerCrop()
                .placeholder(R.drawable.b)
                .into(holder.imgShopType);
        holder.tvShopType.setText(shopTypes.get(i).getShop_type_name());
        holder.shopType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopTypeId = shopTypes.get(i).getId();
                ArrayList<Shop> shops = new ArrayList<>();

                if (shopTypeId.equals("0")){
                    shops = ShopsFragment.shops;
                }else{
                    for (Shop shop : ShopsFragment.shops)
                    {
                        if (shop.getShop_type_id().equals(shopTypeId)){
                            shops.add(shop);
                        }
                    }
                }
                ShopsFragment.rvShops.setLayoutManager(new LinearLayoutManager(activity));
                ShopAdapter Shopadapter = new ShopAdapter(shops, activity,requestQueue);
                ShopsFragment.rvShops.setAdapter(Shopadapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopTypes.size();
    }

    class ShopTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShopType;
        TextView tvShopType;
        ConstraintLayout shopType;

        public ShopTypeViewHolder(View itemView) {
            super(itemView);
            imgShopType = itemView.findViewById(R.id.imgShopType);
            tvShopType = itemView.findViewById(R.id.tvShopType);
            shopType = itemView.findViewById(R.id.shopType);
        }
    }
}
