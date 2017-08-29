package com.prohua.demanager.view.main;

import java.util.List;
import java.util.Map;

/**
 * Model层接口
 * Created by Deep on 2017/8/29 0029.
 */

public interface MainFragmentModelInterface {

    List<Map<String, Object>> getList();

    void setList(List<Map<String, Object>> list);

    List<PathBean> getPathList();

    void setPathList(List<PathBean> pathList);

    String getBaseFile();

    void setBaseFile(String baseFile);
}
