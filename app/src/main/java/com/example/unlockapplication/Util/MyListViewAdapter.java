package com.example.unlockapplication.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.unlockapplication.R;
import com.example.unlockapplication.entity.ListViewItem;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {

    Context context;
    List<ListViewItem> datas;

    public MyListViewAdapter(Context context, List<ListViewItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ListViewItem listViewItem = (ListViewItem) getItem(i);

        View view = null;
        ViewHolder viewHolder;

        switch (i){
            case 0:
                if (convertView == null){
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_device,null);
                    viewHolder = new ViewHolder();
                    viewHolder.itemName = view.findViewById(R.id.itemName);
                    viewHolder.deviceName = view.findViewById(R.id.deviceName);
                    /*viewHolder.progressBar = view.findViewById(R.id.progressBar);
                    viewHolder.ToggleButton = view.findViewById(R.id.ToggleButton);*/
                    viewHolder.backImg = view.findViewById(R.id.backImg);
                    view.setTag(viewHolder);
                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }
                viewHolder.itemName.setText(listViewItem.getItemName());
                viewHolder.deviceName.setText(listViewItem.getDeviceName());
                break;
            case 1:
                if (convertView == null){
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_wifi,null);
                    viewHolder = new ViewHolder();
                    viewHolder.itemName = view.findViewById(R.id.itemName);
                    /*viewHolder.progressBar = view.findViewById(R.id.progressBar);
                    viewHolder.ToggleButton = view.findViewById(R.id.ToggleButton);*/
                    view.setTag(viewHolder);
                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }
                viewHolder.itemName.setText(listViewItem.getItemName());
                break;
            case 2:
                if (convertView == null){
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_luminance,null);
                    viewHolder = new ViewHolder();
                    viewHolder.itemName = view.findViewById(R.id.itemName);
                    /*viewHolder.progressBar = view.findViewById(R.id.progressBar);
                    viewHolder.ToggleButton = view.findViewById(R.id.ToggleButton);*/
                    view.setTag(viewHolder);
                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }
                viewHolder.itemName.setText(listViewItem.getItemName());
            case 3:
                break;
            case 4:
            case 5:
            case 6:
                break;
        }

        /*if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_wifi,null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = view.findViewById(R.id.itemName);
            viewHolder.deviceName = view.findViewById(R.id.deviceName);
            viewHolder.progressBar = view.findViewById(R.id.progressBar);
            viewHolder.ToggleButton = view.findViewById(R.id.ToggleButton);
            viewHolder.backButton = view.findViewById(R.id.backButton);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.itemName.setText(listViewItem.getItemName());
        viewHolder.deviceName.setText(listViewItem.getDeviceName());
        viewHolder.progressBar.setMax(listViewItem.getProgressBar());
        viewHolder.ToggleButton.setText(listViewItem.getToggleButton());
        viewHolder.backButton.setText(listViewItem.getBackButton());*/

        return view;
    }

    // 创建 ViewHolder 类
    class ViewHolder {
        TextView itemName;
        TextView deviceName;
        ProgressBar progressBar;
        ToggleButton ToggleButton;
        ImageView backImg;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
