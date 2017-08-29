package com.prohua.demanager.adapter;

import android.content.Context;
import android.view.View;

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
        holder.itemView.setOnClickListener(view -> onBindItemClick.onBindItemClick(view, position));
    }

    private OnBindItemView onBindItemView;

    public void setOnBindItemView(OnBindItemView onBindItemView) {
        this.onBindItemView = onBindItemView;
    }

    public interface OnBindItemView {
        void onBindItemViewHolder(DefaultViewHolder holder, int position);
    }

    private OnBindItemClick onBindItemClick;

    public void setOnBindItemClick(OnBindItemClick onBindItemClick) {
        this.onBindItemClick = onBindItemClick;
    }

    public interface OnBindItemClick {
        void onBindItemClick(View view, int position);
    }

}