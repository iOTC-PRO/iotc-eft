package com.iotc.eft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iotc.eft.bo.ReturnResult;
import com.iotc.eft.bo.TransferInfo;
import com.iotc.eft.bo.TransferRecord;
import com.iotc.eft.client.MixinApiClient;
import com.iotc.eft.config.MixinConfig;
import com.iotc.eft.dto.*;
import com.iotc.eft.dto.Error;
import com.iotc.eft.enums.MixinSourceEnum;
import com.iotc.eft.enums.StatusCode;
import com.iotc.eft.service.MixinApiService;
import com.iotc.eft.utils.AssetUtils;
import com.iotc.eft.utils.MixinApiUtils;
import com.iotc.eft.vo.MixinTransfer;
import com.iotc.eft.vo.TransferParam;
import com.iotc.eft.vo.TransferRecordParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description mixin业务处理
 */
@Service
@Slf4j
public class MixinApiServiceImpl implements MixinApiService {

    @Autowired
    private MixinApiClient mixinApiClient;

    @Resource
    private MixinConfig mixinConfig;


    @Override
    public ReturnResult<TransferInfo> transfer(TransferParam transferParam) {
        ReturnResult<TransferInfo> returnResult =  new ReturnResult<>();
        if(transferParam == null){
            returnResult.setCode(StatusCode.PARAMS_LACK.getCode());
            returnResult.setMessage(StatusCode.PARAMS_LACK.getMsg());
            return returnResult;
        }

        String assetType = transferParam.getAssetType();
        String toAccountId = transferParam.getToAccountId();
        String amount = transferParam.getAmount();
        String orderId = transferParam.getOrderId();
        String content = transferParam.getContent();
        if(StringUtils.isBlank(content) || StringUtils.isBlank(assetType) || StringUtils.isBlank(toAccountId)
                || StringUtils.isBlank(amount) || StringUtils.isBlank(orderId)){
            returnResult.setCode(StatusCode.PARAMS_LACK.getCode());
            returnResult.setMessage(StatusCode.PARAMS_LACK.getMsg());
            return returnResult;
        }
        log.info("transfer param,assetType:{},toAccountId:{},amount:{},orderId:{}", assetType,
                toAccountId,amount,orderId);

        //校验是否存在此币种
        String assetId = AssetUtils.getAssetId(assetType);
        if(StringUtils.isBlank(assetId)){
            returnResult.setCode(StatusCode.ASSET_UNEXSIT.getCode());
            returnResult.setMessage(StatusCode.ASSET_UNEXSIT.getMsg());
            return returnResult;
        }

        //解析转账密码
        String pin = MixinApiUtils.getEncryptedPin(mixinConfig);

        MixinTransfer mixinTransfer = new MixinTransfer();
        mixinTransfer.setCounter_user_id(toAccountId);//收款账号
        mixinTransfer.setAmount(amount);//转账金额
        mixinTransfer.setTrace_id(orderId);//交易跟踪id
        mixinTransfer.setAsset_id(assetId);//币种资产id
        mixinTransfer.setPin(pin);//转账密码
        mixinTransfer.setMemo(content);//备注信息

        Response<Transfer> transferResponse = null;

        try {
            String payLoad = JSONObject.toJSONString(mixinTransfer);
            transferResponse = mixinApiClient.transfer(payLoad);
            log.info("mixin transfer response:{}",transferResponse);
        }catch (Exception e){
            log.error("mixin transfer,param:{},error:{}",transferParam,e);
            returnResult.setCode(StatusCode.REQUEST_TIMEOUT.getCode());
            returnResult.setMessage(StatusCode.REQUEST_TIMEOUT.getMsg());
            return returnResult;
        }


        if(transferResponse == null){//请求失败
            log.error("mixin transfer failure,response is null,param:{}",transferParam);
            returnResult.setCode(StatusCode.REQUEST_TIMEOUT.getCode());
            returnResult.setMessage(StatusCode.REQUEST_TIMEOUT.getMsg());
            return returnResult;
        }


        Transfer transfer = transferResponse.getData();
        if(transfer == null){
            Error error = transferResponse.getError();
            log.error("mixin transfer failure,error:{}",error);
            returnResult.setCode(error.getCode());
            returnResult.setMessage(error.getDescription());
            return returnResult;
        }

        log.info("mixin transfer success,message:{}",transfer);
        //请求正常，构建返回数据
        String tmpAssetType = AssetUtils.getAssetType(transfer.getAsset_id());//确认币种一致
        TransferInfo transferInfo =  new TransferInfo();
        transferInfo.setAssetType(tmpAssetType);
        transferInfo.setSnapshotId(transfer.getSnapshot_id());
        transferInfo.setToAccountId(transfer.getCounter_user_id());
        String tmpAmount = transfer.getAmount();
        if(StringUtils.isNotBlank(tmpAmount)){
            tmpAmount = tmpAmount.replaceAll("-","");
        }
        transferInfo.setAmount(tmpAmount);
        transferInfo.setTraceId(transfer.getTrace_id());

        returnResult.setCode(StatusCode.SUCCESS.getCode());
        returnResult.setMessage(StatusCode.SUCCESS.getMsg());
        returnResult.setData(transferInfo);

        return returnResult;
    }

    @Override
    public ReturnResult<Map<String,Object>> pullTransfeInRecord(TransferRecordParam transferRecordParam) {
        ReturnResult<Map<String,Object>> returnResult =  new ReturnResult<>();
        if(transferRecordParam == null){
            returnResult.setCode(StatusCode.PARAMS_LACK.getCode());
            returnResult.setMessage(StatusCode.PARAMS_LACK.getMsg());
            return returnResult;
        }

        String assetType = transferRecordParam.getAssetType();
        String endTimeStamp = transferRecordParam.getEndTimeStamp();
        String limit = transferRecordParam.getLimit();

        //要求必须给定币种,防止拉取不相关数据太多
        if(StringUtils.isBlank(assetType)){
            returnResult.setCode(StatusCode.PARAMS_LACK.getCode());
            returnResult.setMessage(StatusCode.PARAMS_LACK.getMsg());
            return returnResult;
        }

        String assetId = AssetUtils.getAssetId(assetType);
        if(StringUtils.isBlank(assetId)){
            returnResult.setCode(StatusCode.ASSET_UNEXSIT.getCode());
            returnResult.setMessage(StatusCode.ASSET_UNEXSIT.getMsg());
            return returnResult;
        }

        if(StringUtils.isBlank(limit)){
            limit = "100";//默认100条
        }

        String offset = null;

        if(StringUtils.isNotBlank(endTimeStamp)){
            Date date = new Date(Long.parseLong(endTimeStamp));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
            offset = format.format(date);
        }

        log.info("pull mixin recorde,assetType:{},offset:{},limit:{}",assetType,offset,limit);

        Response<List<Snapshot>> listResponse = null;
        try {
            listResponse = mixinApiClient.snapshots(limit,offset,assetId);
        }catch (Exception e){
            log.error("pull mixin recorde,error:{}",e);
            returnResult.setCode(StatusCode.REQUEST_TIMEOUT.getCode());
            returnResult.setMessage(StatusCode.REQUEST_TIMEOUT.getMsg());
            return returnResult;
        }

        if(listResponse == null){//请求失败
            log.error("pull mixin recorde failure,response is null,assetType:{}",assetType);
            returnResult.setCode(StatusCode.REQUEST_TIMEOUT.getCode());
            returnResult.setMessage(StatusCode.REQUEST_TIMEOUT.getMsg());
            return returnResult;
        }


        List<Snapshot> snapshotList = listResponse.getData();
        if(snapshotList == null || snapshotList.size() == 0){
            Error error = listResponse.getError();
            log.error("pull mixin recorde failure,error:{}",error);
            returnResult.setCode(error.getCode());
            returnResult.setMessage(error.getDescription());
            return returnResult;
        }

        Map<String,Object> transferRecorMap = exchangeRecord(snapshotList);

        returnResult.setCode(StatusCode.SUCCESS.getCode());
        returnResult.setMessage(StatusCode.SUCCESS.getMsg());
        returnResult.setData(transferRecorMap);

        return returnResult;
    }

    //转换记录
    private Map<String,Object> exchangeRecord(List<Snapshot> snapshotList){
        List<TransferRecord> transferRecordList = new ArrayList<>();
        Long endTimeStap = null;
          for(Snapshot snapshot :snapshotList){

            Date createAtDate = new Date();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                String created = snapshot.getCreated_at();
                created = created.substring(0,23);//需要截取，要不格式化有问题
                createAtDate = format.parse(created);
            } catch (Exception e) {
                log.error("format create time error:{}",e);
            }
            endTimeStap = createAtDate.getTime();

            String userId = snapshot.getUser_id();
            if(StringUtils.isBlank(userId)){
                continue;
            }

            log.info("pull record of user,snapshot:{}",snapshot);
            Asset asset = snapshot.getAsset();
            String assetType = AssetUtils.getAssetType(asset.getAsset_id());
            if(StringUtils.isBlank(assetType)){
                continue;
            }

            //只拉取转账的数据，在这里不关注充值提现等数据
            String source = snapshot.getSource();
            if(StringUtils.isBlank(source) || !MixinSourceEnum.TRANSFER.getType().equals(source)){
                continue;
            }

            String amount = snapshot.getAmount();
            if(StringUtils.isBlank(amount)){
                continue;
            }

            //负数，为转出，不检测转出记录
            boolean tmpFlag = amount.startsWith("-");
            if(tmpFlag){
                continue;
            }


            String data = snapshot.getData();
            if(StringUtils.isBlank(data)){
                log.error("the record data is null");
                continue;
            }

            TransferRecord transferRecord = new TransferRecord();
            transferRecord.setToAccountId(userId);
            transferRecord.setSnapshotId(snapshot.getSnapshot_id());
            transferRecord.setAmount(snapshot.getAmount());
            transferRecord.setAssetType(assetType);
            transferRecord.setCreateTimeStamp(createAtDate.getTime()+"");
            transferRecord.setTraceId(snapshot.getTrace_id());
            transferRecord.setOpponenId(snapshot.getOpponent_id());
            transferRecord.setContent(data);

            transferRecordList.add(transferRecord);

        }

        Map<String,Object> result = new HashMap<>();

        if(transferRecordList !=null&&
                endTimeStap != null){
            result.put("recordList",transferRecordList);
            result.put("lastTimestamp",endTimeStap+"");
        }

        return result;
    }
}
