package com.example.unlockapplication.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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
import androidx.core.content.ContextCompat;

import com.example.unlockapplication.R;
import com.example.unlockapplication.Util.MyListAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Toolbar toolbar;
    TextView deviceName;
    private WifiManager wifiManager;
    List<ScanResult> list;
    View wifiView;
    AudioManager mAudioManager;
    SeekBar seekbar_volume,seekbar_luminance;
    BroadcastReceiver receiver;

    private WindowManager.LayoutParams lp = null;//窗口处理器的参数类
    int Max_Brightness = 255;   //进度条最大值
    float fBrightness = 0.0f;//亮度值

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        //设置状态栏为全透明
        StatusBarUtil.setTransparent(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        receiver = new BeekBarReceiver();
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("android.media.VOLUME_CHANGED_ACTION") ;
        filter.addAction("");
        this.registerReceiver(receiver, filter) ;

        RelativeLayout layout_device = findViewById(R.id.layout_device);
        RelativeLayout layout_volume = findViewById(R.id.layout_volume);
        //Switch switch_wifi = findViewById(R.id.switch_wifi);

        deviceName = findViewById(R.id.deviceName);

        layout_device.setOnClickListener(this);
        //switch_wifi.setOnCheckedChangeListener(this);

        seekbar_volume = findViewById(R.id.seekbar_volume);
        seekbar_luminance = findViewById(R.id.seekbar_luminance);

        seekbar_luminance.setMax(Max_Brightness);
        seekbar_luminance.setProgress(getSystemBrightness());  //默认进度值为当前系统亮度
        System.out.println("系统亮度"+getSystemBrightness());
        //默认屏幕为当前亮度(0-1)
        lp = getWindow().getAttributes();
        fBrightness = (float) seekbar_luminance.getProgress() / (float)Max_Brightness;
        System.out.println("亮度"+seekbar_luminance.getProgress());
        lp.screenBrightness =fBrightness;
        getWindow().setAttributes(lp);
        //lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取媒体声音最大值
        seekbar_volume.setMax(maxVolume);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar_volume.setProgress(currentVolume);//设置当前进度

        seekbar_volume.setOnSeekBarChangeListener(this);
        seekbar_luminance.setOnSeekBarChangeListener(this);
        /*volume_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    //设置媒体音量
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                    int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    /*@Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        wifiView = LayoutInflater.from(this).inflate(R.layout.dialog_wifi_list,null);
        init();
        dialogBuilder.setView(wifiView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }*/

    /**
     * 获得系统亮度
     *
     * @return
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
            case R.id.layout_volume:

                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.seekbar_volume:
                if(b){
                    //设置媒体音量
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                    int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
                break;
            case R.id.seekbar_luminance:
                if(i<1) i=1;//此处是为了避免screenbrightness=0，从而导致屏幕自动休眠锁屏
                fBrightness = (float) i/ (float)Max_Brightness;
                lp.screenBrightness =fBrightness;
                getWindow().setAttributes(lp);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class BeekBarReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekbar_volume.setProgress(currentVolume);
            }else if(intent.getAction().equals("est.android.setbrightness.action")){
                System.out.println("亮度变化");
                fBrightness = getSystemBrightness()/(float)Max_Brightness;
                lp.screenBrightness = fBrightness;
                getWindow().setAttributes(lp);
            }
        }
    }

    /*private void init() {
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.wifi);
        requestLocationPermission();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        openWifi();
        StringBuilder sb = new StringBuilder();
        list = wifiManager.getScanResults();

        String abc = getWIFISSID(this);
        System.out.println("哈哈哈"+abc);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ScanResult scanResult = list.get(i);
                sb.append(scanResult.SSID + "---" + scanResult.BSSID + "\n");
                list.add(scanResult);
            }
        } else {
            Log.e("哈哈", "非常遗憾未搜索到wifi");
        }
        ListView listView = (ListView) wifiView.findViewById(R.id.listView);
        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }else {
            listView.setAdapter(new MyListAdapter(this,bmp,list));
        }

    }

    *//**
     *  打开WIFI
     *//*
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

    }

    public void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(getApplicationContext(), "自Android 6.0开始需要打开位置权限才可以搜索到WIFI设备", Toast.LENGTH_SHORT);

                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }
    }

    public static String getWIFISSID(Activity activity) {
        String ssid = "unknown id";

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {

            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }*/
}
