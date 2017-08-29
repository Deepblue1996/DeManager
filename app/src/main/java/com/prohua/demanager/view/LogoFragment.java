package com.prohua.demanager.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prohua.demanager.R;
import com.prohua.demanager.view.main.MainFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 启动图
 * Created by Deep on 2017/8/28 0028.
 */

public class LogoFragment extends SupportFragment {

    private boolean okStart = true;

    public static LogoFragment newInstance() {
        return new LogoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo, container, false);

        // 这里我做了个判断，如果用户在启动界面显示途中退出，就不执行主界面
        new Handler().postDelayed(() -> {
            if (okStart)
                start(MainFragment.newInstance());
        }, 1000);

        return view;
    }


}
