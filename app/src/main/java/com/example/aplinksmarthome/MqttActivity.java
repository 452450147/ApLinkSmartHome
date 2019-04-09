package com.example.aplinksmarthome;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MqttActivity extends Activity implements View.OnClickListener,IGetMessageCallBack{


    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private EditText editText;
    private Button button,button2;
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    private int flag ;
    private MyApplication application;
    private String topic_recive = "a";
    private String clientId_set = "111";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mqtt);
        editText = (EditText)findViewById(R.id.et_mqtt);
        button = (Button) findViewById(R.id.bt_mqtt);
        button.setOnClickListener(this);
        application = (MyApplication)this.getApplication();
        flag = application.getMqttActivity_flag();
        if (flag == 1){button.setText("已连接，点击断开");   button.setActivated(true);}
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MqttActivity.this);

        Intent intent = new Intent(this, MQTTService.class);

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
        Toast.makeText(MqttActivity.this,message,Toast.LENGTH_LONG).show();

        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }

}