package com.example.aplinksmarthome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplinksmarthome.DataBase.EnergyUsed;
import com.example.aplinksmarthome.Service.WiFiServer;
import com.example.aplinksmarthome.Service.SendAsyncTask;

public class WifiUseActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_content;
    private Button bt_send,bt_loginwifi;
    private EditText send_edit,ip_edit,socket_edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        OpenServer();
    }
    private void initview(){
        setContentView(R.layout.wifiuse);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        tv_content = findViewById(R.id.tv_content);
        send_edit = findViewById(R.id.tv_send);
        ip_edit =  findViewById(R.id.ip_edit);
        socket_edit =  findViewById(R.id.socket_edit);
        bt_send = findViewById(R.id.bt_send);
        bt_loginwifi =  findViewById(R.id.bt_loginwifi);
        bt_send.setOnClickListener(this);
        bt_loginwifi.setOnClickListener(this);
    }

    private void OpenServer(){
        //开启服务器
        WiFiServer mobileServer = new WiFiServer();
        mobileServer.setHandler(handler);
        new Thread(mobileServer).start();
    }
    Handler handler = new Handler() {

        @Override

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:
                    //测试协议 （0位#表示开始标志，1位表示数据的类型，2-4位表示数值，5-10位表示年月日，10-13表示时间
                    String xieyi = (String)msg.obj;
                    tv_content.setText("WiFi模块发送的：" + xieyi);
                    char fir = xieyi.charAt(0);
                    char sec = xieyi.charAt(1);
                    String PowerData_str = xieyi.substring(2,5);
                    int PowerData_int = Integer.parseInt(PowerData_str);
                    String Date_month;
                    if (!xieyi.substring(7,8).equals("0")){
                        Date_month = xieyi.substring(7,9);}
                    else  Date_month = xieyi.substring(7,8);
                    String Date_day = xieyi.substring(9,11);
                    String Date_display = Date_month + "月" + Date_day + "日";
                    //P表示电量数据
                    if (fir == '#')
                        switch (sec){
                            case 'P':
                                Toast.makeText(WifiUseActivity.this, "得到电量数据", Toast.LENGTH_LONG).show();

                                EnergyUsed energyUsed = new EnergyUsed();
                                // energyUsed.setElectrical_appliances_name(xieyi);
                                energyUsed.setEnergy_used(PowerData_int);
                                energyUsed.setDate(Date_display);
                                energyUsed.save();
                                break;
                            default:Toast.makeText(WifiUseActivity.this, "未知数据", Toast.LENGTH_LONG).show();
                                break;

                        }

            }

        }

    };

    @Override

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_send:
                String str = send_edit.getText().toString();
                new SendAsyncTask().execute(str);
                break;
            case R.id.bt_loginwifi:
                String ip_edit_str = ip_edit.getText().toString();
                String socket_edit_str = socket_edit.getText().toString();
                int socket_edit_int = Integer.parseInt(socket_edit_str);

                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:

                break;
            case R.id.backup:
                finish();

                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
}
