package com.project.eldalell.user.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.eldalell.user.Classes.Address;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Fragments.AddAddressFragment;
import com.project.eldalell.user.Fragments.OrderSetupFragment;
import com.project.eldalell.user.R;

import java.util.ArrayList;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder> {
    private ArrayList<Address> AddressList;
    private Activity activity;
    RequestQueue requestQueue;

    public AddressesAdapter(ArrayList<Address> AddressList, Activity activity,RequestQueue requestQueue) {
        this.AddressList = AddressList;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public AddressesAdapter.AddressesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View addressesRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_row
                , viewGroup, false);

        AddressesAdapter.AddressesViewHolder holder = new AddressesAdapter.AddressesViewHolder(addressesRow);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesViewHolder addressesViewHolder, int i) {
        Address currentAddress = new Address();

        currentAddress.setCity(AddressList.get(i).getCity());
        currentAddress.setDistrict_id(AddressList.get(i).getDistrict_id());
        currentAddress.setStreet(AddressList.get(i).getStreet());
        currentAddress.setBuildingNumber(AddressList.get(i).getBuildingNumber());
        currentAddress.setFloorNumber(AddressList.get(i).getFloorNumber());
        currentAddress.setApartmentNumber(AddressList.get(i).getApartmentNumber());
        currentAddress.setDistrict(AddressList.get(i).getDistrict());
        currentAddress.setAddressSubject(AddressList.get(i).getAddressSubject());

        addressesViewHolder.tvGovernmentDetails.setText(currentAddress.getCity());
        addressesViewHolder.tvDistrictDetails.setText(currentAddress.getDistrict());
        addressesViewHolder.tvStreetDetails.setText(currentAddress.getStreet());
        addressesViewHolder.tvBuildingDetails.setText(currentAddress.getBuildingNumber());
        addressesViewHolder.tvFloorDetails.setText(currentAddress.getFloorNumber());
        addressesViewHolder.tvApartmentDetails.setText(currentAddress.getApartmentNumber());
        addressesViewHolder.tvHome.setText(currentAddress.getAddressSubject());


    }

    @Override
    public int getItemCount() {
        return AddressList.size();
    }

    class AddressesViewHolder extends RecyclerView.ViewHolder {

        TextView  tvGovernmentDetails, tvDistrictDetails,
                tvStreetDetails, tvBuildingDetails, tvFloorDetails,
                tvApartmentDetails, tvEditAddress, tvDeleteAddress,tvHome;
        CardView cardAddress;

        public AddressesViewHolder(View itemView) {
            super(itemView);

            tvHome = itemView.findViewById(R.id.tvHome);
            cardAddress = itemView.findViewById(R.id.cardAddress);
            tvGovernmentDetails = itemView.findViewById(R.id.tvGovernmentDetails);
            tvDistrictDetails = itemView.findViewById(R.id.tvDistrictDetails);
            tvStreetDetails = itemView.findViewById(R.id.tvStreetDetails);
            tvBuildingDetails = itemView.findViewById(R.id.tvBuildingDetails);
            tvFloorDetails = itemView.findViewById(R.id.tvFloorDetails);
            tvApartmentDetails = itemView.findViewById(R.id.tvApartmentDetails);
            tvEditAddress = itemView.findViewById(R.id.tvEditAddress);
            tvDeleteAddress = itemView.findViewById(R.id.tvDeleteAddress);

            tvDeleteAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition(),requestQueue);
                }
            });
            cardAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OrderSetupFragment.fromOrderSetup) {
                        OrderSetupFragment.currentAddress = AddressList.get(getAdapterPosition());
                        OrderSetupFragment.fromOrderSetup = false;
                        Navigation.findNavController(v).popBackStack(R.id.addressesFragment, true);
                        Navigation.findNavController(v).navigate(R.id.orderSetupFragment);
                    }
                }
            });
            tvEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAddressFragment.fromEdit = true;
                    AddAddressFragment.address = AddressList.get(getAdapterPosition());
                    AddAddressFragment.address = AddressList.get(getAdapterPosition());
                    Navigation.findNavController(v).navigate(R.id.addAddressFragment);
                }
            });
        }
    }

    public void delete(final int position, final RequestQueue requestQueue) { //removes the row
        String addressID = AddressList.get(position).getId();
        Connection connection = new Connection();

        StringRequest request = new StringRequest(Request.Method.DELETE, connection.getDeleteAddress() + addressID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        AddressList.remove(position);
                        notifyItemRemoved(position);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}