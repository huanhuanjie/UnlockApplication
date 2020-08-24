package com.example.unlockapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.unlockapplication.R;
import com.example.unlockapplication.entity.WifiListBean;

import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

    private List<WifiListBean> wifiListBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListerer;

    public void setmOnItemClickListerer(OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }

    public WifiListAdapter(List<WifiListBean> wifiListBeanList) {
        this.wifiListBeanList = wifiListBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(wifiListBeanList.get(position).getName());
        holder.tv_encrypt.setText("加密方式：" + wifiListBeanList.get(position).getEncrypt());
        /*holder.btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListerer.onItemClick(v, position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return wifiListBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_encrypt;
        Button btn_link;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.wifi_name);
            tv_encrypt = view.findViewById(R.id.wifi_encrypt);
        }
    }
}
