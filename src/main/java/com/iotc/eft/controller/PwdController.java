package com.iotc.eft.controller;

import com.iotc.eft.constant.CommonConstant;
import com.iotc.eft.utils.AesUtils;
import com.iotc.eft.utils.CacheUtils;
import com.iotc.eft.utils.PwdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置解密密码
 */
@Controller
@RequestMapping("/local")
@Slf4j
public class PwdController {

    @Value("${mixin.privateKey}")
    private String mixin_privateKey;

    @Value("${mixin.pinToken}")
    private String mixin_pinToken;

    @RequestMapping(value = "/pwd", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String cachePwd(HttpServletRequest request,
                              HttpServletResponse response){
        String key = request.getParameter("pwd");
        String sign = request.getParameter("sign");
        //校验签名
        String signStr = AesUtils.decode(CommonConstant.SIGN_ENCODE_KEY,sign);
        if(!StringUtils.equals(signStr,CommonConstant.SIGN_CONTENT)){
            return "error";
        }

        String realPwd = PwdUtils.decrypt(CommonConstant.PWD_ENCODE_KEY,key);
        String privateKey = AesUtils.decode(realPwd,mixin_privateKey);
        String pinToken = AesUtils.decode(realPwd,mixin_pinToken);

        if(StringUtils.isBlank(privateKey) || StringUtils.isBlank(pinToken)){
            log.info("decode privateKey or pinToken is null");
            return "error";
        }
        //放到缓存中
        CacheUtils.setPrivateKey(privateKey);
        CacheUtils.setPinToken(pinToken);

        return "success";
    }


}
