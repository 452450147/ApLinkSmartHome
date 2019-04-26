package com.example.aplinksmarthome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aplinksmarthome.DataBase.DeviceManager;
import com.example.aplinksmarthome.UI.tree.AndroidUtil;
import com.example.aplinksmarthome.UI.tree.BaseActivity;
import com.example.aplinksmarthome.UI.tree.DensityUtils;
import com.example.aplinksmarthome.UI.tree.EditMapContract;
import com.example.aplinksmarthome.UI.tree.EditMapPresenter;
import com.example.aplinksmarthome.UI.tree.NodeModel;
import com.example.aplinksmarthome.UI.tree.RightTreeLayoutManager;
import com.example.aplinksmarthome.UI.tree.TreeModel;
import com.example.aplinksmarthome.UI.tree.TreeView;
import com.example.aplinksmarthome.UI.tree.TreeViewItemClick;
import com.example.aplinksmarthome.UI.tree.TreeViewItemLongClick;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by owant on 21/03/2017.
 */

public class EditMapActivity extends BaseActivity implements EditMapContract.View {
    private String plan_str = "分层管理图",power = null;
    private EditMapContract.Presenter mEditMapPresenter;
    private TreeView editMapTreeView;
    private boolean OnceFlag = true,OnceFlag2 = true,OnceFlag3 = true;
    String jsonUrl = "http://167.179.101.198/api/v2/mysql/_table/manager_detection?api_key=9ddd5d8beda28c6ae5145a3fd4ae46827f59a8aa27c8a2173667b02b9f8f2ea2";
    public static final int GET_Power = 2;

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_edit_think_map;
    }

    @Override
    protected void onBaseBindView() {
        editMapTreeView = findViewById(R.id.edit_map_tree_view);
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
                int layer = editMapTreeView.returnlayer_int(getCurrentFocusNode());
                int this_layer_id =  editMapTreeView.return_this_layer_id_int(getCurrentFocusNode());
                sendRequestWithOkHttp(layer,this_layer_id);

            }
        });

        initPresenter();
    }

    @Override
    protected void onLoadData() {

    }
    private void initPresenter() {
        //presenter层关联的View
        mEditMapPresenter = new EditMapPresenter(this);
        mEditMapPresenter.start();
            mEditMapPresenter.createDefaultTreeModel();
            adddata();
    }

    public void adddata(){
        List<DeviceManager> DataList_layer1 = LitePal.where("layer = ?","1").order("this_layer_id").find(DeviceManager.class);
        for(DeviceManager deviceManager:DataList_layer1){
            String str_1 = GetDeviceName(deviceManager.getLayer()) + deviceManager.getThis_layer_id();
            editMapTreeView.creatSubNode_root(str_1,deviceManager.getSwitch_status(),deviceManager.getLayer(),deviceManager.getThis_layer_id());
            String this_id = String.valueOf(deviceManager.getThis_layer_id());
            List<DeviceManager> DataList_layer2 = LitePal.where("layer = ? and upper_layer_id = ?","2",this_id).order("this_layer_id").find(DeviceManager.class);
            if (DataList_layer2 !=null){
                for (DeviceManager deviceManager2:DataList_layer2){
                    if (OnceFlag == false){String str_2 = GetDeviceName(deviceManager2.getLayer()) + deviceManager2.getThis_layer_id();
                    editMapTreeView.creatNode(str_2,deviceManager2.getSwitch_status(),deviceManager2.getLayer(),deviceManager2.getThis_layer_id());}
                    if (OnceFlag == true){String str_2 = GetDeviceName(deviceManager2.getLayer()) + deviceManager2.getThis_layer_id();
                        editMapTreeView.creatSubNode(str_2,deviceManager2.getSwitch_status(),deviceManager2.getLayer(),deviceManager2.getThis_layer_id());OnceFlag = false;}
                        String this_id2 = String.valueOf(deviceManager2.getThis_layer_id());
                    List<DeviceManager> DataList_layer3 = LitePal.where("layer = ? and upper_layer_id = ?","3",this_id2).order("this_layer_id").find(DeviceManager.class);
                   if (DataList_layer3 !=null){ editMapTreeView.noteParentNode();
                       for (DeviceManager deviceManager3:DataList_layer3){
                           if (OnceFlag2 == false){String str_3 = GetDeviceName(deviceManager3.getLayer()) + deviceManager3.getThis_layer_id();
                               editMapTreeView.creatNode(str_3,deviceManager3.getSwitch_status(),deviceManager3.getLayer(),deviceManager3.getThis_layer_id());}
                           if (OnceFlag2 == true){String str_3 = GetDeviceName(deviceManager3.getLayer()) + deviceManager3.getThis_layer_id();
                               editMapTreeView.creatSubNode(str_3,deviceManager3.getSwitch_status(),deviceManager3.getLayer(),deviceManager3.getThis_layer_id());OnceFlag2 = false;}
                           String this_id3 = String.valueOf(deviceManager3.getThis_layer_id());
                           List<DeviceManager> DataList_layer4 = LitePal.where("layer = ? and upper_layer_id = ?","4",this_id3).order("this_layer_id").find(DeviceManager.class);
                           if (DataList_layer4 !=null){ editMapTreeView.noteParentNode2();
                               for (DeviceManager deviceManager4:DataList_layer4){
                               if (OnceFlag3 == false){String str_4 = GetDeviceName(deviceManager4.getLayer()) + deviceManager4.getThis_layer_id();
                                   editMapTreeView.creatNode(str_4,deviceManager4.getSwitch_status(),deviceManager4.getLayer(),deviceManager4.getThis_layer_id());}
                               if (OnceFlag3 == true){String str_4 = GetDeviceName(deviceManager4.getLayer()) + deviceManager4.getThis_layer_id();
                                   editMapTreeView.creatSubNode(str_4,deviceManager4.getSwitch_status(),deviceManager4.getLayer(),deviceManager4.getThis_layer_id());OnceFlag3 = false;}
                                 /*    String this_id4 = String.valueOf(deviceManager4.getThis_layer_id());
                                 List<DeviceManager> DataList_layer5 = LitePal.where("layer = ? and upper_layer_id = ?","5",this_id4).order("this_layer_id").find(DeviceManager.class);
                                   if (DataList_layer5 !=null){ editMapTreeView.noteParentNode3();
                                       for (DeviceManager deviceManager5:DataList_layer5){
                                           if (OnceFlag4 == false){String str_5 = GetDeviceName(deviceManager5.getDevice_name()) + deviceManager5.getThis_layer_id();
                                               editMapTreeView.creatNode(str_5,deviceManager5.getSwitch_status());}
                                           if (OnceFlag3 == true){String str_5 = GetDeviceName(deviceManager5.getDevice_name()) + deviceManager5.getThis_layer_id();
                                               editMapTreeView.creatSubNode(str_5,deviceManager5.getSwitch_status());OnceFlag4 = false;}}
                                               editMapTreeView.returnParentNode3();OnceFlag4 = true;
                                   }*/
                           }editMapTreeView.returnParentNode2();
                               OnceFlag3 = true;
                           }
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
        EditMapActivity.this.finish();
    }

    @Override
    public void setTreeViewData(TreeModel treeModel) {
        editMapTreeView.setTreeModel(treeModel);
    }


    private  void  sendRequestWithOkHttp(final int layer , final int this_layer_id){
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
                    parseJSONWithJSONObject(responseData,layer,this_layer_id);
                    Message message = new Message();
                    message.what = GET_Power;
                    handler.sendMessage(message);
                }
                //这里发现了Android P 会不让HTTPS外的连接成功，要修改android:networkSecurityConfig
                catch (IOException e){e.printStackTrace(); Log.d("test",Log.getStackTraceString(e));}
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsondata,int layer ,int this_layer_id){
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            String jsonObject_resource = jsonObject.getString("resource");
            JSONArray jsonArray = new JSONArray(jsonObject_resource);
            for (int i = 0; i <jsonArray.length(); i++){
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String layer_str = jsonObject2.getString("layer");
                String layer_id_str = jsonObject2.getString("layer_id");
                if (layer_str.equals(String.valueOf(layer)) & layer_id_str.equals(String.valueOf(this_layer_id)))
                {  power = jsonObject2.getString("power");}
            }

        }catch (Exception e){
            e.printStackTrace();Log.d("test",Log.getStackTraceString(e));
        }
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_Power:
                    Toast.makeText(EditMapActivity.this,"用电量"+power,Toast.LENGTH_LONG).show();
                    break;
                    default:
                        break;
            }
        }
    };
}

