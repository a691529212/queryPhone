package com.yktong.queryphone.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.yktong.queryphone.MyApp;
import com.yktong.queryphone.bean.DbNumber;
import com.yktong.queryphone.plugin.QueryPhone;
import com.yktong.queryphone.tool.DBTool;
import com.yktong.queryphone.util.SPUtil;
import com.yktong.queryphone.util.ShellUtils;
import com.yktong.queryphone.util.SupportUtil;
import com.yktong.queryphone.value.SpValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2017/5/20.
 */

public class AccessService extends AccessibilityService implements SpValue {
    private AccessibilityService mService;
    private SupportUtil supportUtil;
    private ArrayList<DbNumber> dbNumbers;
    private ArrayList<String> tails;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        String version = getVersion(this);
        supportUtil = new SupportUtil(version);
        Log.d("AccessService", "微信版本 :" + version);
        Toast.makeText(mService, "微信版本 : " + version, Toast.LENGTH_SHORT).show();
        String city = (String) SPUtil.get(MyApp.getmContext(), QUERY_CITY, "");
        dbNumbers = DBTool.getInstance().queryNumberByCity(city);
        tails = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String tail;
            if (i < 10) {
                tail = "000" + i;
            } else if (i < 100) {
                tail = "00" + i;
            } else if (i < 1000) {
                tail = "0" + i;
            } else {
                tail = "" + i;
            }
            tails.add(tail);
        }
//        String[] shell = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI"};
//        ShellUtils.execCommand(shell, true);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        Log.d("AccessService", className);
        Log.d("AccessService", "event.getEventType():" + event.getEventType());
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                event.getEventType() == (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) ||
                event.getEventType() == (AccessibilityEvent.TYPE_WINDOWS_CHANGED)) {
            Log.d("AccessService", "here");
            QueryPhone.getInstence(dbNumbers, mService, tails, supportUtil).queryNum(className, event);
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
