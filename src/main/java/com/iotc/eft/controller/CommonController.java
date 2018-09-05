package com.iotc.eft.controller;

import com.iotc.eft.bo.ReturnResult;
import com.iotc.eft.bo.TransferInfo;
import com.iotc.eft.bo.TransferRecord;
import com.iotc.eft.enums.StatusCode;
import com.iotc.eft.service.MixinApiService;
import com.iotc.eft.vo.TransferParam;
import com.iotc.eft.vo.TransferRecordParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/eft")
@Slf4j
public class CommonController {

    @Resource
    private MixinApiService mixinApiService;
    /**
     * @description 转账
     * @param transferParam 转账相关参数
     * @return 返回转账结果
     */
    @RequestMapping(value = "/transfer", method = {RequestMethod.POST})
    public ReturnResult<TransferInfo> transfer(@RequestBody TransferParam transferParam) {
        log.info("CommonController transfer,param:{}", transferParam);
        try {
            return mixinApiService.transfer(transferParam);
        } catch (Exception e) {
            log.error("CommonController transfer,exception:{}", e);
            ReturnResult<TransferInfo> returnResult = new ReturnResult<>();
            returnResult.setCode(StatusCode.ERROR.getCode());
            returnResult.setCode(StatusCode.ERROR.getMsg());
            return returnResult;
        }
    }


    /**
     * @description 拉取转账记录,转入的记录
     * @param transferRecordParam 转账参数
     * @return 返回转账记录
     */
    @RequestMapping(value = "/pullTransfeInRecord", method = {RequestMethod.POST})
    public ReturnResult<Map<String,Object>> pullTransfeInRecord(@RequestBody TransferRecordParam transferRecordParam) {
        log.info("CommonController pullTransfeInRecord,param:{}", transferRecordParam);
        try {
            return mixinApiService.pullTransfeInRecord(transferRecordParam);
        } catch (Exception e) {
            log.error("CommonController pullTransfeInRecord,exception:{}", e);
            ReturnResult<Map<String,Object>> returnResult = new ReturnResult<>();
            returnResult.setCode(StatusCode.ERROR.getCode());
            returnResult.setCode(StatusCode.ERROR.getMsg());
            return returnResult;
        }
    }



}
