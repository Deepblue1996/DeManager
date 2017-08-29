package com.prohua.demanager.view.main;

import java.util.List;
import java.util.Map;

/**
 * 中间事件
 * Created by Deep on 2017/8/29 0029.
 */

public class MainFragmentEvent {
    private List<Map<String, Object>> list;
    private List<PathBean> plist;

    private int position; // 1 add -1 reduce

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<PathBean> getPlist() {
        return plist;
    }

    public MainFragmentEvent(List<Map<String, Object>> list, List<PathBean> plist, int position) {
        this.list = list;
        this.plist = plist;
        this.position = position;
    }

    public void setPlist(List<PathBean> plist) {
        this.plist = plist;

    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
