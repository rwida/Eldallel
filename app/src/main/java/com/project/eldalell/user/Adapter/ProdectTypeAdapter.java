package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.project.eldalell.user.Classes.Order;
import com.project.eldalell.user.Classes.ProdectType;
import com.project.eldalell.user.Classes.Product;
import com.project.eldalell.user.Fragments.CategoriesFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

public class ProdectTypeAdapter extends RecyclerView.Adapter<ProdectTypeAdapter.ProdectTypeViewHolder> {

    private ArrayList<ProdectType> prodectTypes;
    private Activity activity;
    private RequestQueue requestQueue;
    public ProdectTypeAdapter(ArrayList<ProdectType> prodectTypes, Activity activity, RequestQueue requestQueue) {
        this.prodectTypes = prodectTypes;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public ProdectTypeAdapter.ProdectTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shops_row, viewGroup, false);
        ProdectTypeAdapter.ProdectTypeViewHolder holder = new ProdectTypeAdapter.ProdectTypeViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdectTypeAdapter.ProdectTypeViewHolder holder, final int i) {

        Glide.with(activity)
                .load(prodectTypes.get(i).getImage())
                .centerCrop()
                .placeholder(R.drawable.btn_filter_shape)
                .into(holder.imgProdectType);
        holder.tvProdectType.setText(prodectTypes.get(i).getItem_category_name());
        holder.ProdectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodectTypeId = prodectTypes.get(i).getId();
                ArrayList<Product> products = new ArrayList<>();
                ArrayList<Order> orders = new ArrayList<>();
                if (prodectTypeId.equals("0")){
                    products = CategoriesFragment.products;
                }else{
                    for (Product product : CategoriesFragment.products)
                    {
                        if (product.getItem_category_id().equals(prodectTypeId)){
                            products.add(product);
                        }
                    }
                }
                for (int i =0; i <products.size();i++){
                    for (int j=0;j<CategoriesFragment.orders.size();j++) {
                        if (CategoriesFragment.orders.get(j).getOrderName().equals(products.get(i).getProductName())) {

                            orders.add(CategoriesFragment.orders.get(j));

                        }
                    }
                }
                ProductAdapter adapter1 = new ProductAdapter(activity, products,orders);
                CategoriesFragment.gvCategories.setAdapter(adapter1);
                CategoriesFragment.typeChange = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodectTypes.size();
    }

    class ProdectTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProdectType;
        TextView tvProdectType;
        ConstraintLayout ProdectType;

        public ProdectTypeViewHolder(View itemView) {
            super(itemView);
            imgProdectType = itemView.findViewById(R.id.imgShopType);
            tvProdectType = itemView.findViewById(R.id.tvShopType);
            ProdectType = itemView.findViewById(R.id.shopType);
        }
    }
}
