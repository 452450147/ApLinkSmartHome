package com.example.aplinksmarthome.UI.tree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.aplinksmarthome.R;


/**
 * Created by owant on 09/02/2017.
 */
@SuppressLint("AppCompatCustomView")
public class NodeView extends TextView {

    public NodeModel<String> treeNode = null;
    public boolean swich;
    public int layer,device_id,upper_layer,device_name;

    public NodeView(Context context) {
        this(context, null, 0);
    }

    public NodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTextColor(Color.WHITE);
        setPadding(12, 10, 12, 10);

        Drawable drawable = context.getResources().getDrawable(R.drawable.node_view_bg);
        setBackgroundDrawable(drawable);
    }
    public void setSwitch(boolean swich){
        this.swich = swich;
        if (swich == false){
        setTextColor(Color.RED);}
        if (swich == true){
            setTextColor(Color.GREEN);}
    }
    public boolean getSwitch(){
        return swich;
    }
    public void setLayer(int layer){
        this.layer = layer;

    }
    public int getLayer(){
        return layer;
    }
    public void setUppre_Layer(int upper_layer){
        this.upper_layer = upper_layer;

    }
    public int getUpper_Layer(){
        return upper_layer;
    }
    public void setDevice_name(int device_name){
        this.device_name = device_name;

    }
    public int getDevice_name(){
        return device_name;
    }
    public void setDevice_id(int device_id){
        this.device_id = device_id;

    }
    public int getDevice_id(){
        return device_id;
    }

    public NodeModel<String> getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(NodeModel<String> treeNode) {
        this.treeNode = treeNode;
        setSelected(treeNode.isFocus());
        setText(treeNode.getValue());
    }

}