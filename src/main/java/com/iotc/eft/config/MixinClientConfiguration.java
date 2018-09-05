package com.iotc.eft.config;

import com.iotc.eft.utils.MixinApiUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description mixin client configuration
 */
@Configuration
@Slf4j
public class MixinClientConfiguration {
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    @Resource
    private MixinConfig mixinConfig;

    private class HeaderRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate requestTemplate) {
            String url = requestTemplate.url();
            String method = requestTemplate.method();

            String query = null;
            if(StringUtils.isNotBlank(method) && "GET".equals(method)){
                query = requestTemplate.queryLine();

            }

            if(StringUtils.isNotBlank(query)){
                url = url + query;
            }

            byte[] bodyByte = requestTemplate.body();
            String body = "";
            if(bodyByte != null && bodyByte.length != 0){
                body = new String(bodyByte);
            }

            //获取签名token
            String token = MixinApiUtils.genUserToken(method,url,body,mixinConfig);
            if(StringUtils.isBlank(token)){
                return;
            }

            //添加签名token
            addHeader(requestTemplate,token);
        }
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new HeaderRequestInterceptor();
    }


    //添加header
    private void addHeader(RequestTemplate requestTemplate,String token){
        requestTemplate.header("Authorization", AUTHORIZATION_PREFIX+token);
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("accept", "text/plain");
        requestTemplate.header("connection", "Keep-Alive");
        requestTemplate.header("Content-length", "0");
        requestTemplate.header("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV11)");

    }



}
