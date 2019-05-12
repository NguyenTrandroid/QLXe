package com.example.giuaki.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.giuaki.CarActivity;
import com.example.giuaki.Models.Catalog;
import com.example.giuaki.R;


import java.util.List;

/**
 * Created by yarolegovich on 08.03.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Catalog> data;
    Context context;

    public ForecastAdapter(List<Catalog> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_catalog, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Catalog catalog = data.get(position);
        Glide.with(holder.itemView.getContext())
                .load(catalog.getImg())
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CarActivity.class);
                intent.putExtra("MaLoai", catalog.getId());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.city_image);
            cardView = itemView.findViewById(R.id.cv_catalog);
        }
    }
}
