package com.example.unlockapplication.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unlockapplication.R;

import java.util.List;

/**
 * @Author sunhuan
 * @Date 2020/8/19
 * @Description
 */
public class MyListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ScanResult> list;
    Bitmap bitmap;

    public MyListAdapter(Context context,Bitmap bitmap, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.bitmap = bitmap;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = inflater.inflate(R.layout.item_wifi_list, null);
        ScanResult scanResult = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(scanResult.SSID);
        System.out.println("哈哈哈"+scanResult.SSID);
        TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
        signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //判断信号强度，显示对应的指示图标
        if (Math.abs(scanResult.level) > 100) {
            imageView.setImageBitmap(bitmap);
        } else if (Math.abs(scanResult.level) > 80) {
            imageView.setImageBitmap(bitmap);
        } else if (Math.abs(scanResult.level) > 70) {
            imageView.setImageBitmap(bitmap);
        } else if (Math.abs(scanResult.level) > 60) {
            imageView.setImageBitmap(bitmap);
        } else if (Math.abs(scanResult.level) > 50) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageBitmap(bitmap);
        }
        return view;
    }
}
