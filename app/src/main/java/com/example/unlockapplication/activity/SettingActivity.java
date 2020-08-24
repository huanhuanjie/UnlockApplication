package com.example.unlockapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.unlockapplication.adapter.WifiListAdapter;
import com.example.unlockapplication.entity.WifiListBean;
import com.example.unlockapplication.util.CollectionUtils;
import com.example.unlockapplication.util.MyWifiManager;
import com.example.unlockapplication.util.PermissionsChecker;
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


    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private final int RESULT_CODE_LOCATION = 0x001;
    //定位权限,获取app内常用权限
    String[] permsLocation = {"android.permission.ACCESS_WIFI_STATE"
            , "android.permission.CHANGE_WIFI_STATE"
            , "android.permission.ACCESS_COARSE_LOCATION"
            , "android.permission.ACCESS_FINE_LOCATION"};

    RecyclerView recyclerWifiList;
    Button btnGetWifi;
    WifiListAdapter adapter;
    private WifiManager mWifiManager;
    private List<ScanResult> mScanResultList;//wifi列表
    private List<WifiListBean> wifiListBeanList;
    private Dialog dialog;
    private View inflate;
    private WifiBroadcastReceiver wifiReceiver;
    private TextView tv_wifiState;

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





    //监听wifi变化
    private void registerReceiverWifi() {
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        registerReceiver(wifiReceiver, filter);
    }

    //setAdapter
    private void setWifiAdapter() {
        adapter = new WifiListAdapter(wifiListBeanList);
        if (wifiListBeanList.size() > 0) {
            adapter.notifyDataSetChanged();
            System.out.println("欢欢");
            Toast.makeText(SettingActivity.this, "获取wifi列表成功", Toast.LENGTH_SHORT).show();
        } else {
            adapter.notifyDataSetChanged();
            Toast.makeText(SettingActivity.this, "wifi列表为空，请检查wifi页面是否有wifi存在", Toast.LENGTH_SHORT).show();
        }
        recyclerWifiList.setAdapter(adapter);
        recyclerWifiList.setLayoutManager(new LinearLayoutManager(this));
        adapter.setmOnItemClickListerer(new WifiListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //连接wifi
                //showCentreDialog(wifiListBeanList.get(position).getName(), position);
            }
        });
    }

    //获取权限
    private void getPerMission() {
        mPermissionsChecker = new PermissionsChecker(SettingActivity.this);
        if (mPermissionsChecker.lacksPermissions(permsLocation)) {
            ActivityCompat.requestPermissions(SettingActivity.this, permsLocation, RESULT_CODE_LOCATION);
        }
    }

    private void initView() {
        /*btnGetWifi = findViewById(R.id.btnGetWifi);
        tv_wifiState = findViewById(R.id.tv_wifiState);*/
        wifiListBeanList = new ArrayList<>();
        mScanResultList = new ArrayList<>();
    }

    private void initWifiInfo() {
        //获取wifi列表
        wifiListBeanList.clear();

        //开启wifi
        MyWifiManager.openWifi(mWifiManager);
        //获取到wifi列表
        mScanResultList = MyWifiManager.getWifiList(mWifiManager);
        for (int i = 0; i < mScanResultList.size(); i++) {
            WifiListBean wifiListBean = new WifiListBean();
            wifiListBean.setName(mScanResultList.get(i).SSID);
            wifiListBean.setEncrypt(MyWifiManager.getEncrypt(mWifiManager, mScanResultList.get(i)));
            wifiListBeanList.add(wifiListBean);
        }
    }

    //中间显示的dialog
    /*public void showCentreDialog(final String wifiName, final int position) {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_centre, null);
        //自定义dialog显示风格
        dialog = new Dialog(SettingActivity.this, R.style.DialogCentre);
        //点击其他区域消失
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        dialog.show();
        TextView tvName, tvMargin;
        final EditText et_password;
        tvName = dialog.findViewById(R.id.tvName);
        tvMargin = dialog.findViewById(R.id.tvMargin);
        et_password = dialog.findViewById(R.id.et_password);

        tvName.setText("wifi：" + wifiName);
        tvMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                MyWifiManager.disconnectNetwork(mWifiManager);//断开当前wifi
                String type = MyWifiManager.getEncrypt(mWifiManager, mScanResultList.get(position));//获取加密方式
                Log.e("=====连接wifi:", wifiName + "；加密方式" + type);
                MyWifiManager.connectWifi(mWifiManager, wifiName, et_password.getText().toString(), type);//连接wifi
                dialog.dismiss();
            }
        });
    }*/


    //监听wifi状态广播接收器
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                //wifi开关变化
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED: {
                        //wifi关闭
                        Log.e("=====", "已经关闭");
                        //tv_wifiState.append("\n 打开变化：wifi已经关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        //wifi正在关闭
                        Log.e("=====", "正在关闭");
                        //tv_wifiState.append("\n 打开变化：wifi正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        //wifi已经打开
                        Log.e("=====", "已经打开");
                        //tv_wifiState.append("\n 打开变化：wifi已经打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        //wifi正在打开
                        Log.e("=====", "正在打开");
                        //tv_wifiState.append("\n 打开变化：wifi正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        //未知
                        Log.e("=====", "未知状态");
                        //tv_wifiState.append("\n 打开变化：wifi未知状态");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                //监听wifi连接状态
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.e("=====", "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.e("=====", "wifi没连接上");
                    //tv_wifiState.append("\n 连接状态：wifi没连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    Log.e("=====", "wifi以连接");
                    //tv_wifiState.append("\n 连接状态：wifi以连接，wifi名称：" + MyWifiManager.getWiFiName(mWifiManager));
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    Log.e("=====", "wifi正在连接");
                    //tv_wifiState.append("\n 连接状态：wifi正在连接");
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                //监听wifi列表变化
                Log.e("=====", "wifi列表发生变化");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消监听
        unregisterReceiver(wifiReceiver);
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
                    recyclerWifiList = wifiView.findViewById(R.id.recy_list_wifi);

                    wifiDialogBuilder.setView(wifiView);
                    wifiAlertDialog = wifiDialogBuilder.create();
                    wifiAlertDialog.show();

                    if (mWifiManager == null) {
                        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    }
                    getPerMission();//权限
                    initView();//控件初始化
                    initWifiInfo();//获取wifi
                    setWifiAdapter();//wifi列表
                }
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
                mBrightnessObserver);  //监听亮度变化
        registerReceiverWifi();//监听wifi变化
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