package com.example.aplinksmarthome.tree;

import android.text.TextUtils;



import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by owant on 21/03/2017.
 */
public class EditMapPresenter implements EditMapContract.Presenter {

    private EditMapContract.View mView;

    private boolean mIsCreate;
    private String mFilePath;
    private String mDefaultFilePath;
    private String mFileName;
    private TreeModel<String> mTreeModel;

    private TreeModel<String> mOldTreeModel;

    private String[] mOwantFilesArray;

    public EditMapPresenter(EditMapContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mIsCreate = true;
        mFileName = mView.getDefaultPlanStr();
        mView.showLoadingFile();
    }

    @Override
    public void onRecycle() {
        mOwantFilesArray = null;
        mTreeModel = null;
    }



    @Override
    public void createDefaultTreeModel() {
        NodeModel<String> plan = new NodeModel<>(mView.getDefaultPlanStr());
        mTreeModel = new TreeModel<>(plan);
        mView.setTreeViewData(mTreeModel);

    }




    @Override
    public void addNote() {
        mView.showAddNoteDialog();
    }

    @Override
    public void addSubNote() {
        mView.showAddSubNoteDialog();
    }

    @Override
    public void editNote() {
        mView.showEditNoteDialog();
    }

    @Override
    public void focusMid() {
        mView.focusingMid();
    }


    private boolean isEqualsOldTreeModel() {
        boolean equals = false;
        TreeModel<String> temp = mTreeModel;
        TreeModel<String> compareTemp = mOldTreeModel;

        StringBuffer tempBuffer = new StringBuffer();
        Stack<NodeModel<String>> stack = new Stack<>();
        NodeModel<String> rootNode = temp.getRootNode();
        stack.add(rootNode);
        while (!stack.isEmpty()) {
            NodeModel<String> pop = stack.pop();
            tempBuffer.append(pop.value);
            LinkedList<NodeModel<String>> childNodes = pop.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                stack.add(item);
            }
        }

        StringBuffer compareTempBuffer = new StringBuffer();
        Stack<NodeModel<String>> stackThis = new Stack<>();
        NodeModel<String> rootNodeThis = compareTemp.getRootNode();
        stackThis.add(rootNodeThis);
        while (!stackThis.isEmpty()) {
            NodeModel<String> pop = stackThis.pop();
            compareTempBuffer.append(pop.value);
            LinkedList<NodeModel<String>> childNodes = pop.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                stackThis.add(item);
            }
        }

        if (compareTempBuffer.toString().equals(tempBuffer.toString())) {
            equals = true;
        }
        return equals;
    }



    @Override
    public void setTreeModel(TreeModel<String> treeModel) {
        mTreeModel = treeModel;
        mView.setTreeViewData(mTreeModel);
    }

    @Override
    public TreeModel<String> getTreeModel() {
        return mTreeModel;
    }




}
