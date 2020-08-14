package com.example.unlockapplication.entity;

public class GridViewItem {

    private String imgName;
    private int imgId;

    public GridViewItem(String imgName, int imgId) {
        this.imgName = imgName;
        this.imgId = imgId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
