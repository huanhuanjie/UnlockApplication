package com.example.unlockapplication.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unlockapplication.R;
import com.example.unlockapplication.Util.MySoundListAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    RelativeLayout layout_device,layout_sound_effect,layout_openpwd,layout_setpwd;
    SeekBar seekbar_volume,seekbar_luminance;
    Toolbar toolbar;
    TextView deviceName;

    AudioManager mAudioManager;
    BroadcastReceiver receiver;

    int Max_Brightness = 255;   //亮度进度条最大值
    int maxVolume,currentVolume;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        //设置状态栏为全透明，让背景图片充满状态栏
        StatusBarUtil.setTransparent(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //媒体音量变化广播
        receiver = new BeekBarReceiver();
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("android.media.VOLUME_CHANGED_ACTION") ;
        filter.addAction("");
        this.registerReceiver(receiver, filter);

        //各设置项
        layout_device = findViewById(R.id.layout_device);
        seekbar_luminance = findViewById(R.id.seekbar_luminance);
        seekbar_volume = findViewById(R.id.seekbar_volume);
        layout_sound_effect = findViewById(R.id.layout_sound_effect);
        layout_openpwd = findViewById(R.id.layout_openpwd);
        layout_setpwd = findViewById(R.id.layout_setpwd);
        deviceName = findViewById(R.id.deviceName);

        //亮度
        seekbar_luminance.setMax(Max_Brightness);
        seekbar_luminance.setProgress(getSystemBrightness());  //默认进度值为当前系统亮度

        //音量
        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取媒体声音最大值
        seekbar_volume.setMax(maxVolume);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar_volume.setProgress(currentVolume);//设置当前进度

        layout_device.setOnClickListener(this);
        seekbar_volume.setOnSeekBarChangeListener(this);
        seekbar_luminance.setOnSeekBarChangeListener(this);
        layout_sound_effect.setOnClickListener(this);
        layout_openpwd.setOnClickListener(this);
        layout_setpwd.setOnClickListener(this);
    }

    /**
     * 监听屏幕亮度变化
     */
    private ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            seekbar_luminance.setProgress(getSystemBrightness());
        }
    };

    /**
     * 利用广播监听媒体音量变化
     */
    private class BeekBarReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekbar_volume.setProgress(currentVolume);
            }
        }
    }

    /**
     * 获取系统亮度
     */
    private int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog;
        switch (view.getId()){
            case R.id.layout_device:
                View deviceView = LayoutInflater.from(this).inflate(R.layout.dialog_device,null);

                final EditText device_editText = deviceView.findViewById(R.id.device_editText);
                Button surebtn_device = deviceView.findViewById(R.id.surebtn_device);
                Button canclebtn_device = deviceView.findViewById(R.id.canclebtn_device);

                device_editText.setText(deviceName.getText());
                dialogBuilder.setView(deviceView);
                dialogBuilder.setCancelable(false);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                surebtn_device.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deviceName.setText(device_editText.getText());
                        alertDialog.dismiss();
                    }
                });
                canclebtn_device.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.layout_wifi:
                View wifiView = LayoutInflater.from(this).inflate(R.layout.dialog_wifi_list,null);
                //init();
                dialogBuilder.setView(wifiView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.layout_sound_effect:
                View soundEffectView = LayoutInflater.from(this).inflate(R.layout.dialog_sound_effect,null);

                dialogBuilder.setView(soundEffectView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                List<String> list = new ArrayList<>();
                list.add("按鍵音");list.add("報警音");list.add("正常音");

                ListView listview_sound_effect = soundEffectView.findViewById(R.id.listview_sound_effect);
                MySoundListAdapter adapter = new MySoundListAdapter(this,list);
                listview_sound_effect.setAdapter(adapter);
                break;
            case R.id.layout_openpwd:
                View openPwdView = LayoutInflater.from(this).inflate(R.layout.dialog_openpwd,null);
                dialogBuilder.setView(openPwdView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.layout_setpwd:
                View setPwdView = LayoutInflater.from(this).inflate(R.layout.dialog_setpwd,null);
                dialogBuilder.setView(setPwdView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.seekbar_volume:
                if(b){
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);//设置媒体音量
                    int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
                break;
            case R.id.seekbar_luminance:
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                        seekbar_luminance.getProgress());
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true,
                mBrightnessObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(receiver);
        //注销亮度监听
        getContentResolver().unregisterContentObserver(
                mBrightnessObserver);
    }
}