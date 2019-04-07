package com.example.aplinksmarthome;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zs.easy.mqtt.EasyMqttService;

import com.zs.easy.mqtt.IEasyMqttCallBack;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class MqttActivity extends Activity implements View.OnClickListener,IGetMessageCallBack{

    private MyServiceConnection serviceConnection;
    private MqttService mqttService;

    //private EasyMqttService mqttService;
    private EditText editText, editText2;
    private Button button,button2;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mqtt);
        editText = (EditText) findViewById(R.id.et_mqtt);
        button = (Button) findViewById(R.id.bt_mqtt);
        editText2 = (EditText) findViewById(R.id.et_mqtt2);
        button2 = (Button) findViewById(R.id.bt_mqtt2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MqttActivity.this);

        Intent intent = new Intent(this, MqttService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_mqtt:
             //   String str = editText.getText().toString();
              // publish(str,"windows",0,true);
                MqttService.publish("测试");
                break;
            case R.id.bt_mqtt2:
            //    String str_2 = editText2.getText().toString();
             //   buildEasyMqttService_check(str_2);
              //  editText2.setText("tcp://167.179.84.221:1883");
             //   connect();
                break;
        }
    }

    @Override
    public void setMessage(String message) {
        editText.setText(message);
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}