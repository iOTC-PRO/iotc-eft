package com.iotc.eft.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description mixin用户信息
 */
@Data
public class MixinUser {
    /**
     * 类型
     */
    private String type;
    /**
     * mixin用户id
     */
    private String user_id;
    /**
     * mxin app用户iP
     */
    private String identity_number;
    /**
     * mixin上的用户名，创建的时候设置
     */
    private String full_name;

    /**
     * mixn上的头像
     */
    private String avatar_url;

    /**
     * 好友关系
     */
    private String relationship;

    private String mute_until;
    /**
     * 创建时间
     */
    private Date created_at;

    private boolean is_verified;
    /**
     * 用于签名的
     */
    private String session_id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用于解密转账密码的token
     */
    private String pin_token;
    /**
     * 邀请码
     */
    private String invitation_code;

    private String code_id;

    private String code_url;

    private boolean has_pin;

    private String receive_message_source;

}