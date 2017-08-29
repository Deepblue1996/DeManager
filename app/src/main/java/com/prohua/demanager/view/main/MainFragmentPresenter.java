package com.prohua.demanager.view.main;

import android.util.Log;

import com.prohua.demanager.R;
import com.prohua.demanager.util.GetFilesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Presenter层
 * Created by Deep on 2017/8/29 0029.
 */

public class MainFragmentPresenter {

    private MainFragmentModel mainFragmentModel;

    private MainFragmentInterface mainFragmentInterface;

    public MainFragmentPresenter(MainFragmentInterface view) {
        mainFragmentInterface = view;
        mainFragmentModel = new MainFragmentModel();

        // 开新线程加载列表
        new Thread(() -> {
            try {
                loadFolderList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 进入目录
     */
    public void innerName(int position) {
        Log.i("path",addPathList(mainFragmentModel.getPathList().get(position)).toString());
    }

    /**
     * 第一次初始化列表
     */
    public void loadFolderList() throws IOException {
        // 获取根目录绝对路径
        mainFragmentModel.setBaseFile(GetFilesUtils.getInstance().getBasePath());
        // 根据路径获取列表
        try {
            loadFolderList(addPathList("").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 路径
     */
    public StringBuffer addPathList(String path) {
        List<String> pathList;
        if (mainFragmentModel.getPathList().size() == 0) {
            pathList = mainFragmentModel.getPathList();
            pathList.add(mainFragmentModel.getBaseFile());
        } else {
            pathList = mainFragmentModel.getPathList();
            pathList.add(path);
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
            stringBuffer.append(mainFragmentModel.getPathList().get(i)).append("/");
        }
        File file = new File(stringBuffer.toString());
        //判断文件夹是否存在,如果不存在则不添加,回滚
        if (!file.exists()) {
            pathList.remove(pathList.size()-1);
            stringBuffer = new StringBuffer();
            for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
                stringBuffer.append(mainFragmentModel.getPathList().get(i)).append("/");
            }
        }
        return stringBuffer;
    }

    /**
     * 加载列表
     */
    public void loadFolderList(String file) throws IOException {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(file);
        if (list != null) {
            Collections.sort(list, GetFilesUtils.getInstance().defaultOrder());
            mainFragmentModel.getList().clear();
            for (Map<String, Object> map : list) {
                String fileType = (String) map.get(GetFilesUtils.FILE_INFO_TYPE);
                Map<String, Object> gMap = new HashMap<String, Object>();
                if (map.get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)) {
                    gMap.put("fIsDir", true);
                    gMap.put("fImg", R.mipmap.dir);
                    gMap.put("fInfo", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS) + "个文件夹和" +
                            map.get(GetFilesUtils.FILE_INFO_NUM_SONFILES) + "个文件");
                } else {
                    gMap.put("fIsDir", false);
                    if (fileType.equals("txt") || fileType.equals("text")) {
                        gMap.put("fImg", R.mipmap.file);
                    } else {
                        gMap.put("fImg", R.mipmap.file);
                    }
                    gMap.put("fInfo", "文件大小:" + GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put("fPath", map.get(GetFilesUtils.FILE_INFO_PATH));
                mainFragmentModel.getList().add(gMap);
            }
        } else {
            mainFragmentModel.getList().clear();
        }

        EventBus.getDefault().post(new MainFragmentEvent(mainFragmentModel.getList(), mainFragmentModel.getPathList()));
    }

    /**
     * 获取名称
     */
    public String getPositionName(int position) {
        return mainFragmentModel.getList().get(position).get("fName").toString();
    }

    /**
     * 获取图片
     */
    public String getPositionImg(int position) {
        return mainFragmentModel.getList().get(position).get("fImg").toString();
    }

    /**
     * 获取名称
     */
    public String getPathPosition(int position) {
        return mainFragmentModel.getPathList().get(position);
    }
}
