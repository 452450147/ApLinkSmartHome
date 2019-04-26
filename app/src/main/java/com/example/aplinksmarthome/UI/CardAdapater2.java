package com.example.aplinksmarthome.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplinksmarthome.DataBase.DeviceEnergy;
import com.example.aplinksmarthome.DataBase.DeviceManager;
import com.example.aplinksmarthome.DataBase.EnergyUsed;
import com.example.aplinksmarthome.MqttActivity;
import com.example.aplinksmarthome.R;
import com.example.aplinksmarthome.EditMapActivity;

import org.litepal.LitePal;

import java.util.List;

public class CardAdapater2 extends RecyclerView.Adapter<CardAdapater2.ViewHolder>  {

    private Context mContext;
    private List<CardBottom>mCardList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_item2,viewGroup,false);
        view.getBackground().setAlpha(0);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                CardBottom cardBottom = mCardList.get(position);
                if (mContext.getResources().getString(R.string.manager_tree).equals(cardBottom.getName())) {
                    try {
                        Intent TreeMAPIntent = new Intent(v.getContext(), EditMapActivity.class);
                        TreeMAPIntent.setClass(v.getContext(), EditMapActivity.class);
                        v.getContext().startActivity(TreeMAPIntent);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), "出错了", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }  else if (mContext.getResources().getString(R.string.testdate_creat).equals(cardBottom.getName())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setTitle("选择生成的月份");
                    final String[] month_dialog = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
                    builder.setItems(month_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int d = 1; d <= 30; d++) {
                                for (int i = 5; i <= 7; i++) {
                                    for (int j = 0; j <= 23; j++) {
                                        DeviceEnergy deviceEnergy = new DeviceEnergy();
                                        deviceEnergy.setDevice_name(i);
                                        String d_str, m_str;
                                        if (d < 10) {
                                            d_str = "0" + d;
                                        } else d_str = String.valueOf(d);
                                        int m = which + 1;
                                        if (m < 10) {
                                            m_str = "0" + m;
                                        } else m_str = String.valueOf(m);
                                        deviceEnergy.setDate("19" + m_str + d_str);
                                        deviceEnergy.setLayer(i);
                                        deviceEnergy.setThis_layer_id(i);
                                        deviceEnergy.setTime(j * 100 + "");
                                        deviceEnergy.setEnergy_used((float) (2 + Math.random() * (9 - 2 + 1)));
                                        deviceEnergy.save();

                                    }
                                }
                            }
                            Toast.makeText(mContext, month_dialog[which] + "数据生成成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                }
                else if (mContext.getResources().getString(R.string.mqtt_link).equals(cardBottom.getName())){
                    try{
                        Intent MqttIntent = new Intent(v.getContext(), MqttActivity.class);
                        MqttIntent.setClass(v.getContext(),MqttActivity.class);
                        v.getContext().startActivity(MqttIntent);}
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if (mContext.getResources().getString(R.string.testdevice_creat).equals(cardBottom.getName())){
                    for (int i = 1; i <= 7; i++) {
                        for (int j =1;j <= 2*i; j++ ){
                            DeviceManager deviceManager = new DeviceManager();
                            deviceManager.setDevice_name(i);
                            deviceManager.setLayer(i);
                            deviceManager.setThis_layer_id(j);
                            deviceManager.setUpper_layer_id((int)(1+Math.random()*(2*(i-1)-1+1)));
                            if (i <= 3 || i == 5 ||i == 6){
                                deviceManager.setSwitch_status(true);}
                            else deviceManager.setSwitch_status(false);
                            deviceManager.save();}
                    }
                    Toast.makeText(v.getContext(), "随机数据生成成功", Toast.LENGTH_LONG).show();
                }
                else if (mContext.getResources().getString(R.string.testdevice_creat).equals(cardBottom.getName())){
                    LitePal.deleteAll(EnergyUsed.class);
                    LitePal.deleteAll(DeviceManager.class);
                    LitePal.deleteAll(DeviceEnergy.class);
                    Toast.makeText(v.getContext(), "数据库已删除", Toast.LENGTH_LONG).show();
                }
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardBottom cardBottom = mCardList.get(i);
        viewHolder.cardName.setText(cardBottom.getName());
        Glide.with(mContext).load(cardBottom.getImageId()).into(viewHolder.cardImage);
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView cardImage;
        TextView cardName;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            cardImage = view.findViewById(R.id.cardview2_image);
            cardName = view.findViewById(R.id.cardview2_text);
        }
    }
    public CardAdapater2(List<CardBottom>CardList){
        mCardList = CardList;
    }

}
