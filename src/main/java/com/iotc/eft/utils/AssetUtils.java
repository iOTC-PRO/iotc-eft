package com.iotc.eft.utils;


import com.iotc.eft.enums.AssetEnum;
import org.apache.commons.lang.StringUtils;

/**
 * @description 币种id映射工具
 */
public class AssetUtils {


    /**
     * @descritpion 获取币种类型
     * @param assetId 币种对应的资产id
     * @return 返回币种名称
     */
    public static String getAssetType(String assetId){
        if(StringUtils.isBlank(assetId)){
            return null;
        }

        AssetEnum[] assetEnums = AssetEnum.values();
        if(assetEnums == null || assetEnums.length == 0 ){
            return null;
        }

        for(AssetEnum assetEnum:assetEnums){
            String tmpAssetId = assetEnum.getAssetId();
            if(tmpAssetId.equals(assetId)){
                return assetEnum.getType();
            }
        }

        return null;

    }


    /**
     * @descritpion 获取币种id
     * @param assetType 币种对应的类型，如BTC/ETH/USDT
     * @return 返回币种id
     */
    public static String getAssetId(String assetType){
        if(StringUtils.isBlank(assetType)){
            return null;
        }

        AssetEnum[] assetEnums = AssetEnum.values();
        if(assetEnums == null || assetEnums.length == 0 ){
            return null;
        }

        for(AssetEnum assetEnum:assetEnums){
            String tmpAssetType = assetEnum.getType();
            if(tmpAssetType.equals(assetType)){
                return assetEnum.getAssetId();
            }
        }

        return null;

    }



}
