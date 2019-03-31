package com.example.aplinksmarthome;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private TextView tv_content, tv_send_text,tv_checkdata;

    private Button bt_send,bt_checkdata,bt_DelTable,bt_test,bt_test2,bt_test3,bt_test4;

    private EditText send_edit;

    public String pickdate_month,pickdate_day;
    //测试
    String[] testdate;
    int[] testscore;//图表的数据点




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
        bt_test3 = (Button)findViewById(R.id.bt_test3);
        bt_test4 = (Button)findViewById(R.id.bt_test4);
        bt_checkdata.setOnClickListener(this);
        bt_DelTable.setOnClickListener(this);
        bt_test.setOnClickListener(this);
        bt_test2.setOnClickListener(this);
        bt_test3.setOnClickListener(this);
        bt_test4.setOnClickListener(this);
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

            case R.id.bt_test3:
                Intent PieChartiIntent = new Intent(MainActivity.this,PieChartActivity.class);
                startActivity(PieChartiIntent);
                break;

            case R.id.bt_test2:
                for (int i = 1; i < 12; i++) {
                    for (int j =1;j<10;j++ ){
                    EnergyUsed energyUsed = new EnergyUsed();
                   energyUsed.setDate(i+"月"+j+"日");
                   energyUsed.setEnergy_used((float)(1+Math.random()*(100-1+1)));
                   energyUsed.save();}
                }
                break;

            case R.id.bt_test4:
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this,this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();


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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        pickdate_month = String.valueOf(month+1);
        pickdate_day = String.valueOf(dayOfMonth);
        Intent LineChart_month_Intent = new Intent(MainActivity.this,LineChartActivity.class);
        LineChart_month_Intent.putExtra("month_choose",pickdate_month);
        startActivity(LineChart_month_Intent);


    }
}
