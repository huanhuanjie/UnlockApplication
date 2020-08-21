package com.example.unlockapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unlockapplication.R;
import com.example.unlockapplication.util.AppContants;
import com.example.unlockapplication.util.CollectionUtils;
import com.example.unlockapplication.util.WifiListAdapter;
import com.example.unlockapplication.util.WifiSupport;
import com.example.unlockapplication.entity.WifiBean;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener {

    RelativeLayout layout_device,layout_sound_effect,layout_openpwd,layout_setpwd;
    Switch switch_wifi,switch_sound_effect1,switch_sound_effect2,switch_sound_effect3;
    SeekBar seekbar_volume,seekbar_luminance;
    Toolbar toolbar;
    TextView deviceName;

    AudioManager mAudioManager;
    BroadcastReceiver receiver;
    SoundPool mSoundPool;
    AlertDialog wifiAlertDialog = null;

    int Max_Brightness = 255;   //亮度进度条最大值
    int maxVolume,currentVolume;
    String openPwd = "111111",setPwd = "111111";
    int streamID1,streamID2,streamID3;
    int soundid1,soundid2,soundid3;
    List<WifiBean> realWifiList = new ArrayList<>();
    private boolean mHasPermission;
    WifiListAdapter adapter;

    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

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
        switch_wifi = findViewById(R.id.switch_wifi);
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
        switch_wifi.setOnCheckedChangeListener(this);
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

                switch_sound_effect1 = soundEffectView.findViewById(R.id.switch_sound_effect1);
                switch_sound_effect2 = soundEffectView.findViewById(R.id.switch_sound_effect2);
                switch_sound_effect3 = soundEffectView.findViewById(R.id.switch_sound_effect3);

                dialogBuilder.setView(soundEffectView);
                AlertDialog alertDialog1 = dialogBuilder.create();
                alertDialog1.show();

                //音效预加载
                mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
                streamID1 = mSoundPool.load(this,R.raw.type_sound, 1);
                streamID2 = mSoundPool.load(this,R.raw.warn_sound, 1);
                streamID3 = mSoundPool.load(this,R.raw.normal_sound, 1);

                alertDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        mSoundPool.release();
                    }
                });

                switch_sound_effect1.setOnCheckedChangeListener(this);
                switch_sound_effect2.setOnCheckedChangeListener(this);
                switch_sound_effect3.setOnCheckedChangeListener(this);
                break;
            case R.id.layout_openpwd:
                View openPwdView = LayoutInflater.from(this).inflate(R.layout.dialog_openpwd,null);

                Button surebtn_openpwd = openPwdView.findViewById(R.id.surebtn_openpwd);
                Button canclebtn_openpwd = openPwdView.findViewById(R.id.canclebtn_openpwd);
                final EditText open_pwd = openPwdView.findViewById(R.id.open_pwd);
                final EditText open_newpwd = openPwdView.findViewById(R.id.open_newpwd);
                final EditText open_surepwd = openPwdView.findViewById(R.id.open_surepwd);
                final TextView open_tip = openPwdView.findViewById(R.id.open_tip);

                dialogBuilder.setView(openPwdView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                surebtn_openpwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!open_pwd.getText().toString().equals(openPwd)){
                            open_tip.setText("原密码错误!");
                            System.out.println("原密码错误!");
                        }else if (!open_newpwd.getText().toString().equals(open_surepwd.getText().toString())){
                            open_tip.setText("新密码与确认密码不一致！");
                            System.out.println("新密码与确认密码不一致！");
                        }else {
                            openPwd = open_newpwd.getText().toString();
                            alertDialog.dismiss();
                        }
                    }
                });

                canclebtn_openpwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                break;
            case R.id.layout_setpwd:
                final View setPwdView = LayoutInflater.from(this).inflate(R.layout.dialog_setpwd,null);

                Button surebtn_setpwd = setPwdView.findViewById(R.id.surebtn_setpwd);
                Button canclebtn_setpwd = setPwdView.findViewById(R.id.canclebtn_setpwd);
                final EditText set_pwd = setPwdView.findViewById(R.id.set_pwd);
                final EditText set_newpwd = setPwdView.findViewById(R.id.set_newpwd);
                final EditText set_surepwd = setPwdView.findViewById(R.id.set_surepwd);
                final TextView set_tip = setPwdView.findViewById(R.id.set_tip);

                dialogBuilder.setView(setPwdView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                surebtn_setpwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!set_pwd.getText().toString().equals(setPwd)){
                            set_tip.setText("原密码错误!");
                            System.out.println("原密码错误!");
                        }else if (!set_newpwd.getText().toString().equals(set_surepwd.getText().toString())){
                            set_tip.setText("新密码与确认密码不一致！");
                            System.out.println("新密码与确认密码不一致！");
                        }else {
                            setPwd = set_newpwd.getText().toString();
                            alertDialog.dismiss();
                        }
                    }
                });

                canclebtn_setpwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 开关按钮监听事件
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.switch_wifi:
                AlertDialog.Builder wifiDialogBuilder = new AlertDialog.Builder(this);
                if (b){
                    View wifiView = LayoutInflater.from(this).inflate(R.layout.dialog_wifi_list,null);
                    RecyclerView recyclerWifiList = wifiView.findViewById(R.id.recy_list_wifi);

                    wifiDialogBuilder.setView(wifiView);
                    wifiAlertDialog = wifiDialogBuilder.create();
                    wifiAlertDialog.show();

                    mHasPermission = checkPermission();
                    if (!mHasPermission && WifiSupport.isOpenWifi(this)) {  //未获取权限，申请权限
                        requestPermission();
                    }else if(mHasPermission && WifiSupport.isOpenWifi(this)){  //已经获取权限
                        adapter = new WifiListAdapter(this,realWifiList);
                        recyclerWifiList.setLayoutManager(new LinearLayoutManager(this));
                        recyclerWifiList.setAdapter(adapter);
                    }else{
                        Toast.makeText(this,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();
                    }
                    sortScaResult();
                }
                switch_wifi.setChecked(false);
                /*wifiAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        switch_wifi.setChecked(false);
                        //System.out.println("wifi列表消失");
                    }
                });*/
                break;
            case R.id.switch_sound_effect1:
                if (b){
                    soundid1 = mSoundPool.play(streamID1, 1, 1, 1, -1, 1.0f);
                    switch_sound_effect2.setChecked(false);
                    switch_sound_effect3.setChecked(false);
                }else {
                    mSoundPool.stop(soundid1);
                }
                break;
            case R.id.switch_sound_effect2:
                if (b){
                    soundid2 = mSoundPool.play(streamID2, 1, 1, 1, -1, 1.0f);
                    switch_sound_effect1.setChecked(false);
                    switch_sound_effect3.setChecked(false);
                }else {
                    mSoundPool.stop(soundid2);
                }
                break;
            case R.id.switch_sound_effect3:
                if (b){
                    soundid3 = mSoundPool.play(streamID3, 1, 1, 1, -1, 1.0f);
                    switch_sound_effect1.setChecked(false);
                    switch_sound_effect2.setChecked(false);
                }else {
                    mSoundPool.stop(soundid3);
                }
                break;
        }
    }


    /**
     * 进度条滑动监听事件
     */
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

    /**
     * 检查是否已经授予权限
     * @return
     */
    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    public void sortScaResult(){
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(this));
        realWifiList.clear();
        if(!CollectionUtils.isNullOrEmpty(scanResults)){
            for(int i = 0;i < scanResults.size();i++){
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);

                //排序
                Collections.sort(realWifiList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}