package com.yktong.queryphone.tool;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.yktong.queryphone.MyApp;
import com.yktong.queryphone.bean.DbNumber;
import com.yktong.queryphone.tool.DBTool;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Vampire on 2017/4/26.
 */

public class DownDate {
    public static void downData(String url) {
        Log.d(TAG, "down");
        File path = Environment.getExternalStorageDirectory();
        OkHttpClient okHttpUtil = new OkHttpClient.Builder()
                //设置缓存位置 以及缓存大小
                .cache(new Cache(path, 10 * 1024 * 1024))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        RequestBody requestBody = builder.build();
        final Request request = new Request
                .Builder().url(url)
                .post(requestBody)
                .build();
        okHttpUtil.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post("号段保存成功");
                Looper.prepare();
                Log.e(TAG, "请求失败");
                Toast.makeText(MyApp.getmContext(), "网络不佳,请稍后再试", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim().toString();
                Log.d(TAG, Thread.currentThread().getName());
                List<String> request = Arrays.asList(result.split("\n"));
                List<DbNumber> dbNumbers = new ArrayList<DbNumber>();
                for (String s : request) {
                    Log.d(TAG, s);
                    String[] split = s.split(",");
                    // TODO: 2016/12/16  数组越界
                    for (int i = 0; i < split.length; i++) {
                        DbNumber dbNumber = new DbNumber(split[1], split[2], split[0]);
                        dbNumbers.add(dbNumber);
                    }
                }
                DBTool.getInstance().instertNumber(dbNumbers);

            }
        });
    }
}
