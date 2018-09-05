package com.iotc.eft.config;


import com.iotc.eft.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description mixin api相关配置信息
 */
@Component
public class MixinConfig {

    @Value("${mixin.uid}")
    private String mixin_uid;

    @Value("${mixin.sid}")
    private String mixin_sid;

    @Value("${mixin.pin}")
    private String mixin_pin;

    public String getMixin_privateKey() {

        return CacheUtils.getPrivateKey();
    }

    public String getMixin_uid() {
        return mixin_uid;
    }

    public String getMixin_sid() {
        return mixin_sid;
    }

    public String getMixin_pinToken() {
        return CacheUtils.getPinToken();
    }

    public String getMixin_pin() {
        return mixin_pin;
    }
}
