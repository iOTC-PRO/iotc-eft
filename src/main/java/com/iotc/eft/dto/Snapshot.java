package com.iotc.eft.dto;

import lombok.Data;

/**
 * mixin交易记录
 */
@Data
public class Snapshot {
    /**
     * 交易金额
     */
    private String amount;
    /**
     * 资产信息
     */
    private Asset asset;
    /**
     * 创建时间
     */
    private String created_at;
    /**
     * 内容
     */
    private String data;
    /**
     * 交易订单id
     */
    private String snapshot_id;
    /**
     * 用户user_id
     */
    private String user_id;

    /**
     * 转账时传入的订单id
     */
    private String trace_id;

    /**
     * 交易类型TRANSFER_INITIALIZED为转账
     */
    private String source;
    /**
     * 交易类型
     */
    private String type;

    /**
     * 对方id，即转出到哪的id和从哪来的id
     */
    private String opponent_id;
}
