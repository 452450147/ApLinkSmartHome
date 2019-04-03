package com.example.aplinksmarthome.tree;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplinksmarthome.R;

import java.io.Serializable;

/**
 * Created by owant on 21/03/2017.
 */

public class EditMapActivity extends BaseActivity implements EditMapContract.View {
    private final static String TAG = "EditMapActivity";
    private final static String tree_model = "tree_model";
    private EditMapContract.Presenter mEditMapPresenter;
    private TreeView editMapTreeView;

/*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_think_map);
        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view);
        int dx = DensityUtils.dp2px(getApplicationContext(), 20);
        int dy = DensityUtils.dp2px(getApplicationContext(), 20);
        int screenHeight = DensityUtils.dp2px(getApplicationContext(), 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, screenHeight));
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar1);
        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {

            }
        });

        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {

            }
        });

        initPresenter();
    }
*/
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
        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view);
        int dx = DensityUtils.dp2px(getApplicationContext(), 20);
        int dy = DensityUtils.dp2px(getApplicationContext(), 20);
        int screenHeight = DensityUtils.dp2px(getApplicationContext(), 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, screenHeight));

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                mEditMapPresenter.editNote();
            }
        });

        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {

            }
        });
        Log.d("testtest","111");
        initPresenter();
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
        Log.d("testtest","222");
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
        return "第一个节点";
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

}

