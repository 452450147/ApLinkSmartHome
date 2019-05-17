package com.example.aplinksmarthome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UnusualActivity extends AppCompatActivity {
    String jsonUrl = "http://167.179.101.198/api/v2/mysql/_table/manager_detection?api_key=9ddd5d8beda28c6ae5145a3fd4ae46827f59a8aa27c8a2173667b02b9f8f2ea2";
    public static final int GET_Unusual = 2;
    int yichangcishu=0;
    TextView textView;
    String unusual_user,date_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unusual);
        textView = findViewById(R.id.tv_unusual);
        sendRequestWithOkHttp();
    }

    private  void  sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(jsonUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                    Message message = new Message();
                    message.what = GET_Unusual;
                    handler.sendMessage(message);
                }
                //这里发现了Android P 会不让HTTPS外的连接成功，要修改android:networkSecurityConfig
                catch (IOException e){e.printStackTrace(); Log.d("test",Log.getStackTraceString(e));}
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsondata){
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            String jsonObject_resource = jsonObject.getString("resource");
            JSONArray jsonArray = new JSONArray(jsonObject_resource);
            for (int i = 0; i <jsonArray.length(); i++){
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                int layer = Integer.parseInt(jsonObject2.getString("layer"));
                int layer_id = Integer.parseInt(jsonObject2.getString("layer_id"));
                String date = jsonObject2.getString("date");
                unusual_user = GetDeviceName(layer) + layer_id;
                date_tv= date;
            }

        }catch (Exception e){
            e.printStackTrace();Log.d("test",Log.getStackTraceString(e));
        }
    }
    private String GetDeviceName(int i){
        String str = null;
        switch (i){
            case 1:
                str = getResources().getString(R.string.device_name1);
                break;
            case 2:
                str = getResources().getString(R.string.device_name2);
                break;
            case 3:
                str = getResources().getString(R.string.device_name3);
                break;
            case 4:
                str = getResources().getString(R.string.device_name4);
                break;
            case 5:
                str = getResources().getString(R.string.device_name5);
                break;
        }
        return str;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_Unusual:
                   textView.append("出现异常用电用户:   "+ unusual_user+ "    发现时间:   "+date_tv );
                   yichangcishu++;
                    break;
                default:
                    break;
            }
        }
    };
}
