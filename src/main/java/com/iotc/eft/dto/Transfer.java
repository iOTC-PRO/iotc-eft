package com.iotc.eft.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description 转账响应信息
 */
@Data
public class Transfer {
    /**
     * 操作类型
     */
    private String type;
    /**
     * 快照id
     */
    private String snapshot_id;
    /**
     * 收款人用户id
     */
    private String counter_user_id;
    /**
     * 币种类型id
     */
    private String asset_id;
    /**
     * 转账金额，为负数，即扣减的
     */
    private String amount;
    /**
     * 订单跟踪id
     */
    private String trace_id;

    /**
     * 留言
     */
    private String memo;
    /**
     * 创建时间
     */
    private Date created_at;

}
