package com.example.unlockapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.TextView;

import com.example.unlockapplication.R;
import com.example.unlockapplication.Util.MyGridViewAdapter;
import com.example.unlockapplication.Util.ObtainTime;
import com.example.unlockapplication.entity.GridViewItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ObtainTime obtainTime;
    ArrayList<GridViewItem> datas = new ArrayList<>();
    int[] images;

    TextView time;
    TextView date;
    TextView week;
    TextView lunar_date;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.gridview_numbers);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        week = findViewById(R.id.week);
        lunar_date = findViewById(R.id.lunar_date);

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

}