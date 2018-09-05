package com.iotc.eft.dto;

import lombok.Data;

/**
 * mixin钱包充值地址信息
 */
@Data
public class AssetAddress {
    /**
     * 类型
     */
    private String type;
    /**
     * 币种类型id
     */
    private String asset_id;
    /**
     * 链id
     */
    private String chain_id;
    /**
     * 币种描述
     */
    private String symbol;
    /**
     * 币种名称
     */
    private String  name;
    /**
     * icon地址
     */
    private String icon_url;
    /**
     * 价格
     */
    private String balance;

    /**
     * 用户充值地址
     */
    private String public_key;
    /**
     * 对btc价格
     */
    private String price_btc;
    /**
     * 对usd价格
     */
    private String price_usd;

}
