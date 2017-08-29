package com.prohua.demanager.view.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prohua.demanager.R;
import com.prohua.demanager.adapter.DefaultAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.demanager.util.MimeUtils.getMIMEType;

/**
 * 主界面
 * Created by Deep on 2017/8/28 0028.
 */

public class MainFragment extends SupportFragment implements MainFragmentInterface {

    @BindView(R.id.recycler_header_view)
    RecyclerView recyclerViewHeader;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.have_not_file)
    LinearLayout haveNotFile;

    private MainFragmentPresenter mainFragmentPresenter;
    private DefaultAdapter headerAdapter;
    private DefaultAdapter itemAdapter;

    // 再点一次退出, 程序间隔时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

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

        // 初始化视图
        initView();
        // 初始化数据
        initData();

        return view;
    }

    /**
     * 初始化View控件
     */
    private void initView() {

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHeader.setLayoutManager(linearLayoutManager2);
    }

    /**
     * 初始化Presenter层
     */
    public void initData() {
        mainFragmentPresenter = new MainFragmentPresenter(this);
    }

    /**
     * 设置适配器, 必须UI线程,所以我使用了EventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setDefaultAdapter(MainFragmentEvent mainFragmentEvent) {

        if (itemAdapter == null) {
            itemAdapter = new DefaultAdapter(getContext(), mainFragmentEvent.getList(), R.layout.item_home_recycler);
            itemAdapter.setOnBindItemView((holder, position) -> {
                holder.text(R.id.f_path, mainFragmentPresenter.getPositionName(position));
                holder.image(R.id.img, Integer.valueOf(mainFragmentPresenter.getPositionImg(position)));
            });
            itemAdapter.setOnBindItemClick((view, position) -> mainFragmentPresenter.innerName(position));
            recyclerView.setAdapter(itemAdapter);
        } else {
            itemAdapter.notifyDataSetChanged();
        }

        if(mainFragmentEvent.getList().size() == 0) {
            haveNotFile.setVisibility(View.VISIBLE);
        } else {
            haveNotFile.setVisibility(View.GONE);
        }

        // 滚动到指定位置
        recyclerView.scrollBy(0, mainFragmentPresenter.getPathScroll());

        if (headerAdapter == null) {
            headerAdapter = new DefaultAdapter(getContext(), mainFragmentEvent.getPlist(), R.layout.item_home_header_recycler);

            headerAdapter.setOnBindItemView((holder, position) ->
                    holder.text(R.id.path_name, mainFragmentPresenter.getPathPosition(position))
            );
            headerAdapter.setOnBindItemClick((view, position) -> {

            });

            recyclerViewHeader.setAdapter(headerAdapter);
        } else {
            if (mainFragmentEvent.getPosition() == 0) {
                headerAdapter.notifyDataSetChanged();
            } else if (mainFragmentEvent.getPosition() == 1) {
                headerAdapter.notifyItemInserted(mainFragmentPresenter.getPathListSize());
            } else if (mainFragmentEvent.getPosition() == -1) {
                headerAdapter.notifyItemRemoved(mainFragmentPresenter.getPathListSize());
            }
        }
        recyclerViewHeader.scrollToPosition(mainFragmentPresenter.getPathListSize() - 1);

    }

    /**
     * 处理回退事件
     *
     * @return true
     */
    @Override
    public boolean onBackPressedSupport() {

        if (mainFragmentPresenter.getPathListSize() == 1) {
            // LogoFragment
            if (_mActivity.getSupportFragmentManager().getBackStackEntryCount() > 2) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    // 杀死线程,完全退出
                    //android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                    //System.exit(0);
                    _mActivity.finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    // TODO: UI提示
                    Toast.makeText(getContext(), "再按一次,确定退出", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            mainFragmentPresenter.beforePath();
        }
        return true;
    }

    /**
     * 滑动的距离
     *
     * @return height
     */
    public int getRecyclerViewItemScroll() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    //打开文件时调用
    public void openFiles(String filesPath) {
        Uri uri = Uri.parse("file://" + filesPath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String type = getMIMEType(filesPath);
        intent.setDataAndType(uri, type);
        if (!type.equals("*/*")) {
            try {
                startActivity(intent);
            } catch (Exception e) {
                startActivity(showOpenTypeDialog(filesPath));
            }
        } else {
            startActivity(showOpenTypeDialog(filesPath));
        }
    }

    //显示打开方式
    public void show(String filesPath){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(showOpenTypeDialog(filesPath));
    }

    public static Intent showOpenTypeDialog(String param) {
        Log.e("ViChildError", "showOpenTypeDialog");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }
}
