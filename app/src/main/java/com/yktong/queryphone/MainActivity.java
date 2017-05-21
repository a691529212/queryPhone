package com.yktong.queryphone;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yktong.queryphone.bean.DbNumber;
import com.yktong.queryphone.tool.DBTool;
import com.yktong.queryphone.tool.DownDate;
import com.yktong.queryphone.util.SPUtil;
import com.yktong.queryphone.value.SpValue;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SpValue {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText provinceEt = (EditText) findViewById(R.id.province_et);
        String province = (String) SPUtil.get(MyApp.getmContext(), SpValue.QUERY_PROVINCE, "省份");
        provinceEt.setHint(province);
        String city = (String) SPUtil.get(MyApp.getmContext(), SpValue.QUERY_CITY, "城市");
        EditText cityEt = (EditText) findViewById(R.id.city_et);
        cityEt.setHint(city);
        Button downloadBtn = (Button) findViewById(R.id.down_btn);
//        String d = null;
//        Log.d("MainActivity", d);

        downloadBtn.setOnClickListener(v -> {
            if (!provinceEt.getText().toString().isEmpty()) {
                String url = "http://222.222.24.136:8180/api/wxuser/findPrefixByPC?province=" + provinceEt.getText().toString();
                if (!cityEt.getText().toString().isEmpty()) {
                    url += ("&city=" + cityEt.getText().toString());
                }
                Log.d("MainActivity", url);
                String finalUrl = url;
                DBTool.getInstance().queryNumberByCity(cityEt.getText().toString(), new DBTool.QueryListener() {
                    @Override
                    public <T> void onQueryListener(List<T> list) {
                        List<DbNumber> numberList = (List<DbNumber>) list;
                        if (numberList.isEmpty() || numberList.size() == 0) {
                            Log.d("MainActivity", "Thread.currentThread():" + Thread.currentThread());
                            DownDate.downData(finalUrl);
                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.QUERY_CITY, cityEt.getText().toString());
                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.QUERY_PROVINCE, provinceEt.getText().toString());
                        } else {
                            String s = "设置城市为: " + cityEt.getText().toString();
                            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
