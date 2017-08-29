package com.prohua.demanager.view.main;

/**
 * 路径信息记录
 * Created by Deep on 2017/8/29 0029.
 */

public class PathBean {
    // 目录名称
    private String pathName;
    // 用户滚到了距离记录
    private int itemHeight;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }
}
