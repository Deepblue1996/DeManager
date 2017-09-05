package com.prohua.demanager.view.main;

import android.util.Log;

import com.prohua.demanager.R;
import com.prohua.demanager.util.GetFilesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Presenter层
 * Created by Deep on 2017/8/29 0029.
 */

public class MainFragmentPresenter {

    // 数据
    private MainFragmentModel mainFragmentModel;
    // 视图
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
        String str = addPathList(getPositionName(position)).toString();
        if (!str.equals("not dir")) {
            Log.i("path", str);
            // 开新线程加载列表
            String finalStr = str;

            new Thread(() -> {
                try {
                    loadFolderList(finalStr);
                    EventBus.getDefault().post(new MainFragmentEvent(mainFragmentModel.getList(), mainFragmentModel.getPathList(), 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            // 打开文件
            str = getFilePath(getPositionName(position)).toString();
            if (!str.equals("not file")) {
                mainFragmentInterface.stopRefreshAnimator();
                mainFragmentInterface.openFiles(str);
            } else {
                Log.i("open file code", "400");
            }
        }
    }

    /**
     * 初始化列表
     */
    public void loadFolderList() throws IOException {
        // 获取根目录绝对路径
        mainFragmentModel.setBaseFile(GetFilesUtils.getInstance().getBasePath());
        // 根据路径获取列表
        try {
            loadFolderList(addPathList("").toString());
            EventBus.getDefault().post(new MainFragmentEvent(mainFragmentModel.getList(), mainFragmentModel.getPathList(), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开目录的路径
     */
    public StringBuffer addPathList(String path) {
        List<PathBean> pathList;
        PathBean pathBean = new PathBean();
        if (mainFragmentModel.getPathList().size() == 0) {
            pathList = mainFragmentModel.getPathList();
            pathBean.setPathName(mainFragmentModel.getBaseFile());
            pathBean.setItemHeight(0);
            pathList.add(pathBean);
        } else {
            pathList = mainFragmentModel.getPathList();
            pathBean.setPathName(path);
            pathBean.setItemHeight(0);
            pathList.add(pathBean);
            pathList.get(getPathListSize() - 2).setItemHeight(mainFragmentInterface.getRecyclerViewItemScroll());
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
            stringBuffer.append(mainFragmentModel.getPathList().get(i).getPathName()).append("/");
        }
        Log.i("path - dir", stringBuffer.toString());
        File file = new File(stringBuffer.toString());
        //判断文件夹是否存在,如果不存在则不添加,回滚
        if (!file.isDirectory()) {
            pathList.remove(pathList.size() - 1);
            stringBuffer = new StringBuffer();
            stringBuffer.append("not dir");
            return stringBuffer;
        } else {
            return stringBuffer;
        }
    }

    /**
     * 获取当前打开文件的路径
     */
    public StringBuffer getFilePath(String path) {

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
            stringBuffer.append(mainFragmentModel.getPathList().get(i).getPathName()).append("/");
        }
        stringBuffer.append(path);
        Log.i("path - dir", stringBuffer.toString());
        File file = new File(stringBuffer.toString());
        //判断文件夹是否存在,如果不存在则不添加,回滚
        if (!file.isFile()) {
            stringBuffer = new StringBuffer();
            stringBuffer.append("not file");
            return stringBuffer;
        } else {
            return stringBuffer;
        }
    }

    /**
     * 加载列表,相关信息
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
                    // 添加信息
                    gMap.put("fIsDir", false);
                    if (fileType.equals("txt") || fileType.equals("text")) {
                        gMap.put("fImg", R.mipmap.type_txt);
                    } else if (fileType.equals("mp4") || fileType.equals("3gp") || fileType.equals("avi")
                            || fileType.equals("mkv") || fileType.equals("rmvb")) {
                        gMap.put("fImg", R.mipmap.type_video);
                    } else if (fileType.equals("pdf")) {
                        gMap.put("fImg", R.mipmap.type_pdf);
                    } else if (fileType.equals("cd") || fileType.equals("ogg") || fileType.equals("mp3")
                            || fileType.equals("asf") || fileType.equals("wma") || fileType.equals("wav") || fileType.equals("mp3pro")
                            || fileType.equals("rm") || fileType.equals("real") || fileType.equals("ape") || fileType.equals("module")
                            || fileType.equals("midi") || fileType.equals("vgf")) {
                        gMap.put("fImg", R.mipmap.type_music);
                    } else if (fileType.equals("zip") || fileType.equals("rar") || fileType.equals("gz")) {
                        gMap.put("fImg", R.mipmap.type_zip);
                    } else if (fileType.equals("torrent")) {
                        gMap.put("fImg", R.mipmap.type_bt);
                    } else if (fileType.equals("bmp") || fileType.equals("pcx") || fileType.equals("tiff") || fileType.equals("jpg")
                            || fileType.equals("gif") || fileType.equals("jpeg") || fileType.equals("tga") || fileType.equals("exif")
                            || fileType.equals("fpx") || fileType.equals("svg") || fileType.equals("psd") || fileType.equals("cdr")
                            || fileType.equals("pcd") || fileType.equals("dxf") || fileType.equals("ufo") || fileType.equals("eps")
                            || fileType.equals("ai") || fileType.equals("png") || fileType.equals("hdri") || fileType.equals("raw")) {
                        gMap.put("fImg", R.mipmap.type_img);
                    } else {
                        gMap.put("fImg", R.mipmap.file);
                    }
                    gMap.put("fInfo", "文件大小:" + GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put("fPath", map.get(GetFilesUtils.FILE_INFO_PATH));
                gMap.put("fSelect", 0);
                mainFragmentModel.getList().add(gMap);
            }
        } else {
            mainFragmentModel.getList().clear();
        }
    }

    /**
     * 获取名称
     */
    public String getPositionName(int position) {
        return mainFragmentModel.getList().get(position).get("fName").toString();
    }

    /**
     * 获取名称
     */
    public String getSPositionName(int position) {
        return mainFragmentModel.getsList().get(position).get("fName").toString();
    }

    /**
     * 获取图片
     */
    public String getPositionImg(int position) {
        return mainFragmentModel.getList().get(position).get("fImg").toString();
    }

    /**
     * 获取图片
     */
    public String getSPositionImg(int position) {
        return mainFragmentModel.getsList().get(position).get("fImg").toString();
    }

    /**
     * 获取名称
     */
    public String getPathPosition(int position) {
        return mainFragmentModel.getPathList().get(position).getPathName();
    }

    /**
     * 获取是否选择
     */
    public int getIsSelectPosition(int position) {
        return (int) mainFragmentModel.getList().get(position).get("fSelect");
    }

    /**
     * 获取路径长度
     */
    public int getPathListSize() {
        return mainFragmentModel.getPathList().size();
    }

    /**
     * 获取记录的距离
     */
    public int getPathScroll() {
        Log.i("path - this - height", mainFragmentModel.getPathList().get(getPathListSize() - 1).getItemHeight() + "");
        return mainFragmentModel.getPathList().get(getPathListSize() - 1).getItemHeight();
    }

    /**
     * 返回上一级
     */
    public void beforePath() {
        List<PathBean> pathList = mainFragmentModel.getPathList();
        pathList.remove(getPathListSize() - 1);
        mainFragmentModel.setPathList(pathList);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
            stringBuffer.append(mainFragmentModel.getPathList().get(i).getPathName()).append("/");
        }
        // 开新线程加载列表
        new Thread(() -> {
            try {
                loadFolderList(stringBuffer.toString());
                EventBus.getDefault().post(new MainFragmentEvent(mainFragmentModel.getList(), mainFragmentModel.getPathList(), -1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 根据用户点击item,快速切换到指定路径
     */
    public void selectPath(int position) {
        List<PathBean> fpathList = mainFragmentModel.getPathList();
        List<PathBean> pathList = new ArrayList<>();
        for (int i = 0; i < position + 1; i++) {
            pathList.add(fpathList.get(i));
        }
        mainFragmentModel.setPathList(pathList);

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mainFragmentModel.getPathList().size(); i++) {
            stringBuffer.append(mainFragmentModel.getPathList().get(i).getPathName()).append("/");
        }

        Log.i("fpath", stringBuffer + "");

        // 开新线程加载列表
        new Thread(() -> {
            try {
                loadFolderList(stringBuffer.toString());
                EventBus.getDefault().post(new MainFragmentEvent(mainFragmentModel.getList(), mainFragmentModel.getPathList(), 2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 设置显示选择
     */
    public void setListVisibilitySelect() {

        for (int i = 0; i < mainFragmentModel.getList().size(); i++) {
            mainFragmentModel.getList().get(i).put("fSelect", 1);
        }

        mainFragmentModel.setSelect(true);
    }

    /**
     * 设置不显示选择
     */
    public void setListVisibilityUnSelect() {

        for (int i = 0; i < mainFragmentModel.getList().size(); i++) {
            mainFragmentModel.getList().get(i).put("fSelect", 0);
        }

        mainFragmentModel.setSelect(false);
    }

    /**
     * 设置指定Item选择状态
     */
    public void setListItemSelect(int position) {

        if ((int) mainFragmentModel.getList().get(position).get("fSelect") == 1) {
            mainFragmentModel.getList().get(position).put("fSelect", 2);
        } else {
            mainFragmentModel.getList().get(position).put("fSelect", 1);
        }
    }

    /**
     * 添加当前选择的进选择列表
     *
     * @return 已选择数量
     */
    public int addInSList() {

        // 清空当前列表的数据,再添加进入
        for (int i = 0; i < mainFragmentModel.getList().size(); i++) {

                for (int j = 0; j < mainFragmentModel.getsList().size(); j++) {
                    // 如果已添加,则不添加
                    if (mainFragmentModel.getsList().get(j).get("fPath").equals(mainFragmentModel.getList().get(i).get("fPath")) &&
                            mainFragmentModel.getsList().get(j).get("fName").equals(mainFragmentModel.getList().get(i).get("fName"))) {
                        mainFragmentModel.getsList().remove(j);
                    }
                }
        }


        for (int i = 0; i < mainFragmentModel.getList().size(); i++) {
            // 处理已选择的
            if ((int) mainFragmentModel.getList().get(i).get("fSelect") == 2) {
                mainFragmentModel.getsList().add(mainFragmentModel.getList().get(i));
            }
        }

        return mainFragmentModel.getsList().size();
    }

    /**
     * 清空已选择的列表
     */
    public void clearSList() {
        mainFragmentModel.getsList().clear();
    }

    /**
     * 获取已选择的数量
     *
     * @return 已选择数量
     */
    public int getSListSize() {
        return mainFragmentModel.getsList().size();
    }


    /**
     * 获取已选择的文件数量
     *
     * @return 已选择数量
     */
    public int getSListFileSize() {
        int fileSize = 0;
        for (int i = 0; i < mainFragmentModel.getsList().size(); i++) {
            if (mainFragmentModel.getsList().get(i).get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(false)) {
                fileSize++;
            }
        }
        return fileSize;
    }

    /**
     * 获取已选择的目录数量
     *
     * @return 已选择数量
     */
    public int getSListDirSize() {
        int dirSize = 0;
        for (int i = 0; i < mainFragmentModel.getsList().size(); i++) {
            if (mainFragmentModel.getsList().get(i).get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)) {
                dirSize++;
            }
        }
        return dirSize;
    }

    /**
     * 设置当前目录列表在已选择的状态
     */
    public void setSListToList() {

        for (int i = 0; i < mainFragmentModel.getList().size(); i++) {
            for (int j = 0; j < mainFragmentModel.getsList().size(); j++) {
                if (mainFragmentModel.getsList().get(j).get("fPath").equals(mainFragmentModel.getList().get(i).get("fPath")) &&
                        mainFragmentModel.getsList().get(j).get("fName").equals(mainFragmentModel.getList().get(i).get("fName"))) {
                    mainFragmentModel.getList().get(i).put("fSelect", 2);
                }
            }
        }

    }

    /**
     * 设置该路径列表是否已经滚动的值
     */
    public void setPathItemSelect(int hasScroll) {
        mainFragmentModel.getPathList().get(getPathListSize() - 1).setHasScroll(hasScroll);
    }

    /**
     * 获取该路径列表是否已经滚动的值
     */
    public int getPathItemSelect() {
        return mainFragmentModel.getPathList().get(getPathListSize() - 1).getHasScroll();
    }

    /**
     * 获取当前列表的状态
     */
    public boolean getIsShowSelectList() {
        return mainFragmentModel.isSelect();
    }

    /**
     * 获取当前列表的数量
     */
    public int getListSize() {
        return mainFragmentModel.getList().size();
    }

    public List<Map<String, Object>> getSList() {
        return mainFragmentModel.getsList();
    }

    /**
     * 在已选择列表清除指定选择
     * @param position
     */
    public void clearPositionSList(int position) {
        mainFragmentModel.getsList().remove(position);
    }

    /**
     * 判断是否已在选择列表
     * @param position
     * @return
     */
    public boolean isInSList(int position) {
        for (int i = 0; i < mainFragmentModel.getsList().size(); i++) {
            if (mainFragmentModel.getsList().get(i).get("fPath").equals(mainFragmentModel.getList().get(position).get("fPath")) &&
                    mainFragmentModel.getsList().get(i).get("fName").equals(mainFragmentModel.getList().get(position).get("fName"))) {
                return true;
            }
        }
        return false;
    }
}
