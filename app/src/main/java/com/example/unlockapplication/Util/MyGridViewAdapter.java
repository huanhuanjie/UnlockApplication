package com.example.unlockapplication.Util;

import android.content.Context;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unlockapplication.R;
import com.example.unlockapplication.entity.GridViewItem;

import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<GridViewItem> datas;

    public MyGridViewAdapter(Context context, List<GridViewItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    // 返回子项视图
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        GridViewItem gridViewItem = (GridViewItem) getItem(i);

        View view;
        ViewHolder viewHolder;

        if (convertView == null){
            if (i!=9){
                view = LayoutInflater.from(context).inflate(R.layout.item_layout,null);
                viewHolder = new ViewHolder();
                viewHolder.itemImage = view.findViewById(R.id.image);
                viewHolder.itemName = view.findViewById(R.id.tv);
                view.setTag(viewHolder);
            }else {
                view = LayoutInflater.from(context).inflate(R.layout.item_layout_bell,null);
                viewHolder = new ViewHolder();
                viewHolder.itemImage = view.findViewById(R.id.image_bell);
                viewHolder.itemName = view.findViewById(R.id.tv_bell);
                view.setTag(viewHolder);
            }
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (i == 9){
            viewHolder.itemName.setText(gridViewItem.getImgName());
            viewHolder.itemImage.setImageResource(gridViewItem.getImgId());
            Matrix matrix = new Matrix();
            matrix.postScale(80, 80);
            viewHolder.itemImage.setImageMatrix(matrix);
        }else if (i == 11){
            viewHolder.itemName.setText(gridViewItem.getImgName());
            viewHolder.itemImage.setImageBitmap(null);
        }else {
            viewHolder.itemName.setText(gridViewItem.getImgName());
            viewHolder.itemImage.setImageResource(gridViewItem.getImgId());
        }
        return view;
    }

    //子项个数
    @Override
    public int getCount() {
        return datas.size();
    }

    //返回子项对象
    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    //返回子项下标
    @Override
    public long getItemId(int i) {
        return i;
    }

    // 创建 ViewHolder 类
    class ViewHolder {
        ImageView itemImage;
        TextView itemName;
    }
}
