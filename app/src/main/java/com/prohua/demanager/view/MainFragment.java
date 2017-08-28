package com.prohua.demanager.view;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主界面
 * Created by Deep on 2017/8/28 0028.
 */

public class MainFragment extends SupportFragment implements DefaultAdapter.OnBindItemView {

    private List<Map<String, Object>> aList = new ArrayList<>();
    private String baseFile;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DefaultAdapter defaultAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        initView();
        initData();

        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        baseFile = GetFilesUtils.getInstance().getBasePath();

        new Thread(() -> {
            try {
                loadFolderList(baseFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void loadFolderList(String file) throws IOException {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(file);
        if(list!=null){
            Collections.sort(list, GetFilesUtils.getInstance().defaultOrder());
            aList.clear();
            for(Map<String, Object> map:list){
                String fileType=(String) map.get(GetFilesUtils.FILE_INFO_TYPE);
                Map<String,Object> gMap=new HashMap<String, Object>();
                if(map.get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)){
                    gMap.put("fIsDir", true);
                    gMap.put("fImg",R.mipmap.dir );
                    gMap.put("fInfo", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS)+"个文件夹和"+
                            map.get(GetFilesUtils.FILE_INFO_NUM_SONFILES)+"个文件");
                }else{
                    gMap.put("fIsDir", false);
                    if(fileType.equals("txt")||fileType.equals("text")){
                        gMap.put("fImg", R.mipmap.file);
                    }else{
                        gMap.put("fImg", R.mipmap.file);
                    }
                    gMap.put("fInfo","文件大小:"+GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put("fPath", map.get(GetFilesUtils.FILE_INFO_PATH));
                aList.add(gMap);
            }
        }else{
            aList.clear();
        }

        getActivity().runOnUiThread(() -> {
            defaultAdapter = new DefaultAdapter(getContext(), aList, R.layout.item_home_recycler);
            defaultAdapter.setOnBindItemView(MainFragment.this);
            recyclerView.setAdapter(defaultAdapter);
        });
    }

    @Override
    public void onBindItemViewHolder(DefaultViewHolder holder, int position) {
        holder.text(R.id.f_path,aList.get(position).get("fName").toString());
        holder.image(R.id.img, Integer.valueOf(aList.get(position).get("fImg").toString()));
    }

    @Override
    public boolean onBackPressedSupport() {
        getActivity().finish();
        return true;
    }
}
