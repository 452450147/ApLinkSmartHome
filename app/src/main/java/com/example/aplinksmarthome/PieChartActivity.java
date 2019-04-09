package com.example.aplinksmarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartActivity extends AppCompatActivity {

    private PieChartView pieChart;
    private PieChartData piedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        pieChart = (PieChartView)findViewById(R.id.piechart);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        Initchart();
        InitData();
    }
    private void Initchart(){
        pieChart.setViewportCalculationEnabled(true);//设置饼图自动适应大小
        pieChart.setChartRotation(360,true);//设置饼图旋转角度，且是否为动画
        pieChart.setChartRotationEnabled(true);//设置饼图是否可以手动旋转
        pieChart.setCircleFillRatio(1);//设置饼图其中的比例
        //pieChart.setCircleOval(RectF orginCircleOval);//设置饼图成椭圆形

    }
    private  void InitData(){
        //初始化数据
        List<EnergyUsed> PieList = LitePal.findAll(EnergyUsed.class);
        List<SliceValue> values = new ArrayList<SliceValue>();
        for(EnergyUsed energyUsed:PieList){
            SliceValue sliceValue = new SliceValue(energyUsed.getEnergy_used(), ChartUtils.pickColor());
            values.add(sliceValue);

        }
        piedata = new PieChartData(values);
        piedata.setHasLabels(true);
        piedata.setHasLabelsOutside(false);
        piedata.setHasCenterCircle(false);
        pieChart.setPieChartData(piedata);
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
