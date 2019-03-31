package com.example.aplinksmarthome;

import android.app.Activity;
import android.os.Bundle;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartActivity extends Activity {

    private PieChartView pieChart;
    private PieChartData piedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        pieChart = (PieChartView)findViewById(R.id.piechart);
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


}
