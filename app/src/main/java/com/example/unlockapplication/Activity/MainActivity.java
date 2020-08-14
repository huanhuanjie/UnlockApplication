package com.example.unlockapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.gridview_numbers);
        TextView time = findViewById(R.id.time);
        TextView date = findViewById(R.id.date);
        TextView week = findViewById(R.id.week);
        TextView lunar_date = findViewById(R.id.lunar_date);

        obtainTime = new ObtainTime();
        time.setText(obtainTime.getTime());
        date.setText(obtainTime.getDate());
        week.setText(obtainTime.getWeek());
        //lunar_date.setText(obtainTime.getWeek());

        images = new int[]{R.drawable.num1,R.drawable.num2,R.drawable.num3,
                R.drawable.num4,R.drawable.num5,R.drawable.num6,
                R.drawable.num7,R.drawable.num8,R.drawable.num9,
                R.drawable.bell,R.drawable.num0};

        for (int i=0;i<11;i++){
            datas.add(new GridViewItem("", images[i]));
        }
        datas.add(new GridViewItem("取消",0));

        MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(this,datas);
        gridView.setAdapter(gridViewAdapter);
    }
}