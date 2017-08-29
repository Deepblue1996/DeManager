package com.prohua.demanager.view.main;

import com.prohua.demanager.R;
import com.prohua.demanager.util.GetFilesUtils;

import org.greenrobot.eventbus.EventBus;

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
     * 第一次初始化列表
     */
    public void loadFolderList() throws IOException {
        // 获取根目录绝对路径
        mainFragmentModel.setBaseFile(GetFilesUtils.getInstance().getBasePath());
        // 根据路径获取列表
        loadFolderList(mainFragmentModel.getBaseFile());
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

        MainFragmentEvent event = new MainFragmentEvent();
        event.setList(mainFragmentModel.getList());
        EventBus.getDefault().post(event);
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
}
