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
    protected void onBindItemViewHolder(DefaultViewHolder holder, final int position) {
        onBindItemView.onBindItemViewHolder(holder, position);
        if (onBindItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBindItemClick.onBindItemClick(v, position);
                }
            });
        }
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

    private OnBindItemLongClick onBindItemLongClick;

    public void setOnBindItemLongClick(OnBindItemLongClick onBindItemLongClick) {
        this.onBindItemLongClick = onBindItemLongClick;
    }

    public interface OnBindItemLongClick {
        void onBindItemLongClick(View view, int position);
    }
}