package com.example.aplinksmarthome.tree;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplinksmarthome.DeviceManager;
import com.example.aplinksmarthome.IGetMessageCallBack;
import com.example.aplinksmarthome.MQTTService;
import com.example.aplinksmarthome.MqttActivity;
import com.example.aplinksmarthome.MyServiceConnection;
import com.example.aplinksmarthome.R;
import com.example.aplinksmarthome.SendAsyncTask;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by owant on 21/03/2017.
 */

public class EditmapActivity_user extends BaseActivity implements EditMapContract.View,View.OnClickListener, IGetMessageCallBack {
    private final static String TAG = "EditMapActivity";
    private final static String tree_model = "tree_model";
    private String plan_str = "点击此处开始控制";
    private EditMapContract.Presenter mEditMapPresenter;



    private TreeView editMapTreeView;

    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    private boolean OnceFlag = true,OnceFlag2 = true;
    private String topic_send = "ForTest";

    @Override
    protected void onBaseIntent() { }

    @Override
    protected void onBasePreLayout() { }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_edit_think_map2;
    }

    @Override
    protected void onBaseBindView() {
        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view2);
        int dx = DensityUtils.dp2px(getApplicationContext(), 20);
        int dy = DensityUtils.dp2px(getApplicationContext(), 20);
        int screenHeight = DensityUtils.dp2px(getApplicationContext(), 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, screenHeight));
        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {

            }
        });

        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {
                editMapTreeView.changeNodeSwich(getCurrentFocusNode());
                String str = editMapTreeView.returnStr(getCurrentFocusNode());
                new SendAsyncTask().execute(str);
                MQTTService.publish(str);
            }
        });

        initPresenter();
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(EditmapActivity_user.this);

        Intent intent = new Intent(this, MQTTService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onLoadData() {

    }
    //------分割线
    private void initPresenter() {
        //presenter层关联的View
        mEditMapPresenter = new EditMapPresenter(this);
        mEditMapPresenter.start();
        mEditMapPresenter.createDefaultTreeModel();
        adddata();
    }

    public void adddata(){
        List<DeviceManager> DataList_layer1 = LitePal.where("layer = ?","5").order("this_layer_id").find(DeviceManager.class);
        for(DeviceManager deviceManager:DataList_layer1){
            String str_1 = GetDeviceName(deviceManager.getDevice_name()) + deviceManager.getThis_layer_id();
            editMapTreeView.creatSubNode_root(str_1,deviceManager.getSwitch_status(),deviceManager.getLayer(),deviceManager.getThis_layer_id());
            String this_id = String.valueOf(deviceManager.getThis_layer_id());
            List<DeviceManager> DataList_layer2 = LitePal.where("layer = ? and upper_layer_id = ?","6",this_id).order("this_layer_id").find(DeviceManager.class);
            String size = String.valueOf(DataList_layer2.size());
            if (DataList_layer2 !=null){
                for (DeviceManager deviceManager2:DataList_layer2){
                    boolean switch_judge;
                    if (deviceManager.getSwitch_status()){switch_judge = deviceManager2.getSwitch_status();}else switch_judge = false;
                    if (OnceFlag == false){String str_2 = GetDeviceName(deviceManager2.getDevice_name()) + deviceManager2.getThis_layer_id();
                    editMapTreeView.creatNode(str_2,switch_judge,deviceManager2.getLayer(),deviceManager2.getThis_layer_id());}
                    if (OnceFlag == true){String str_2 = GetDeviceName(deviceManager2.getDevice_name()) + deviceManager2.getThis_layer_id();
                        editMapTreeView.creatSubNode(str_2,switch_judge,deviceManager2.getLayer(),deviceManager2.getThis_layer_id());OnceFlag = false;}
                    String this_id2 = String.valueOf(deviceManager2.getThis_layer_id());
                    List<DeviceManager> DataList_layer3 = LitePal.where("layer = ? and upper_layer_id = ?","7",this_id2).order("this_layer_id").find(DeviceManager.class);
                    if (DataList_layer3 !=null){ editMapTreeView.noteParentNode();
                        for (DeviceManager deviceManager3:DataList_layer3){boolean switch_judge2;
                            if (deviceManager2.getSwitch_status()){switch_judge2 = deviceManager3.getSwitch_status();}else switch_judge2 = false;
                            if (OnceFlag2 == false){String str_3 = GetDeviceName(deviceManager3.getDevice_name()) + deviceManager3.getThis_layer_id();
                                editMapTreeView.creatNode(str_3,switch_judge2,deviceManager3.getLayer(),deviceManager3.getThis_layer_id());}
                            if (OnceFlag2 == true){String str_3 = GetDeviceName(deviceManager3.getDevice_name()) + deviceManager3.getThis_layer_id();
                                editMapTreeView.creatSubNode(str_3,switch_judge2,deviceManager3.getLayer(),deviceManager3.getThis_layer_id());OnceFlag2 = false;}

                        }
                        editMapTreeView.returnParentNode();
                        OnceFlag2 = true;
                    }
                }
                OnceFlag = true;
            }

        }

    }
    private String GetDeviceName(int i){
        String str = null;
        switch (i){
            case 5:
                str = "电接口";
                break;
            case 2:
                str = "冰箱";
                break;
            case 3:
                str = "洗衣机";
                break;
            case 4:
                str = "电视";
                break;
            case 1:
                str = "灯泡";
                break;
            case 6:
                str = "插座";
                break;
            case 7:
                str = "风扇";
                break;
        }
        return str;
    }

    @Override
    public void setPresenter(EditMapContract.Presenter presenter) {
        if (mEditMapPresenter == null) {
            mEditMapPresenter = presenter;
        }
    }



    @Override
    public void showLoadingFile() {

    }

    @Override
    public void hideLoadingFile() {

    }

    @Override
    public void showAddNoteDialog() {

    }

    @Override
    public void showAddSubNoteDialog() {

    }

    @Override
    public void showEditNoteDialog() {

    }

    @Override
    public void showSaveFileDialog(String fileName) {

    }

    @Override
    public void focusingMid() {

        editMapTreeView.focusMidLocation();
    }

    @Override
    public String getDefaultPlanStr() {
        return plan_str;
    }

    @Override
    public NodeModel<String> getCurrentFocusNode() {

        return editMapTreeView.getCurrentFocusNode();
    }

    @Override
    public String getDefaultSaveFilePath() {
        return null;
    }

    @Override
    public String getAppVersion() {
        return AndroidUtil.getAppVersion(getApplicationContext());
    }

    @Override
    public void finishActivity() {
        unbindService(serviceConnection);
        EditmapActivity_user.this.finish();
    }


    @Override
    public void setTreeViewData(TreeModel treeModel) {
        editMapTreeView.setTreeModel(treeModel);
    }
    @Override

    public void onClick(View v) {

        switch (v.getId()) {
        }
    }
    @Override
    public void setMessage(String message) {

        String xieyi = message;

        char fir = xieyi.charAt(0);

        char sec = xieyi.charAt(1);

        char switch_status_char = xieyi.charAt(6);

        boolean switch_status = false;

        if (switch_status_char == '1'){switch_status = true;}
        String layer = xieyi.substring(2,4);

        String device_id = xieyi.substring(4,6);

        if (fir == '#')

            switch (sec){

                case 'R':

                    Toast.makeText(EditmapActivity_user.this, "设备情况已更改", Toast.LENGTH_SHORT).show();

                    DeviceManager deviceManager = new DeviceManager();

                    if (switch_status == true){deviceManager.setSwitch_status(switch_status);}
                    if (switch_status == false){deviceManager.setToDefault("switch_status");}

                    deviceManager.updateAll("layer = ? and this_layer_id = ?",layer,device_id);
                    break;

                default:Toast.makeText(EditmapActivity_user.this, "未知数据", Toast.LENGTH_LONG).show();

                    break;


            }

        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }



}

