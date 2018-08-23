package com.example.apn.vortex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by APN on 1/22/2018.
 */

public class SupplierAdzAdapter extends RecyclerView.Adapter<SupplierAdzAdapter.ViewHolder> {

    private List<SupplierAdzList> supplierAdzLists;

    public SupplierAdzAdapter(List<SupplierAdzList> supplierAdzLists, Context context) {
        this.supplierAdzLists = supplierAdzLists;
        this.context = context;
    }

    private Context context;

    @Override
    public SupplierAdzAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supplier_adz_item,parent,false);
        return new SupplierAdzAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SupplierAdzAdapter.ViewHolder holder, int position) {
        final SupplierAdzList supplierAdzList = supplierAdzLists.get(position);

        holder.adzName.setText(supplierAdzList.getAdzName());
        holder.adzDescription.setText(supplierAdzList.getAdzDescription());
        holder.priceFroService.setText("Rs:"+supplierAdzList.getPriceFroService());
        holder.contactNo.setText("More info: "+supplierAdzList.getContactNumber());

        Picasso.with(context)
                .load(supplierAdzList.getAdzUrl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return supplierAdzLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView adzName;
        public TextView adzDescription;
        public TextView priceFroService;
        public TextView contactNo;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            adzName = (TextView) itemView.findViewById(R.id.adzname);
            adzDescription = (TextView) itemView.findViewById(R.id.adzdescription);
            priceFroService = (TextView) itemView.findViewById(R.id.price);
            contactNo= (TextView) itemView.findViewById(R.id.contactno);
            imageView = (ImageView) itemView.findViewById(R.id.adzphoto);
        }
    }
}
