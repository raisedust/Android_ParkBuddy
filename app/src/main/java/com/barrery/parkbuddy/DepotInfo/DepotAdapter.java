package com.barrery.parkbuddy.DepotInfo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barrery.parkbuddy.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Barrery on 2017/7/10.
 */

public class DepotAdapter extends RecyclerView.Adapter<DepotAdapter.ViewHolder> {
    private Context mContext;
    private List<Depot> mDpeotList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView depotImage;
        TextView depotName;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            depotImage = (ImageView) view.findViewById(R.id.depot_image);
            depotName = (TextView) view.findViewById(R.id.depot_name);
        }
    }
    public DepotAdapter(List<Depot> depotList){
        mDpeotList = depotList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int ViewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.depot_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Depot depot = mDpeotList.get(position);
        holder.depotName.setText(depot.getName());
        Glide.with(mContext).load(depot.getImageId()).into(holder.depotImage);
    }
    @Override
    public int getItemCount(){
        return mDpeotList.size();
    }
}
