package com.prohua.demanager.adapter;

import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用Holder
 * Created by Deep on 2017/8/17 0017.
 */

public class DefaultViewHolder extends RecyclerView.ViewHolder {

    /**
     * 构造器
     *
     * @param view
     */
    public DefaultViewHolder(View view) {
        super(view);
    }

    /**
     * 控件基于View,方便转换,调用
     *
     * @param id 控件的id
     * @return View
     */
    private View findViewById(int id) {
        return id == 0 ? itemView : itemView.findViewById(id);
    }

    /**
     * 根据id,设置TextView内容
     *
     * @param id 控件的id
     * @return 当前view, 链式
     */
    public DefaultViewHolder text(int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    /**
     * 根据id,设置Res到TextView内容
     *
     * @param id        控件的id
     * @param stringRes Res.string.id
     * @return 当前view, 链式
     */
    public DefaultViewHolder text(int id, @StringRes int stringRes) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    /**
     * 根据id,设置Res到TextView颜色
     *
     * @param id      控件的id
     * @param colorId Res.color.id
     * @return 当前view, 链式
     */
    public DefaultViewHolder textColorId(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    /**
     * 根据id,设置Res到ImageView资源
     *
     * @param id      控件的id
     * @param imageId Res.mipmap.id
     * @return 当前view, 链式
     */
    public DefaultViewHolder image(int id, int imageId) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }

}
