package com.example.unlockapplication.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unlockapplication.R;
import com.example.unlockapplication.Util.MyListViewAdapter;
import com.example.unlockapplication.entity.ListViewItem;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ListViewItem> datas = new ArrayList<>();
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        //设置状态栏为全透明
        StatusBarUtil.setTransparent(this);
        //ListView listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(this);

        /*ListViewItem device_item = new ListViewItem("設備名稱","001" ,
                null,null);
        ListViewItem wifi_item = new ListViewItem("WIFI","",
                null,null);
        ListViewItem lum_item = new ListViewItem("亮度","",
                null,null);
        ListViewItem volume_item = new ListViewItem("音量","",
                null,null);
        ListViewItem sound_effect_item = new ListViewItem("音效","",
                null,null);
        ListViewItem open_pwd_item = new ListViewItem("更改開門密碼","",
                null,null);
        ListViewItem set_pwd_item = new ListViewItem("更改設定密碼","",
                null,null);
        datas.add(device_item);datas.add(wifi_item);datas.add(lum_item);
        datas.add(volume_item);datas.add(sound_effect_item);datas.add(open_pwd_item);
        datas.add(set_pwd_item);

        listView.setAdapter(new MyListViewAdapter(this,datas));*/
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
