package com.yktong.queryphone.data;

import cn.bmob.v3.BmobObject;

/**
 * Created by vampire on 2017/5/21.
 */

public class Number extends BmobObject {
    private String userName;
    private String premission;

    public Number(String tableName, String userName, String premission) {
        super(tableName);
        this.userName = userName;
        this.premission = premission;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPremission() {
        return premission;
    }

    public void setPremission(String premission) {
        this.premission = premission;
    }
}
