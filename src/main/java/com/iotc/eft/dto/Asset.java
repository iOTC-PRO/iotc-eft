package com.iotc.eft.dto;


import lombok.Data;

/**
 * 链信息，即资产信息
 */
@Data
public class Asset {
    /**
     * 币种类型id
     */
    private String asset_id;
    /**
     * 链id
     */
    private String chain_id;
    /**
     * 币种icon
     */
    private String icon_url;
    /**
     * 名称
     */
    private String name;
    /**
     * 币种类型
     */
    private String symbol;
    /**
     * 交易类型
     */
    private String type;
}
