package com.barrery.parkbuddy.DepotInfo;

/**
 * Created by Barrery on 2017/7/10.
 */

public class Depot {
    private String name;
    private int imageId;
    public Depot(String name,int imageId){
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
