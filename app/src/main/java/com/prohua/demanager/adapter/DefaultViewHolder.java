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

    public DefaultViewHolder(View view) {
        super(view);
    }

    private View findViewById(int id) {
        return id == 0 ? itemView : itemView.findViewById(id);
    }

    public DefaultViewHolder text(int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public DefaultViewHolder text(int id, @StringRes int stringRes) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    public DefaultViewHolder textColorId(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    public DefaultViewHolder image(int id, int imageId) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }
}
