package com.example.aplinksmarthome;
import org.litepal.crud.LitePalSupport;

public class DeviceManager extends LitePalSupport{
    private int this_layer_id,device_name,switch_status,layer,upper_layer_id;
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
    public int getSwitch_status(){
        return switch_status;
    }
    public void setSwitch_status(int switch_status){
        this.switch_status = switch_status;
    }
    public int getLayer(){
        return layer;
    }
    public void setLayer(int layer){
        this.layer = layer;
    }
    public int getUpper_layer_id(){
        return upper_layer_id;
    }
    public void setUpper_layer_id(int upper_layer_id){
        this.upper_layer_id = upper_layer_id;
    }
}
