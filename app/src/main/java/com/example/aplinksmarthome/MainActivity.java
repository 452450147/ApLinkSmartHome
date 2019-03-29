package com.example.aplinksmarthome;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tv_content, tv_send_text,tv_checkdata;

    private Button bt_send,bt_checkdata,bt_DelTable,bt_test,bt_test2;

    private EditText send_edit;

    public int idplus=1;
    //测试
    String[] testdate = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
    int[] testscore= {50,42,90,33,10,74,22,18,79,20};//图表的数据点




    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();

        //开启服务器

        MobileServer mobileServer = new MobileServer();

        mobileServer.setHandler(handler);

        new Thread(mobileServer).start();



    }



    private void InitView() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_send_text = (TextView) findViewById(R.id.tv_send_text);
        bt_send = (Button) findViewById(R.id.bt_send);
        send_edit = (EditText)findViewById(R.id.send_edit);
        bt_send.setOnClickListener(this);
        tv_checkdata = (TextView) findViewById(R.id.tv_checkdata);
        bt_checkdata = (Button)findViewById((R.id.bt_checkdata));
        bt_DelTable = (Button)findViewById(R.id.bt_DelTable);
        bt_test = (Button)findViewById(R.id.bt_test);
        bt_test2 = (Button)findViewById(R.id.bt_test2);
        bt_checkdata.setOnClickListener(this);
        bt_DelTable.setOnClickListener(this);
        bt_test.setOnClickListener(this);
        bt_test2.setOnClickListener(this);
    }



    @Override

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_send:
                String str = send_edit.getText().toString();
                new SendAsyncTask().execute(str);
                tv_send_text.setText(str);
                break;

            case R.id.bt_checkdata:
               List<EnergyUsed> DataList = LitePal.findAll(EnergyUsed.class);
                tv_checkdata.setText("");
              for(EnergyUsed testused:DataList){
                   tv_checkdata.append(testused.getDate());
              }


             //LitePal.findLast(EnergyUsed.class);

                break;

            case R.id.bt_DelTable:
                Toast.makeText(MainActivity.this, "数据库已删除", Toast.LENGTH_LONG).show();
                LitePal.deleteAll(EnergyUsed.class);
                break;

            case R.id.bt_test:
                Intent LineChartiIntent = new Intent(MainActivity.this,LineChartActivity.class);
                startActivity(LineChartiIntent);
                break;

            case R.id.bt_test2:
                for (int i = 0; i < testdate.length; i++) {
                    EnergyUsed energyUsed = new EnergyUsed();
                   energyUsed.setDate(testdate[i]);
                   energyUsed.setEnergy_used(testscore[i]);
                   energyUsed.save();
                }
                break;

        }



    }



    Handler handler = new Handler() {

        @Override

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:

                   // tv_content.setText("WiFi模块发送的：" + msg.obj);

                    //Toast.makeText(MainActivity.this, "接收到信息", Toast.LENGTH_LONG).show();

                    //测试协议 （0位#表示开始标志，1位表示数据的类型，2-4位表示数值，5-10位表示年月日，10-13表示时间

                    String xieyi = (String)msg.obj;
                    tv_content.setText("WiFi模块发送的：" + xieyi);
                    char fir = xieyi.charAt(0);
                    char sec = xieyi.charAt(1);
                    String PowerData_str = xieyi.substring(2,5);
                    int PowerData_int = Integer.parseInt(PowerData_str);
                    String Date_month = xieyi.substring(7,9);
                    String Date_day = xieyi.substring(9,11);
                    String Date_display = Date_month + "月" + Date_day + "日";
                    //P表示电量数据
                    if (fir == '#')
                    switch (sec){
                        case 'P':
                            Toast.makeText(MainActivity.this, "得到电量数据", Toast.LENGTH_LONG).show();

                            EnergyUsed energyUsed = new EnergyUsed();
                           // energyUsed.setElectrical_appliances_name(xieyi);
                            energyUsed.setEnergy_used(PowerData_int);
                            energyUsed.setDate(Date_display);
                            energyUsed.save();
                            break;
                            default:Toast.makeText(MainActivity.this, "未知数据", Toast.LENGTH_LONG).show();
                                break;

                    }

            }

        }

    };



}
