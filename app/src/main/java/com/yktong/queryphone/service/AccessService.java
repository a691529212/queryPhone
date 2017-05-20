package com.yktong.queryphone.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.yktong.queryphone.plugin.QueryPhone;
import com.yktong.queryphone.util.SupportUtil;

import java.util.List;

/**
 * Created by Vampire on 2017/5/20.
 */

public class AccessService extends AccessibilityService {
    private AccessibilityService mService;
    private SupportUtil supportUtil;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        String version = getVersion(this);
        supportUtil = new SupportUtil(version);
        Toast.makeText(mService, "微信版本 : " + version, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        if (event.equals(AccessibilityEvent.TYPE_VIEW_CLICKED) ||
                event.equals(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) ||
                event.equals(AccessibilityEvent.TYPE_WINDOWS_CHANGED)) {
            // TODO: 2017/5/20
//            QueryPhone.getInstence()

        }

    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 获取微信的版本号
     *
     * @param context
     * @return
     */
    public String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfoList) {
            if ("com.tencent.mm".equals(packageInfo.packageName)) {
                return packageInfo.versionName;
            }
        }
        return "6.5.4";
    }
}
