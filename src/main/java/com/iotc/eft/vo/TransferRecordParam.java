package com.iotc.eft.vo;


import lombok.Data;

/**
 * 转账记录参数
 */
@Data
public class TransferRecordParam {

    /**
     * 币种类型，BTC、ETH、USDT
     */
    private String assetType;

    /**
     * 结束时间戳，mixin是时间点往前推
     */
    private String endTimeStamp;

    /**
     * 条数限制
     */
    private String limit;

}
