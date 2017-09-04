package com.prohua.demanager.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * 继承万能适配器
 * Created by Deep on 2017/8/17 0017.
 */

public class DefaultAdapter extends DefaultRVAdapter {

    /**
     * 适配器
     *
     * @param context  上下文
     * @param list     列表数据
     * @param layoutId item布局id
     */
    public DefaultAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
    }

    /**
     * item的视图默认初始化
     *
     * @param holder   抽象向内传入的视图
     * @param position 当前的position
     */
    @Override
    protected void onBindItemViewHolder(DefaultViewHolder holder, final int position) {
        onBindItemView.onBindItemViewHolder(holder, position);

        // 如果不存在,则不绑定
        if (onBindItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBindItemClick.onBindItemClick(v, position);
                }
            });
        }

        // 如果不存在,则不绑定
        if (onBindItemLongClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onBindItemLongClick.onBindItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    /**
     * item视图
     */
    private OnBindItemView onBindItemView;

    /**
     * 绑定item视图
     *
     * @param onBindItemView 要绑定的接口
     */
    public void setOnBindItemView(OnBindItemView onBindItemView) {
        this.onBindItemView = onBindItemView;
    }

    /**
     * 视图接口
     */
    public interface OnBindItemView {
        void onBindItemViewHolder(DefaultViewHolder holder, int position);
    }

    /**
     * item点击事件
     */
    private OnBindItemClick onBindItemClick;

    /**
     * 绑定item点击事件
     *
     * @param onBindItemClick 要绑定的接口
     */
    public void setOnBindItemClick(OnBindItemClick onBindItemClick) {
        this.onBindItemClick = onBindItemClick;
    }

    /**
     * item点击事件接口
     */
    public interface OnBindItemClick {
        void onBindItemClick(View view, int position);
    }

    /**
     * item长点击事件
     */
    private OnBindItemLongClick onBindItemLongClick;

    /**
     * 绑定item长点击事件
     *
     * @param onBindItemLongClick 要绑定的接口
     */
    public void setOnBindItemLongClick(OnBindItemLongClick onBindItemLongClick) {
        this.onBindItemLongClick = onBindItemLongClick;
    }

    /**
     * item长点击事件接口
     */
    public interface OnBindItemLongClick {
        void onBindItemLongClick(View view, int position);
    }
}