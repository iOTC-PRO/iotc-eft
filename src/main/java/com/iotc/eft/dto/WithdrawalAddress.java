package com.iotc.eft.dto;


import lombok.Data;
import java.util.Date;

/**
 * 添加提现地址和读取提现地址响应
 */
@Data
public class WithdrawalAddress {
    /**
     * 类型
     */
    private String type;
    /**
     * 体现映射mixin的地址
     */
    private String address_id;

    /**
     * 币种类型地址
     */
    private String asset_id;

    /**
     * 实际公链的提现地址
     */
    private String public_key;
    /**
     * 备注
     */
    private String label;

    /**
     * 创建时间
     */
    private Date updated_at;
}
