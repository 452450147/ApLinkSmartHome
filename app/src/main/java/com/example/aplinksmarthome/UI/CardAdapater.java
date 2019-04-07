package com.example.aplinksmarthome.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplinksmarthome.DeviceManager;
import com.example.aplinksmarthome.EnergyUsed;
import com.example.aplinksmarthome.LineChartActivity;
import com.example.aplinksmarthome.MainActivity;
import com.example.aplinksmarthome.MqttActivity;
import com.example.aplinksmarthome.PieChartActivity;
import com.example.aplinksmarthome.R;
import com.example.aplinksmarthome.WifiUseActivity;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

public class CardAdapater extends RecyclerView.Adapter<CardAdapater.ViewHolder>  {

    private Context mContext;
    private List<CardBottom>mCardList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                CardBottom cardBottom = mCardList.get(position);
                Toast.makeText(v.getContext(),cardBottom.getName(),Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                CardBottom cardBottom = mCardList.get(position);
                switch (cardBottom.getName()){
                    case "WiFi直连":try{
                        Intent WifiuseIntent = new Intent(v.getContext(), WifiUseActivity.class);
                        WifiuseIntent.setClass(v.getContext(),WifiUseActivity.class);
                        v.getContext().startActivity(WifiuseIntent);}
                        catch (Exception e){
                        e.printStackTrace();
                    }
                        break;
                    case "月份用电图":
                        Calendar calendar=Calendar.getInstance();
                        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String pickdate_month = String.valueOf(month+1);
                           //String pickdate_day = String.valueOf(dayOfMonth);
                                try{
                            Intent LineChart_month_Intent = new Intent(view.getContext(), LineChartActivity.class);
                            LineChart_month_Intent.setClass(view.getContext(),LineChartActivity.class);
                            LineChart_month_Intent.putExtra("month_choose",pickdate_month);
                            view.getContext().startActivity(LineChart_month_Intent);}
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                        }},
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                        dialog.show();
                        break;
                    case "测试数据随机生成":
                        for (int i = 1; i < 12; i++) {
                            for (int j =1;j<10;j++ ){
                                EnergyUsed energyUsed = new EnergyUsed();
                                energyUsed.setDate(i+"月"+j+"日");
                                energyUsed.setEnergy_used((float)(1+Math.random()*(100-1+1)));
                                energyUsed.save();}
                        }
                        break;
                    case "数据库删除":
                        Toast.makeText(v.getContext(), "数据库已删除", Toast.LENGTH_LONG).show();
                        LitePal.deleteAll(EnergyUsed.class);
                        LitePal.deleteAll(DeviceManager.class);
                        break;
                    case "饼图测试":try{
                        Intent PieChartiIntent = new Intent(v.getContext(), PieChartActivity.class);
                        PieChartiIntent.setClass(v.getContext(),PieChartActivity.class);
                        v.getContext().startActivity(PieChartiIntent);}
                        catch (Exception e){
                        e.printStackTrace();
                        }
                        break;
                    case "MQTT连接":try{
                        Intent MqttIntent = new Intent(v.getContext(), MqttActivity.class);
                        MqttIntent.setClass(v.getContext(),MqttActivity.class);
                        v.getContext().startActivity(MqttIntent);}
                    catch (Exception e){
                        e.printStackTrace();
                    }
                        break;

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
            cardImage = (ImageView)view.findViewById(R.id.cardview_image);
            cardName = (TextView)view.findViewById(R.id.cardview_text);
        }
    }
    public CardAdapater(List<CardBottom>CardList){
        mCardList = CardList;
    }

}
