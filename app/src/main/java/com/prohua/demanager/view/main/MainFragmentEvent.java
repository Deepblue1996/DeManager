package com.prohua.demanager.view.main;

import java.util.List;
import java.util.Map;

/**
 * 中间事件
 * Created by Deep on 2017/8/29 0029.
 */

public class MainFragmentEvent {
    private List<Map<String, Object>> list;
    private List<String> plist;

    public List<String> getPlist() {
        return plist;
    }

    public MainFragmentEvent(List<Map<String, Object>> list, List<String> plist) {
        this.list = list;
        this.plist = plist;
    }

    public void setPlist(List<String> plist) {
        this.plist = plist;

    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
