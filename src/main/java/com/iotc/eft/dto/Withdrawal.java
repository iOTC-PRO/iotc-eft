package com.iotc.eft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 提现响应结果
 */
@Data
public class Withdrawal {
    /**
     * 类型
     */
    private String type;

    /**
     * 快照id
     */
    private String snapshot_id;

    /**
     * 事务，axt..ze
     */
    private String transaction_hash;

    /**
     * 币种类型id
     */
    private String asset_id;
    /**
     * 提现金额，为负数，即扣除
     */
    private String amount;
    /**
     * 订单跟踪id
     */
    private String trace_id;
    /**
     * 备注
     */
    private String memo;
    /**
     * 创建时间
     */
    private Date created_at;
}
