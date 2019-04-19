package com.example.aplinksmarthome.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplinksmarthome.BarChartActivity;
import com.example.aplinksmarthome.LineChartActivity;
import com.example.aplinksmarthome.MqttActivity;
import com.example.aplinksmarthome.PieChartActivity;
import com.example.aplinksmarthome.R;
import com.example.aplinksmarthome.WifiUseActivity;
import com.example.aplinksmarthome.EditmapActivity_user;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
        view.getBackground().setAlpha(0);
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
                if (mContext.getResources().getString(R.string.wifi_link).equals(cardBottom.getName())) {
                    try {
                        Intent WifiuseIntent = new Intent(v.getContext(), WifiUseActivity.class);
                        WifiuseIntent.setClass(v.getContext(), WifiUseActivity.class);
                        v.getContext().startActivity(WifiuseIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (mContext.getResources().getString(R.string.line_chart).equals(cardBottom.getName())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    String month = String.valueOf((calendar.get(Calendar.MONTH)) + 1);
                    Log.d("test", month);
                    try {
                        Intent LineChart_month_Intent = new Intent(v.getContext(), LineChartActivity.class);
                        LineChart_month_Intent.setClass(v.getContext(), LineChartActivity.class);
                        LineChart_month_Intent.putExtra("month_choose", month);
                        v.getContext().startActivity(LineChart_month_Intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (mContext.getResources().getString(R.string.pie_chart).equals(cardBottom.getName())) {
                    Calendar calendar_pie = Calendar.getInstance();
                    DatePickerDialog dialog_pie = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String pickdate_month = String.valueOf(month + 1);
                            String pickdate_day = String.valueOf(dayOfMonth);
                            try {
                                Intent PieChart_month_Intent = new Intent(view.getContext(), PieChartActivity.class);
                                PieChart_month_Intent.setClass(view.getContext(), PieChartActivity.class);
                                PieChart_month_Intent.putExtra("month_choose", pickdate_month);
                                PieChart_month_Intent.putExtra("day_choose", pickdate_day);
                                view.getContext().startActivity(PieChart_month_Intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            calendar_pie.get(Calendar.YEAR),
                            calendar_pie.get(Calendar.MONTH),
                            calendar_pie.get(Calendar.DAY_OF_MONTH));
                    dialog_pie.show();

                } else if (mContext.getResources().getString(R.string.mqtt_link).equals(cardBottom.getName())) {
                    try {
                        Intent MqttIntent = new Intent(v.getContext(), MqttActivity.class);
                        MqttIntent.setClass(v.getContext(), MqttActivity.class);
                        v.getContext().startActivity(MqttIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (mContext.getResources().getString(R.string.user_tree).equals(cardBottom.getName())) {
                    try {
                        Intent TreeMAPIntent2 = new Intent(v.getContext(), EditmapActivity_user.class);
                        TreeMAPIntent2.setClass(v.getContext(), EditmapActivity_user.class);
                        v.getContext().startActivity(TreeMAPIntent2);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), "出错了", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else if (mContext.getResources().getString(R.string.bar_chart).equals(cardBottom.getName())) {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    String month2 = String.valueOf((calendar2.get(Calendar.MONTH)) + 1);
                    try {
                        Intent BarcahrtIntent = new Intent(v.getContext(), BarChartActivity.class);
                        BarcahrtIntent.setClass(v.getContext(), BarChartActivity.class);
                        BarcahrtIntent.putExtra("month_choose", month2);
                        v.getContext().startActivity(BarcahrtIntent);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), "出错了", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

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
