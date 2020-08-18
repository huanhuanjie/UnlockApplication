package com.example.unlockapplication.entity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

/**
 * @Author sunhuan
 * @Date 2020/8/17
 * @Description
 */
public class ListViewItem {

    private String itemName;
    private String deviceName;
    private ProgressBar progressBar;
    private ToggleButton toggleButton;

    public ListViewItem(String itemName, String deviceName, ProgressBar progressBar, ToggleButton toggleButton) {
        this.itemName = itemName;
        this.deviceName = deviceName;
        this.progressBar = progressBar;
        this.toggleButton = toggleButton;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ToggleButton getToggleButton() {
        return toggleButton;
    }

    public void setToggleButton(ToggleButton toggleButton) {
        this.toggleButton = toggleButton;
    }
}
