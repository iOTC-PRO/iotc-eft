package com.iotc.eft.bo;


import lombok.Data;

/**
 * 转账后的信息
 */
@Data
public class TransferInfo {

    /**
     * 网络快照id
     */
    private String snapshotId;

    /**
     * 收款账户的mixinId
     */
    private String toAccountId;

    /**
     * 币种类型，BTC、ETH、USDT
     */
    private String assetType;

    /**
     * 转账数值
     */
    private String amount;

    /**
     * mixin交易订单id,第三方传入的
     */
    private String traceId;




}
