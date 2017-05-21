package com.yktong.queryphone;

import android.app.Application;
import android.content.Context;

import com.yktong.queryphone.tool.exceptioncatch.AbstractCrashReportHandler;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by Vampire on 2017/4/26.
 */

public class MyApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new AbstractCrashReportHandler(this);

        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("0face7ebf73094cfdcbc380eaece854c")
                .setConnectTimeout(30)
                .setFileExpiration(2500)
                .setUploadBlockSize(2048 * 1024)
                .build();
        Bmob.initialize(config);
    }

    public static Context getmContext() {
        return mContext;
    }
}
