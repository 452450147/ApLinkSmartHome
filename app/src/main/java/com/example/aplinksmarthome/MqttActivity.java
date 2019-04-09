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

import com.zs.easy.mqtt.EasyMqttService;

import com.zs.easy.mqtt.IEasyMqttCallBack;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class MqttActivity extends Activity implements View.OnClickListener{
    private EasyMqttService mqttService;

    /**

     * 回调时使用

     */

    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private EditText editText;
    private Button button,button2;
    private int flag ;
    private MyApplication application;
    private String topic_recive = "a";

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(this,

                    new String[]{Manifest.permission.READ_PHONE_STATE},

                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

        }

        buildEasyMqttService();



//        disconnect();



//        close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_mqtt:
                switch (flag){
                    case 0:
                button.setActivated(true);
                button.setText("已连接，点击断开");
                connect();
                      try {subscribe();}catch (Exception e){
                          Toast.makeText(MqttActivity.this,"出错了",Toast.LENGTH_LONG).show();
                          e.printStackTrace();
                      }

                        flag = 1;
                break;
                    case 1:
                        button.setText("点击连接");
                        button.setActivated(false);
                        disconnect();

                        flag = 0;
                }
        }
    }


    @Override
    protected void onDestroy() {
        application.setMqttActivity_flag(flag);
        super.onDestroy();
    }
    private boolean isConnected() {

        return mqttService.isConnected();

    }



    /**

     * 发布消息

     */

    private void publish(String msg, String topic, int qos, boolean retained) {

        mqttService.publish(msg, topic, qos, retained);

    }



    /**

     * 断开连接

     */

    private void disconnect() {

        mqttService.disconnect();

    }



    /**

     * 关闭连接

     */

    private void close() {

        mqttService.close();

    }



    /**

     * 订阅主题 这里订阅三个主题分别是"a", "b", "c"

     */

    private void subscribe() {

        String[] topics = new String[]{topic_recive, "b", "c"};

        //主题对应的推送策略 分别是0, 1, 2 建议服务端和客户端配置的主题一致

        // 0 表示只会发送一次推送消息 收到不收到都不关心

        // 1 保证能收到消息，但不一定只收到一条

        // 2 保证收到切只能收到一条消息

        int[] qoss = new int[]{0, 1, 2};

        mqttService.subscribe(topics, qoss);

    }



    /**

     * 连接Mqtt服务器

     */

    private void connect() {

        mqttService.connect(new IEasyMqttCallBack() {

            @Override

            public void messageArrived(String topic, String message, int qos) {
                Toast.makeText(MqttActivity.this, "得到数据", Toast.LENGTH_SHORT).show();
              //  if (topic == topic_recive){
                    String xieyi = message;
                    char fir = xieyi.charAt(0);
                    char sec = xieyi.charAt(1);
                    char switch_status_char = xieyi.charAt(6);
                    boolean switch_status = false;
                    if (switch_status_char == '1'){switch_status = true;}
                    if (switch_status_char == '0'){switch_status = false;}
                    String layer = xieyi.substring(2,4);
                    String device_id = xieyi.substring(4,6);
                    if (fir == '#')
                        switch (sec){
                            case 'R':
                                Toast.makeText(MqttActivity.this, "得到电量数据", Toast.LENGTH_SHORT).show();
                                DeviceManager deviceManager = new DeviceManager();
                                deviceManager.setSwitch_status(switch_status);
                                deviceManager.updateAll("layer = ? and this_layer_id = ?",layer,device_id);

                                break;
                            default:Toast.makeText(MqttActivity.this, "未知数据", Toast.LENGTH_LONG).show();
                                break;

                        }
                }
                //推送消息到达

        //    }



            @Override

            public void connectionLost(Throwable arg0) {
                Toast.makeText(MqttActivity.this,"连接断开",Toast.LENGTH_LONG).show();
                //连接断开

            }



            @Override

            public void deliveryComplete(IMqttDeliveryToken arg0) {



            }



            @Override

            public void connectSuccess(IMqttToken arg0) {

                //连接成功

            }



            @Override

            public void connectFailed(IMqttToken arg0, Throwable arg1) {
                //连接失败

            }

        });

    }



    /**

     * 构建EasyMqttService对象

     */

    private void buildEasyMqttService() {

        mqttService = new EasyMqttService.Builder()

                //设置自动重连

                .autoReconnect(true)

                //设置不清除回话session 可收到服务器之前发出的推送消息

                .cleanSession(false)

                //唯一标示 保证每个设备都唯一就可以 建议 imei

                .clientId("your clientId")

                //mqtt服务器地址 格式例如：tcp://10.0.261.159:1883

                .serverUrl("tcp://167.179.84.221:1883")

                //心跳包默认的发送间隔

                .keepAliveInterval(20)

                //构建出EasyMqttService 建议用application的context

                .bulid(this.getApplicationContext());

    }
}