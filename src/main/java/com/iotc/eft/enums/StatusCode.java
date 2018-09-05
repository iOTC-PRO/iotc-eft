package com.iotc.eft.enums;


/**
 * 状态码
 */
public enum StatusCode {

    SUCCESS("0", "成功"),
    PARAMS_LACK("1", "参数缺失"),
    SIGN_NOT_CORRECT("2", "签名失败"),
    PARAM_VALUE_ERROR("3", "参数值错误"),
    ERROR("4", "服务内部错误"),
    REQUEST_TIMEOUT("5","mixin请求超时"),
    ASSET_UNEXSIT("6","币种资产不存在")
    ;

    private String code;
    private String msg;

    StatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
