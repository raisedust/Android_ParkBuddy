package com.barrery.parkbuddy.Payment;

/**
 * Created by Barrery on 2017/7/7.
 */

public class PayType {
    private String name;
    private int imageId;
    public PayType(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
