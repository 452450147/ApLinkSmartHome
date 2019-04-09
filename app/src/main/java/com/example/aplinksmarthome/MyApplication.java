package com.example.aplinksmarthome;

import android.app.Application;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static int mqttActivity_flag ;
    @Override
    public void onCreate() {
        LitePal.initialize(this);
        super.onCreate();
        mqttActivity_flag = 0;
    }


    public int getMqttActivity_flag(){
        return mqttActivity_flag;
    }
    public void setMqttActivity_flag(int mqttActivity_flag){
        this.mqttActivity_flag = mqttActivity_flag;
    }
}
