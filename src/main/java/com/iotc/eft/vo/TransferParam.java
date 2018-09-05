package com.iotc.eft.vo;


import lombok.Data;

/**
 * 转账请求参数
 */
@Data
public class TransferParam {

    /**
     * 币种类型，BTC、ETH、USDT
     */
    private String assetType;

    /**
     * 收款账户的mixinId
     */
    private String toAccountId;

    /**
     * 转账数值
     */
    private String amount;

    /**
     * 发生交易的记录id，本地系统的交易记录id，UUID
     */
    private String orderId;

    /**
     * 用户加密相关信息，json格式加密，包含mobile\code\partnerId\address
     */
    private String content;


}
