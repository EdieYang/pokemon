package com.pokepet.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 2018/11/19.
 */
public enum DefaultSettingCode {

    DEFAULT_FLAG("0", "默认有效删除位");

    private String code;

    private String message;

    DefaultSettingCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (DefaultSettingCode item : DefaultSettingCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static String getCode(String name) {
        for (DefaultSettingCode item : DefaultSettingCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

    //校验重复的code值
    public static void main(String[] args) {
        DefaultSettingCode[] defaultSettingCodes = DefaultSettingCode.values();
        List<String> codeList = new ArrayList<String>();
        for (DefaultSettingCode defaultSettingCode : defaultSettingCodes) {
            if (codeList.contains(defaultSettingCode.code)) {
                System.out.println(defaultSettingCode.code);
            } else {
                codeList.add(defaultSettingCode.code());
            }
        }
    }
}
