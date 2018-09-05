package com.iotc.eft.enums;

/**
 * @description 资产类别id
 */
public enum AssetEnum {
    BTC("c6d0c728-2624-429b-8e0d-d9d19b6592fa","BTC","Bitcoin"),
    ETH("43d61dcd-e413-450d-80b8-101d5e903357","ETH","Ethereum"),
    USDT("815b0b1a-2764-3736-8faa-42d694fa620a","USDT","USDT");

    private String assetId;
    private String type;
    private String name;


    AssetEnum(String assetId, String type, String name) {
        this.assetId = assetId;
        this.type = type;
        this.name = name;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
