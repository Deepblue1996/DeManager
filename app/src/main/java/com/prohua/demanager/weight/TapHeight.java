package com.prohua.demanager.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.prohua.demanager.util.StatusBarUtil;

/**
 * Created by Deep on 2017/8/29 0029.
 */

public class TapHeight extends LinearLayout {

    public TapHeight(Context context) {
        super(context);
        setHeight();
    }

    public TapHeight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setHeight();
    }

    public TapHeight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHeight();
    }

    private void setHeight() {
        setPadding(0, StatusBarUtil.getStatusBarHeight(getContext()),0,0);
    }
}
