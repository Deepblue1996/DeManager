package com.prohua.demanager.view.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prohua.demanager.R;
import com.prohua.demanager.adapter.DefaultAdapter;
import com.prohua.demanager.adapter.DefaultViewHolder;
import com.prohua.demanager.util.GetFilesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主界面
 * Created by Deep on 2017/8/28 0028.
 */

public class MainFragment extends SupportFragment implements DefaultAdapter.OnBindItemView, MainFragmentInterface {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DefaultAdapter defaultAdapter;

    private MainFragmentPresenter mainFragmentPresenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // 注册EventBus
        EventBus.getDefault().register(this);
        // 绑定ButterKnife
        ButterKnife.bind(this, view);

        initView();
        initData();

        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void initData() {

        mainFragmentPresenter = new MainFragmentPresenter(this);

        new Thread(() -> {
            try {
                mainFragmentPresenter.loadFolderList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * 设置适配器
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setDefaultAdapter(MainFragmentEvent mainFragmentEvent) {
        defaultAdapter = new DefaultAdapter(getContext(), mainFragmentEvent.getList(), R.layout.item_home_recycler);
        defaultAdapter.setOnBindItemView(MainFragment.this);
        recyclerView.setAdapter(defaultAdapter);
    }

    @Override
    public void onBindItemViewHolder(DefaultViewHolder holder, int position) {
        holder.text(R.id.f_path, mainFragmentPresenter.getPositionName(position));
        holder.image(R.id.img, Integer.valueOf(mainFragmentPresenter.getPositionImg(position)));
    }

    @Override
    public boolean onBackPressedSupport() {
        getActivity().finish();
        return true;
    }
}
