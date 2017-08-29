package com.prohua.demanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.prohua.demanager.application.DeApplication;
import com.prohua.demanager.util.StatusBarUtil;
import com.prohua.demanager.view.LogoFragment;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 主入口
 * Created by Deep on 2017/8/28 0028.
 */
public class MainActivity extends SupportActivity {

    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;
    private String mStatusStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.darkMode(this);
        // 隐藏标题栏
        //if (getSupportActionBar() != null)
            //getSupportActionBar().hide();

        if (findFragment(LogoFragment.class) == null) {
            loadRootFragment(R.id.root_view, LogoFragment.newInstance());  // 加载根Fragment
        }

        updateConsole(DeApplication.cacheMsg.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestExternalStoragePermission();
        }
        DeApplication.msgDisplayListener = msg -> runOnUiThread(() -> updateConsole(msg));
    }

    /**
     * 如果本地补丁放在了外部存储卡中, 6.0以上需要申请读外部存储卡权限才能够使用. 应用内部存储则不受影响
     */

    private void requestExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    updateConsole("local external storage patch is invalid as not read external storage permission");
                }
                break;
            default:
        }
    }

    /**
     * 更新监控台的输出信息
     *
     * @param content 更新内容
     */
    private void updateConsole(String content) {
        mStatusStr += content + "\n";
        Log.i("hotfix", mStatusStr);
    }
}
