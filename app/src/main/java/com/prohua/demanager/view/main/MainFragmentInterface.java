package com.prohua.demanager.view.main;

/**
 * View层接口
 * Created by Deep on 2017/8/29 0029.
 */

public interface MainFragmentInterface {

    void initData();

    int getRecyclerViewItemScroll();

    void openFiles(String filesPath);

    void showRefreshAnimator();

    void stopRefreshAnimator();
}
