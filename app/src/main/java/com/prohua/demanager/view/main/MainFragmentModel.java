package com.prohua.demanager.view.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Model层
 * Created by Deep on 2017/8/29 0029.
 */

public class MainFragmentModel implements MainFragmentModelInterface {

    // 已选择文件列表数据
    private List<Map<String, Object>> sList = new ArrayList<>();
    // 文件列表数据
    private List<Map<String, Object>> list = new ArrayList<>();
    // 路径
    private List<PathBean> pathList = new ArrayList<>();
    // 基础路径
    private String baseFile;

    private boolean select = false;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<Map<String, Object>> getsList() {
        return sList;
    }

    public void setsList(List<Map<String, Object>> sList) {
        this.sList = sList;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public List<PathBean> getPathList() {
        return pathList;
    }

    public void setPathList(List<PathBean> pathList) {
        this.pathList = pathList;
    }

    public String getBaseFile() {
        return baseFile;
    }

    public void setBaseFile(String baseFile) {
        this.baseFile = baseFile;
    }
}
