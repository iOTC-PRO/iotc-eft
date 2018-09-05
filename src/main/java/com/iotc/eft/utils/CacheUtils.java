package com.iotc.eft.utils;


/**
 * 用于缓存解析完成私钥和pin_token
 */
public class CacheUtils {
    //mixin私钥
    private static String privateKey;

    //mixin pintoken
    private static String pinToken;

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        CacheUtils.privateKey = privateKey;
    }

    public static String getPinToken() {
        return pinToken;
    }

    public static void setPinToken(String pinToken) {
        CacheUtils.pinToken = pinToken;
    }
}
