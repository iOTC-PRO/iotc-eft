package com.iotc.eft.enums;

/**
 * @description mixin network记录类型
 */
public enum MixinSourceEnum {
    DEPOSIT("DEPOSIT_CONFIRMED","充值"),
    TRANSFER("TRANSFER_INITIALIZED","转账"),
    WITHDRAWAL("WITHDRAWAL_INITIALIZED","提现"),
    WITHDRAWAL_FEE("WITHDRAWAL_FEE_CHARGED","提现手续费");

    private String type;
    private String description;

    MixinSourceEnum(String type, String description) {
       this.type = type;
       this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
