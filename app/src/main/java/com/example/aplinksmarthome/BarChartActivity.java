package com.example.aplinksmarthome;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aplinksmarthome.DataBase.DeviceEnergy;
import com.example.aplinksmarthome.UI.DatePicker_ForMonth;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class BarChartActivity extends AppCompatActivity {
    private ColumnChartView columnChart;
    private ColumnChartData columnData;
    private String monthchoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        getDateChoose();
        InitData();
        Initchart();


    }
    private void InitView(){
        setContentView(R.layout.bar_chart);
        columnChart = findViewById(R.id.columnchart);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        DatePicker_ForMonth datePicker_forMonth = findViewById(R.id.datepicker_bar);
        datePicker_forMonth.setOnDateSelectedListener(new DatePicker_ForMonth.OnDateSelectedListener(){

            @Override
            public void onDateSelected(int year, int month, int day) {
                if (month < 10){monthchoose = "0" + month;}
                else monthchoose = String.valueOf(month);
                InitData();
                Initchart();
            }
        });
        setSupportActionBar(toolbar1);
    }
    private void getDateChoose(){
        Intent intent = getIntent();
        monthchoose = intent.getStringExtra("month_choose");
        if (Integer.parseInt(monthchoose) < 10){monthchoose = "0" + monthchoose;}
    }


    private void  Initchart(){
        columnChart.setZoomEnabled(true);//设置是否支持缩放
        Viewport v = new Viewport(columnChart.getMaximumViewport());
        v.left = 0;
        v.right = 4;
        columnChart.setCurrentViewport(v);
    }
    private void InitData(){
        List<Column> columnList = new ArrayList<Column>();
        List<SubcolumnValue> subcolumnValueList;

        Axis axisY = new Axis().setHasLines(true);// Y轴属性
        Axis axisX = new Axis();// X轴属性
        axisY.setName("用电量");//设置Y轴显示名称
        axisX.setName("月份");//设置X轴显示名称
        ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
        ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
        axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
        axisX.setLineColor(Color.YELLOW);// 设置X轴轴线颜色
        axisY.setLineColor(Color.YELLOW);// 设置Y轴轴线颜色
        axisX.setTextColor(Color.GREEN);// 设置X轴文字颜色
        axisY.setTextColor(Color.YELLOW);// 设置Y轴文字颜色
        axisX.setTextSize(14);// 设置X轴文字大小
        axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
        axisX.setHasTiltedLabels(true);// 设置X轴文字向左旋转45度
        axisX.setHasLines(true);// 是否显示X轴网格线
        axisY.setHasLines(true);// 是否显示Y轴网格线
        axisX.setInside(false);// 设置X轴文字是否在X轴内部


        String device_names[] = {"照明","制冷制暖","娱乐","厨卫","其它"};
        int a_value[]={3,5,9,11,14,15};

        for (int i = 1 ; i <= 30 ; i++){
            String str;
            Float[]device_energy_value={0f,0f,0f,0f,0f};
            subcolumnValueList = new ArrayList<>();
            if (i < 10){str = "0" + i;}
            else str = String.valueOf(i);
            for (int j  = 0;j < device_names.length ; j++){
                int a =a_value[j];
                int b =a_value[j+1]-1;
                List<DeviceEnergy> DataList = LitePal.where("date = ? and Device_name between ? and ?","19" + monthchoose+ str,String.valueOf(a),String.valueOf(b)).order("time").find(DeviceEnergy.class);
            for (DeviceEnergy deviceEnergy:DataList){
                device_energy_value[j] += deviceEnergy.getEnergy_used();
            }
            SubcolumnValue subcolumnValue = new SubcolumnValue(device_energy_value[j],ChartUtils.pickColor());
            subcolumnValue.setLabel(device_names[j]);
            subcolumnValueList.add(subcolumnValue);

            }Column column = new Column(subcolumnValueList);
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
            column.setFormatter(chartValueFormatter);
            axisValuesX.add(new AxisValue(i-1).setLabel( i + "日"));
            column.setHasLabels(true);
            columnList.add(column);
    }



        columnData = new ColumnChartData(columnList);
        columnData.setAxisXBottom(new Axis(axisValuesX).setHasLines(true)
                .setTextColor(Color.BLACK));
        columnData.setAxisYLeft(new Axis().setHasLines(true)
                .setTextColor(Color.BLACK).setMaxLabelChars(2));
        columnChart.setColumnChartData(columnData);
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
                    Intent ManagerActivityIntent = new Intent(BarChartActivity.this, ManagerActivity.class);
                    startActivity(ManagerActivityIntent);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }};


}
