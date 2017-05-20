package com.yktong.queryphone;

import android.app.Application;
import android.content.Context;

/**
 * Created by Vampire on 2017/4/26.
 */

public class MyApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext =this;
    }

    public static Context getmContext() {
        return mContext;
    }
}
