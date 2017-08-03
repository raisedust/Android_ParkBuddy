package com.barrery.parkbuddy.CarLocationInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barrery.parkbuddy.CarLocationInformation.Car;
import com.barrery.parkbuddy.R;

import java.util.List;

/**
 * Created by Barrery on 2017/7/3.
 */

public class CarAdaper extends ArrayAdapter<Car> {
    private int resourceId;
    public CarAdaper(Context context, int textViewResourceId, List<Car> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Car car = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView carImage = (ImageView) view.findViewById(R.id.car_image);
        TextView carName = (TextView) view.findViewById(R.id.car_name);
        carImage.setImageResource(car.getImageId());
        carName.setText(car.getName());
        return view;
    }
}
