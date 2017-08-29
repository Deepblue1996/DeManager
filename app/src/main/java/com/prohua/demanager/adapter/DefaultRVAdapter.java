package com.prohua.demanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 默认万能适配器
 * Created by Deep on 2017/8/17 0017.
 */

public abstract class DefaultRVAdapter<T> extends RecyclerView.Adapter<DefaultViewHolder> {

    // 内容数据
    private List<T> list;
    // item的布局id
    private int layoutId;
    // 上下文
    private Context context;


    public DefaultRVAdapter(Context context, List<T> list, int layoutId) {
        this.list = list;
        this.layoutId = layoutId;
        this.context = context;
    }

    // Item创建视图
    @Override
    public DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    // 将数据绑定到Item的视图
    protected abstract void onBindItemViewHolder(DefaultViewHolder holder, int position);
    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {
        onBindItemViewHolder(holder, position);
    }

    // 返回总数量
    @Override
    public int getItemCount() {
        return list.size();
    }
}