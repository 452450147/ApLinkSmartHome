package com.example.aplinksmarthome;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplinksmarthome.DataBase.DeviceEnergy;
import com.ycuwq.datepicker.date.DatePicker;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.PieChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimplePieChartValueFormatter;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartActivity extends AppCompatActivity {

    private PieChartView pieChart;
    private PieChartData piedata;
    private String month_choose,day_choose,datechoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        getDateChoose();
        InitView();
        Initchart();
        InitData();
    }
    private void getDateChoose(){
        Intent intent = getIntent();
        month_choose = intent.getStringExtra("month_choose");
        day_choose = intent.getStringExtra("day_choose");
        if (Integer.parseInt(month_choose) < 10){month_choose = "0"+ month_choose;}
        if (Integer.parseInt(day_choose) < 10){day_choose = "0"+ day_choose;}
        datechoose = "19" + month_choose + day_choose;
    }
    private void InitView(){
        pieChart = findViewById(R.id.piechart);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        DatePicker datePicker = findViewById(R.id.piechart_datePicker);
        Button button = findViewById(R.id.bt_piechart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(int year, int month, int day) {
                        Toast.makeText(getApplicationContext(), year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                    }
                });
                datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
            }
        });
        datePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                String day_select,month_select;
                if (month < 10){month_select = "0" + month;}
                else month_select = String.valueOf(month);
                if (day < 10){day_select = "0" + day;}
                else day_select = String.valueOf(day);
                datechoose = "19" + month_select +day_select;
                InitData();
            }
        });
    }
    private void Initchart(){
        pieChart.setValueSelectionEnabled(true);
        pieChart.setOnValueTouchListener(new ValueTouchListener());
        pieChart.setViewportCalculationEnabled(true);//设置饼图自动适应大小
        pieChart.setChartRotation(360,true);//设置饼图旋转角度，且是否为动画
        pieChart.setChartRotationEnabled(true);//设置饼图是否可以手动旋转
        pieChart.setCircleFillRatio(1);//设置饼图其中的比例




    }
    private  void InitData(){
        List<SliceValue> values = new ArrayList<SliceValue>();
        Float[]device_energy_value={0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};
        String[]device_name_value={"日光灯","台灯","空调","风扇","冰箱","暖气","电视","电脑","洗衣机","电磁炉","微波炉","其它"};
        for (int i  = 0;i < device_name_value.length ; i++){
            List<DeviceEnergy>PieList = LitePal.where("date = ? and Device_name = ?",datechoose,String.valueOf(i+3)).find(DeviceEnergy.class);
            for (DeviceEnergy deviceEnergy:PieList){
                device_energy_value[i] += deviceEnergy.getEnergy_used();
            }
            SliceValue sliceValue = new SliceValue(device_energy_value[i], ChartUtils.pickColor());

            sliceValue.setLabel(device_name_value[i]);

            values.add(sliceValue);
        }


        piedata = new PieChartData(values);
       // PieChartValueFormatter pieChartValueFormatter = new SimplePieChartValueFormatter(2);
       // piedata.setFormatter( pieChartValueFormatter);
        piedata.setHasLabels(true);
        piedata.setHasLabelsOutside(false);
        piedata.setHasCenterCircle(true);
        piedata.setCenterCircleColor(Color.WHITE);
        piedata.setCenterCircleScale((float)0.7);
        pieChart.setPieChartData(piedata);
        pieChart.setCircleFillRatio((float)0.7);

    }



    public class ValueTouchListener implements PieChartOnValueSelectListener {
        @Override //如果设置为能选中，选中时走此方法，如果设置为不能选中，点击时走此方法
        public void onValueSelected(int i, SliceValue value) {
            Toast.makeText(PieChartActivity.this, "该设备" + "用电量: " + String.format("%.2f",value.getValue()) + " kwh", Toast.LENGTH_SHORT).show();
        }
        @Override //能选中，取消选中时调用；  不能选中： 不走此方法
        public void onValueDeselected() {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
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
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent ManagerActivityIntent = new Intent(PieChartActivity.this, ManagerActivity.class);
                    startActivity(ManagerActivityIntent);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }};



}
