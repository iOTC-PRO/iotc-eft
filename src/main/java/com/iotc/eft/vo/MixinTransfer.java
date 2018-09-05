package com.iotc.eft.vo;


import lombok.Data;

/**
 * @description 转账请求信息
 */
@Data
public class MixinTransfer {
    /**
     * 币种类型id
     */
    private String asset_id;
    /**
     * 收款人用户id
     */
    private String counter_user_id;
    /**
     * 转账金额，为正数
     */
    private String amount;
    /**
     * 加密的pin
     */
    private String pin;
    /**
     * 订单跟踪id,随机UUID
     */
    private String trace_id;
    /**
     * 备注
     */
    private String memo;
}
