package com.example.aplinksmarthome;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.aplinksmarthome.UI.CardAdapater;
import com.example.aplinksmarthome.UI.CardAdapater2;
import com.example.aplinksmarthome.UI.CardBottom;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {


    public String pickdate_month,pickdate_day;

    private CardBottom[]cardBottoms={
            new CardBottom("WiFi直连",R.drawable.test),
            new CardBottom("MQTT连接",R.drawable.test),
            new CardBottom("测试数据随机生成",R.drawable.test),
            new CardBottom("数据库删除",R.drawable.test),
            new CardBottom("饼图测试",R.drawable.test),
            new CardBottom("月份用电图",R.drawable.test)
    };
    private CardBottom[]cardBottoms2={
            new CardBottom("管理员树形图调试",R.drawable.test),
            new CardBottom("树形图数据随机生成",R.drawable.test),
            new CardBottom("用户设备管理",R.drawable.test),
            new CardBottom("待设定",R.drawable.test),
            new CardBottom("待设定",R.drawable.test),
            new CardBottom("",R.drawable.test)
    };
    private List<CardBottom>cardList = new ArrayList<>();
    private List<CardBottom>cardList2 = new ArrayList<>();
    private CardAdapater adapater;
    private CardAdapater2 adapater2;




    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }};



    private void InitView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        initList();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapater = new CardAdapater(cardList);
        recyclerView.setAdapter(adapater);
        RecyclerView recyclerView2 = (RecyclerView)findViewById(R.id.recycle_view2);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this,1);
        recyclerView2.setLayoutManager(layoutManager2);
        adapater2 = new CardAdapater2(cardList2);
        recyclerView2.setAdapter(adapater2);

    }

    private void initList(){
        cardList.clear();
        for (int i = 0; i<cardBottoms.length;i++){
            cardList.add(cardBottoms[i]);
        }
        cardList2.clear();
        for (int i = 0; i<cardBottoms2.length;i++){
            cardList2.add(cardBottoms2[i]);
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
                Toast.makeText(MainActivity.this,"尚未想好",Toast.LENGTH_SHORT).show();
                break;
            case R.id.backup:
                Toast.makeText(MainActivity.this,"尚未想好",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    /* @Override

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_send:
                String str = send_edit.getText().toString();
                new SendAsyncTask().execute(str);
                tv_send_text.setText(str);
                break;

        }



    }*/





    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        pickdate_month = String.valueOf(month+1);
        pickdate_day = String.valueOf(dayOfMonth);
        Intent LineChart_month_Intent = new Intent(MainActivity.this,LineChartActivity.class);
        LineChart_month_Intent.putExtra("month_choose",pickdate_month);
        startActivity(LineChart_month_Intent);

    }
}
