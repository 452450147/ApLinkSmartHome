package com.example.aplinksmarthome;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aplinksmarthome.DataBase.DeviceEnergy;
import com.example.aplinksmarthome.Service.IGetMessageCallBack;
import com.example.aplinksmarthome.Service.MQTTService2;
import com.example.aplinksmarthome.Service.MyServiceConnection2;

public class MqttActivity extends AppCompatActivity implements View.OnClickListener, IGetMessageCallBack {

    private EditText editText;
    private Button button,button2;
    private MyServiceConnection2 serviceConnection;
    private MQTTService2 mqttService;
    private int flag ;
    private MyApplication application;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initServer();
    }

    private void initView(){
        setContentView(R.layout.activity_mqtt);
        editText = findViewById(R.id.et_mqtt);
        button = findViewById(R.id.bt_mqtt);
        button.setOnClickListener(this);
        application = (MyApplication)this.getApplication();
        flag = application.getMqttActivity_flag();
        if (flag == 1){button.setText("已连接，点击断开");   button.setActivated(true);}
    }
    private void initServer(){
        serviceConnection = new MyServiceConnection2();
        serviceConnection.setIGetMessageCallBack(MqttActivity.this);
        Intent intent = new Intent(this, MQTTService2.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_mqtt:
                switch (flag){
                    case 0:
                button.setActivated(true);
                button.setText("已连接，点击断开");
               flag = 1;
                break;
                    case 1:
                        button.setText("点击连接");
                        button.setActivated(false);

                        flag = 0;
                }
        }
    }


    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        application.setMqttActivity_flag(flag);
        super.onDestroy();
    }
    @Override
    public void setMessage(String message) {
        char fir = message.charAt(0);
        char sec = message.charAt(1);

        if (fir == '#') switch (sec){
            case 'D':
                int layer = Integer.parseInt(message.substring(2,4));
                int id = Integer.parseInt(message.substring(4,6));
                int device_name = Integer.parseInt(message.substring(6,8));
                float energy_used = Float.parseFloat(message.substring(8,11)) + Float.parseFloat(message.substring(11,12))*0.1f;
                String date = message.substring(12,18);
                String time = message.substring(18,22);
                DeviceEnergy deviceEnergy = new DeviceEnergy();
                deviceEnergy.setLayer(layer);
                deviceEnergy.setThis_layer_id(id);
                deviceEnergy.setDevice_name(device_name);
                deviceEnergy.setEnergy_used(energy_used);
                deviceEnergy.setDate(date);
                deviceEnergy.setTime(time);
                deviceEnergy.save();
                break;


        }
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }



}