package com.barrery.parkbuddy.CarLocationInformation;

/**
 * Created by Barrery on 2017/7/3.
 */

public class Car {
    private String name;
    private int imageId;
    public Car(String name,int imageId){
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
