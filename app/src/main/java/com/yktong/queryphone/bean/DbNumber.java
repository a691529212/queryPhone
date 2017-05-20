package com.yktong.queryphone.bean;/**
 * Created by tarena on 2016/11/15.
 */

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * created by Vampire
 * on: 2016/11/15 上午9:52
 */
public class DbNumber {
    private static final String TAG = "DbNumber-vampire";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String number;
    private String province;
    private String city;

    public DbNumber(String city, String number, String province) {
        this.city = city;
        this.number = number;
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getProvince() {
        return province;
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public String toString() {
        return "DbNumber{" +
                "city='" + city + '\'' +
                ", id=" + id +
                ", number='" + number + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
