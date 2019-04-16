package com.example.aplinksmarthome;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection2 implements ServiceConnection {
    private MQTTService2 mqttService;
    private IGetMessageCallBack IGetMessageCallBack;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService2.CustomBinder)iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public MQTTService2 getMqttService(){
        return mqttService;
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
        this.IGetMessageCallBack = IGetMessageCallBack;
    }
}
