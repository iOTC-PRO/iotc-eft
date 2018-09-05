package com.iotc.eft.bo;


import lombok.Data;

/**
 * 转账后的记录
 */
@Data
public class TransferRecord {

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
     * 交易订单id
     */
    private String snapshotId;
    /**
     * mixin交易订单id,第三方传入的
     */
    private String traceId;
    /**
     * 创建时间戳
     */
    private String createTimeStamp;
    /**
     * 备注信息
     */
    private String content;

    /**
     * 来自哪个账户的转账
     */
    private String opponenId;




}
