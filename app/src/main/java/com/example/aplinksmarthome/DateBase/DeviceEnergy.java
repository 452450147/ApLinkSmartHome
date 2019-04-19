package com.example.aplinksmarthome.DateBase;

import org.litepal.crud.LitePalSupport;

public class DeviceEnergy extends LitePalSupport {
    private int this_layer_id,device_name,layer;
    private float energy_used;
    private String date,time;
    public int getThis_layer_id(){
        return this_layer_id;
    }
    public void setThis_layer_id(int this_layer_id){
        this.this_layer_id = this_layer_id;
    }
    public int getDevice_name(){
        return device_name;
    }
    public void setDevice_name(int device_name){
        this.device_name = device_name;
    }
    public float getEnergy_used(){
        return energy_used;
    }
    public void setEnergy_used(float energy_used){
        this.energy_used = energy_used;
    }

    public int getLayer(){
        return layer;
    }
    public void setLayer(int layer){
        this.layer = layer;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }


}
