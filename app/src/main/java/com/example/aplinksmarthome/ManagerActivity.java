package com.example.aplinksmarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aplinksmarthome.UI.CardAdapater2;
import com.example.aplinksmarthome.UI.CardBottom;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    private CardBottom[]cardBottoms={
            new CardBottom("WiFi直连", R.drawable.test),
            new CardBottom("MQTT连接",R.drawable.test),
            new CardBottom("测试数据随机生成",R.drawable.test),
            new CardBottom("数据库删除",R.drawable.test),
            new CardBottom("饼图测试",R.drawable.test),
            new CardBottom("月份用电图",R.drawable.test)
    };
    private List<CardBottom> cardList = new ArrayList<>();
    private CardAdapater2 adapater;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        InitView();
    }
    private void InitView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        initList();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view3);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapater = new CardAdapater2(cardList);
        recyclerView.setAdapter(adapater);

    }
    private void initList(){
        cardList.clear();
        for (int i = 0; i<cardBottoms.length;i++){
            cardList.add(cardBottoms[i]);
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent MainActivityIntent = new Intent(ManagerActivity.this, MainActivity.class);
                    startActivity(MainActivityIntent);
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }};

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
}
