package com.prohua.demanager.adapter;

import android.content.Context;

import java.util.List;

/**
 * 继承万能适配器
 * Created by Deep on 2017/8/17 0017.
 */

public class DefaultAdapter extends DefaultRVAdapter {

    public DefaultAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void onBindItemViewHolder(DefaultViewHolder holder, int position) {
        onBindItemView.onBindItemViewHolder(holder,position);
    }

    private OnBindItemView onBindItemView;

    public void setOnBindItemView(OnBindItemView onBindItemView) {
        this.onBindItemView = onBindItemView;
    }

    public interface OnBindItemView {
        void onBindItemViewHolder(DefaultViewHolder holder, int position);
    }
}