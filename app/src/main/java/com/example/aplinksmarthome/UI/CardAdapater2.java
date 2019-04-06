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
import com.example.aplinksmarthome.PieChartActivity;
import com.example.aplinksmarthome.R;
import com.example.aplinksmarthome.WifiUseActivity;
import com.example.aplinksmarthome.tree.EditMapActivity;
import com.example.aplinksmarthome.tree.EditmapActivity_user;

import org.litepal.LitePal;

import java.util.Calendar;
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
                    case "管理员树形图调试":try{
                        Intent TreeMAPIntent = new Intent(v.getContext(), EditMapActivity.class);
                        TreeMAPIntent.setClass(v.getContext(),EditMapActivity.class);
                        v.getContext().startActivity(TreeMAPIntent);}
                    catch (Exception e){
                        Toast.makeText(v.getContext(),"出错了",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                        break;
                    case "树形图数据随机生成":
                        Toast.makeText(v.getContext(), "随机数据生成成功", Toast.LENGTH_LONG).show();
                        for (int i = 1; i <= 7; i++) {
                            for (int j =1;j <= 2*i; j++ ){
                                DeviceManager deviceManager = new DeviceManager();
                                deviceManager.setDevice_name(i);
                                deviceManager.setLayer(i);
                                deviceManager.setThis_layer_id(j);
                                deviceManager.setUpper_layer_id((int)(1+Math.random()*(2*(i-1)-1+1)));
                                if (i <= 3 || i == 5){
                                deviceManager.setSwitch_status(true);}
                                else deviceManager.setSwitch_status(false);
                                deviceManager.save();}
                        }
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
                    case "用户设备管理":try{
                        Intent TreeMAPIntent2 = new Intent(v.getContext(), EditmapActivity_user.class);
                        TreeMAPIntent2.setClass(v.getContext(),EditmapActivity_user.class);
                        v.getContext().startActivity(TreeMAPIntent2);}
                    catch (Exception e){
                        Toast.makeText(v.getContext(),"出错了",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                        break;
                    case "饼图测试":try{
                        Intent PieChartiIntent = new Intent(v.getContext(), PieChartActivity.class);
                        PieChartiIntent.setClass(v.getContext(),PieChartActivity.class);
                        v.getContext().startActivity(PieChartiIntent);}
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
            cardImage = (ImageView)view.findViewById(R.id.cardview2_image);
            cardName = (TextView)view.findViewById(R.id.cardview2_text);
        }
    }
    public CardAdapater2(List<CardBottom>CardList){
        mCardList = CardList;
    }

}
