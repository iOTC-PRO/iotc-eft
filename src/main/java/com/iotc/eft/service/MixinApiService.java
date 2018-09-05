package com.iotc.eft.service;


import com.iotc.eft.bo.ReturnResult;
import com.iotc.eft.bo.TransferInfo;
import com.iotc.eft.bo.TransferRecord;
import com.iotc.eft.vo.TransferParam;
import com.iotc.eft.vo.TransferRecordParam;

import java.util.List;
import java.util.Map;

/**
 * @description 处理与mixin交互的相关逻辑
 */
public interface MixinApiService {

    /**
     * @description 转账
     * @param transferParam 转账参数
     * @return 返回转账信息
     */
    ReturnResult<TransferInfo> transfer(TransferParam transferParam);


    /**
     * @description 拉取转账记录,转入的,mixin记录拉取为倒排提取方式，即指定时间点往以前时间推，不是往后退
     * @param transferRecordParam 拉取转账记录的参数
     * @return 返回转账记录信息和最后一条记录的时间
     */
    ReturnResult<Map<String,Object>> pullTransfeInRecord(TransferRecordParam transferRecordParam);



}
