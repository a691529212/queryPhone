package com.yktong.queryphone.plugin;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.yktong.queryphone.MyApp;
import com.yktong.queryphone.bean.DbNumber;
import com.yktong.queryphone.util.PerformClickUtils;
import com.yktong.queryphone.util.SPUtil;
import com.yktong.queryphone.util.SupportUtil;
import com.yktong.queryphone.util.WriteFileUtil;
import com.yktong.queryphone.value.SpValue;

import java.util.ArrayList;

import static com.yktong.queryphone.service.AccessService.sleep;

/**
 * Created by Vampire on 2017/5/20.
 */

public class QueryPhone implements SpValue {

    private static QueryPhone instence;
    private ArrayList<DbNumber> numberHeads;
    private ArrayList<String> tails;
    private AccessibilityService mService;
    private SupportUtil mSupportUtil;
    private String city;
    private int headIndex;
    private int index;
    private String num = "";
    private final String province;

    private QueryPhone(ArrayList<DbNumber> numberHeads, AccessibilityService mService, ArrayList<String> tails, SupportUtil supportUtil) {
        this.numberHeads = numberHeads;
        this.mService = mService;
        this.tails = tails;
        this.mSupportUtil = supportUtil;
        city = (String) SPUtil.get(MyApp.getmContext(), SpValue.QUERY_CITY, "");
        province = (String) SPUtil.get(MyApp.getmContext(), SpValue.QUERY_PROVINCE, "");
        if (city.isEmpty())
            Toast.makeText(mService, "请先加载数据", Toast.LENGTH_SHORT).show();
    }

    private void prepare() {
        headIndex = (int) SPUtil.get(MyApp.getmContext(), city, 0); //  --
        index = (int) SPUtil.get(MyApp.getmContext(), QUERT_INDEX, 0); // ++
    }

    public static QueryPhone getInstence(ArrayList<DbNumber> numberHeads, AccessibilityService mService, ArrayList<String> tails, SupportUtil supportUtil)

    {
        if (instence == null) {
            synchronized (QueryPhone.class) {
                if (instence == null) {
                    instence = new QueryPhone(numberHeads, mService, tails, supportUtil);
                }
            }
        }
        return instence;
    }

    public void queryNum(String className, AccessibilityEvent event) {
        if (className.equals(mSupportUtil.getLauncherUI())) {
            PerformClickUtils.findTextAndClick(mService, "搜索");
        } else if (className.equals(mSupportUtil.getSearchUI())) {
            prepare();
            PerformClickUtils.findViewIdAndClick(mService, mSupportUtil.getfTSMainUICleanEtId(), "清除");
            sleep(500);
            num = numberHeads.get(headIndex) + tails.get(index);
            PerformClickUtils.setText(mService, mSupportUtil.getSearchEditId(), num);
            sleep(1000);
            PerformClickUtils.findViewIdAndClick(mService, mSupportUtil.getSearchItemId());
        } else if (className.equals(mSupportUtil.getContactInfoUI())) {
            saveToFile();
        } else if (className.equals(mSupportUtil.getDialog())) {
            String hintId = "com.tencent.mm:id/bpn";
            String hint = PerformClickUtils.getText(mService, hintId);
            if (hint.equals("操作过于频繁，请稍后再试")) {
                saveToFile();
            } else {
                index = index++;
                if (index >= 10000) {
                    SPUtil.putAndApply(MyApp.getmContext(), QUERT_INDEX, 0);
                    SPUtil.putAndApply(MyApp.getmContext(), city, headIndex--);
                } else {
                    SPUtil.putAndApply(MyApp.getmContext(), QUERT_INDEX, index);
                }
                PerformClickUtils.performBack(mService);
                sleep(500);
            }
        }

    }

    private void saveToFile() {
        String info = province + "-" + city + "-" + num + "\n";
        WriteFileUtil.wrieFileByFileOutputStream(info, province + "-" + city);
        PerformClickUtils.performBack(mService);
        index = index++;
        if (index >= 10000) {
            SPUtil.putAndApply(MyApp.getmContext(), QUERT_INDEX, 0);
            SPUtil.putAndApply(MyApp.getmContext(), city, headIndex--);
        } else {
            SPUtil.putAndApply(MyApp.getmContext(), QUERT_INDEX, index);
        }
        sleep(500);
    }
}
