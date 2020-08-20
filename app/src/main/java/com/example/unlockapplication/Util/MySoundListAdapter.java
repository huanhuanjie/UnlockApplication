package com.example.unlockapplication.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.unlockapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sunhuan
 * @Date 2020/8/20
 * @Description  RecyclerView适配器
 */
public class MySoundListAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataList = new ArrayList<>();

    public MySoundListAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // 创建 ViewHolder 类
    class ViewHolder {
        TextView itemName;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        ViewHolder viewHolder;

        if (view== null){
            v = LayoutInflater.from(context).inflate(R.layout.listitem_sound_effect,null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = v.findViewById(R.id.tv_sound_effect);
            v.setTag(viewHolder);
        }else {
            v = view;
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.itemName.setText(dataList.get(i));
        return v;
    }
}
