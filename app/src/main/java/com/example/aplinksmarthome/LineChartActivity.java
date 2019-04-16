package com.example.aplinksmarthome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends Activity {

    LineChartView lineChartView;
    String monthchoose,daychoose;
    int Flag = 1;//1时表示月份，0时表示日用电


    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);
        DatePicker_ForMonth datePicker_forMonth = findViewById(R.id.datepicker_line);
        datePicker_forMonth.setOnDateSelectedListener(new DatePicker_ForMonth.OnDateSelectedListener(){
            @Override
            public void onDateSelected(int year, int month, int day) {
                if (month < 10){monthchoose = "0" + month;}
                else monthchoose = String.valueOf(month);
                mAxisXValues.clear();
                mPointValues.clear();
                getValue();
                InitLineChartView();
            }
        });
        lineChartView = findViewById(R.id.linechart);
        Intent intent = getIntent();
        monthchoose = intent.getStringExtra("month_choose");
        if (Integer.parseInt(monthchoose) < 10){monthchoose = "0" + monthchoose;}
        getValue();
        InitLineChartView();//初始化
    }



    private void InitLineChartView(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        final Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName(monthchoose+"月用电情况");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(20); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 2);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
        axisX.setHasLines(true);// 是否显示X轴网格线
        axisY.setHasLines(true);// 是否显示Y轴网格线
        /**
         *
         */
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;
        v.right = 15;
        lineChartView.setCurrentViewport(v);
        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                if (Flag == 1) {
                    if (pointIndex < 10) {
                        daychoose = "0" + (pointIndex + 1);
                    } else daychoose = String.valueOf(pointIndex + 1);
                    mAxisXValues.clear();
                    mPointValues.clear();
                    getValue_day();
                    InitLineChartView();
                    axisX.setName(monthchoose + "月" + daychoose + "日");
                    Flag = 0;
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    private void getValue() {
        for (int i = 1 ; i <= 30 ; i++){
            String str;
            Float energy = 0f;
            if (i < 10){str = "0" + i;}
            else str = String.valueOf(i);
            List<DeviceEnergy> DataList = LitePal.where("date = ? ","19" + monthchoose+ str).order("time").find(DeviceEnergy.class);
        for(DeviceEnergy deviceEnergy:DataList){
            energy += deviceEnergy.getEnergy_used();
        }            mAxisXValues.add(new AxisValue(i).setLabel(i + "日"));
            mPointValues.add(new PointValue(i,energy));
    }
    }
    private void getValue_day(){
        Float energy = 0f;
        for (int i = 0 ; i <=23 ; i++){
            List<DeviceEnergy> DataList = LitePal.where("date = ? and time like ?","19" + monthchoose+ daychoose,i+"%").order("time").find(DeviceEnergy.class);
            for(DeviceEnergy deviceEnergy:DataList){
                energy = deviceEnergy.getEnergy_used();
            }
            mAxisXValues.add(new AxisValue(i).setLabel(i + ":00"));
            mPointValues.add(new PointValue(i,energy));
        }

    }




}
