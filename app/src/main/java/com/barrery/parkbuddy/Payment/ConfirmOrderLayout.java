package com.barrery.parkbuddy.Payment;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.barrery.parkbuddy.R;

/**
 * Created by Barrery on 2017/7/5.
 */

public class ConfirmOrderLayout extends LinearLayout {
    public ConfirmOrderLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.confirm_order,this);
        Button titleBack = (Button) findViewById(R.id.title_back_1);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
    }
}
