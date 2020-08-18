package com.example.unlockapplication.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unlockapplication.R;
import com.example.unlockapplication.Util.MyGridViewAdapter;
import com.example.unlockapplication.Util.ObtainTime;
import com.example.unlockapplication.entity.GridViewItem;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ObtainTime obtainTime;
    ArrayList<GridViewItem> datas = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList();
    int[] images;String pwd = "";

    GridView gridView;LinearLayout linlayout_circle;
    ImageView circle_1,circle_2,circle_3,circle_4,circle_5,circle_6;
    TextView time;TextView date;TextView week;TextView lunar_date;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏为全透明
        StatusBarUtil.setTransparent(this);

        gridView = findViewById(R.id.gridview_numbers);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        week = findViewById(R.id.week);
        lunar_date = findViewById(R.id.lunar_date);
        linlayout_circle = findViewById(R.id.linlayout_circle);
        circle_1 = findViewById(R.id.circle_1);
        circle_2 = findViewById(R.id.circle_2);
        circle_3 = findViewById(R.id.circle_3);
        circle_4 = findViewById(R.id.circle_4);
        circle_5 = findViewById(R.id.circle_5);
        circle_6 = findViewById(R.id.circle_6);

        linlayout_circle.setVisibility(View.INVISIBLE);
        imageViews.add(circle_1);imageViews.add(circle_2);imageViews.add(circle_3);
        imageViews.add(circle_4);imageViews.add(circle_5);imageViews.add(circle_6);

        obtainTime = new ObtainTime();
        time.setText(obtainTime.getTime());
        date.setText(obtainTime.getDate());
        week.setText(obtainTime.getWeek());
        lunar_date.setText(obtainTime.getLunarDate());

        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReciver,filter);

        images = new int[]{R.drawable.num1,R.drawable.num2,R.drawable.num3,
                R.drawable.num4,R.drawable.num5,R.drawable.num6,
                R.drawable.num7,R.drawable.num8,R.drawable.num9,
                R.drawable.bell,R.drawable.num0};

        for (int i=0;i<11;i++)
            datas.add(new GridViewItem("", images[i]));
        datas.add(new GridViewItem("取消",0));

        MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(this,datas);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(this);
    }

    //通过广播实时刷新时间
    private final BroadcastReceiver timeReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                time.setText(obtainTime.getTime());
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        linlayout_circle.setVisibility(View.VISIBLE);
        if (pwd.length() < 6){
            inputPwd(i);
            if (i != 11)
                imageViews.get(pwd.length() - 1).setImageResource(R.drawable.circle_100);
            if (pwd.length() == 6){
                if (pwd.equals("111111")){
                    Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivityForResult(settingIntent,1);
                }else {
                    pwd = "";
                    for (int k = 0; k < 6; k++)
                        imageViews.get(k).setImageResource(R.drawable.circle_0);
                }
            }
        }
    }

    //输入密码
    private void inputPwd(int i){
        if(i < 9)
            pwd += (i+1);
        else if (i == 10)
            pwd += 0;
        else if (i == 11 && pwd.length() > 0){   //删除
            pwd = pwd.substring(0,pwd.length()-1);
            imageViews.get(pwd.length()).setImageResource(R.drawable.circle_0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            pwd = "";
            linlayout_circle.setVisibility(View.INVISIBLE);
            for (int i = 0; i < 6; i++)
                imageViews.get(i).setImageResource(R.drawable.circle_0);
        }

    }
}