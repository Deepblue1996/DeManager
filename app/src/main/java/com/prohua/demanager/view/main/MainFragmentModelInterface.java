package com.prohua.demanager.view.main;

import java.util.List;
import java.util.Map;

/**
 * Created by Deep on 2017/8/29 0029.
 */

public interface MainFragmentModelInterface {

    List<Map<String, Object>> getList();

    void setList(List<Map<String, Object>> list);

    List<String> getPathList();

    void setPathList(List<String> pathList);

    String getBaseFile();

    void setBaseFile(String baseFile);
}
