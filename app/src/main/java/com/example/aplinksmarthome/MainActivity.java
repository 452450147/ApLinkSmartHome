package com.example.aplinksmarthome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.aplinksmarthome.DateBase.DeviceEnergy;
import com.example.aplinksmarthome.UI.CardAdapater;
import com.example.aplinksmarthome.UI.CardBottom;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;


public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private CardBottom[]cardBottoms;
    private List<CardBottom>cardList = new ArrayList<>();
    private CardAdapater adapater;
    private PieChartView pieChart;
    private PieChartData piedata;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCardBottom();
        InitView();
        initPiechart();
    }
    private void initCardBottom(){
        cardBottoms = new CardBottom[]{
                new CardBottom(getResources().getString(R.string.wifi_link),R.drawable.wifi),
                new CardBottom(getResources().getString(R.string.mqtt_link),R.drawable.mqtt),
                new CardBottom(getResources().getString(R.string.user_tree),R.drawable.shebeiguanli3),
                new CardBottom(getResources().getString(R.string.bar_chart),R.drawable.bar_chart2),
                new CardBottom(getResources().getString(R.string.pie_chart),R.drawable.pie_chart_72px),
                new CardBottom(getResources().getString(R.string.line_chart),R.drawable.yongdianjiankong)
        };
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent ManagerActivityIntent = new Intent(MainActivity.this, ManagerActivity.class);
                    startActivity(ManagerActivityIntent);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }};



    private void InitView() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        initList();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapater = new CardAdapater(cardList);
        recyclerView.setAdapter(adapater);
        pieChart = findViewById(R.id.piechart_main);
    }

    private void initList(){
        cardList.clear();
        for (int i = 0; i<cardBottoms.length;i++){
            cardList.add(cardBottoms[i]);
        }
    }

    private void initPiechart(){
        pieChart.setValueSelectionEnabled(true);
        pieChart.setOnValueTouchListener(new ValueTouchListener());
        pieChart.setViewportCalculationEnabled(true);//设置饼图自动适应大小
        pieChart.setChartRotation(360,true);//设置饼图旋转角度，且是否为动画
        pieChart.setChartRotationEnabled(true);//设置饼图是否可以手动旋转
        pieChart.setCircleFillRatio(1);//设置饼图其中的比例
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String month = String.valueOf((calendar.get(Calendar.MONTH)) + 1);
        if (Integer.parseInt(month) < 10 ){month = "0" + month;}
        String day = String.valueOf((calendar.get(Calendar.DAY_OF_MONTH)));
        if (Integer.parseInt(day) < 10 ){day = "0" + day;}
        String yesterday;
        if ((calendar.get(Calendar.DAY_OF_MONTH)-1) < 10){
            yesterday = "0" +  String.valueOf((calendar.get(Calendar.DAY_OF_MONTH))-1);
        }
        else yesterday = String.valueOf((calendar.get(Calendar.DAY_OF_MONTH))-1);

        List<SliceValue> values = new ArrayList<SliceValue>();
        String[]month_today_yesterday={"本月用电","昨日用电","今天用电"};
        Float[]energy_value={0f,0f,0f};
        List<DeviceEnergy>PieList = LitePal.where("date like ? and Device_name > ?","19" + month + "%",String.valueOf(4)).find(DeviceEnergy.class);
        for (DeviceEnergy deviceEnergy:PieList){
            energy_value[0] += deviceEnergy.getEnergy_used();
        }
        List<DeviceEnergy>PieList2 = LitePal.where("date = ? and Device_name > ?","19" + month + day,String.valueOf(4)).find(DeviceEnergy.class);
        for (DeviceEnergy deviceEnergy2:PieList2){
            energy_value[1] += deviceEnergy2.getEnergy_used();
        }
        List<DeviceEnergy>PieList3 = LitePal.where("date = ? and Device_name > ?","19" + month + yesterday,String.valueOf(4)).find(DeviceEnergy.class);
        for (DeviceEnergy deviceEnergy3:PieList3){
            energy_value[2] += deviceEnergy3.getEnergy_used();
        }
        for (int i  = 0;i < month_today_yesterday.length ; i++){
            SliceValue sliceValue = new SliceValue(energy_value[i], ChartUtils.pickColor());
            sliceValue.setLabel(month_today_yesterday[i]);
            values.add(sliceValue);

        }
        piedata = new PieChartData(values);
        piedata.setHasLabels(true);
        piedata.setHasLabelsOutside(true);
        piedata.setHasCenterCircle(true);
        //piedata.setCenterCircleColor(Color.WHITE);
        piedata.setCenterCircleScale((float)0.7);
        pieChart.setPieChartData(piedata);
        pieChart.setCircleFillRatio((float)0.7);


    }
    public class ValueTouchListener implements PieChartOnValueSelectListener {
        @Override //如果设置为能选中，选中时走此方法，如果设置为不能选中，点击时走此方法
        public void onValueSelected(int i, SliceValue value) {
            Toast.makeText(MainActivity.this,  "用电量: " + (int) value.getValue() + " kwh", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle("选择语言");
                final String[] Language_dialog = { "中文", "英语"};
                builder.setItems(Language_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Resources resources = getResources();
                            DisplayMetrics metrics = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
                            Configuration config = resources.getConfiguration();// 获得配置对象
                            //区别17版本（其实在17以上版本通过 config.locale设置也是有效的，不知道为什么还要区别）
                            // 在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                config.setLocale(Locale.CHINESE);//设置中文

                            } else {
                                config.locale = Locale.CHINESE;//设置中文

                            }
                            resources.updateConfiguration(config, metrics);
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            }
                        else if (which == 1){
                            Resources resources = getResources();
                            DisplayMetrics metrics = resources.getDisplayMetrics();
                            Configuration config = resources.getConfiguration();// 获得配置对象
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                config.setLocale(Locale.ENGLISH);//设置英文
                            } else {
                                config.locale = Locale.ENGLISH;//设置英文
                            }
                            resources.updateConfiguration(config, metrics);
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }
                });
                builder.show();
                break;
            case R.id.backup:
                finish();
                break;
        }
        return true;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
