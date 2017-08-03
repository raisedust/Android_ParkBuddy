package com.barrery.parkbuddy.Payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barrery.parkbuddy.R;

import java.util.List;


/**
 * Created by Barrery on 2017/7/7.
 */

public class PayAdaper extends ArrayAdapter<PayType>{
    private int resourceId;
    public PayAdaper(Context context, int textViewResourceId, List<PayType> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        PayType payType = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView payImage = (ImageView) view.findViewById(R.id.pay_type_image);
        TextView payName = (TextView) view.findViewById(R.id.pay_type_name);
        payImage.setImageResource(payType.getImageId());
        payName.setText(payType.getName());
        return view;
    }
}
