package com.iotc.eft.dto;

import lombok.Data;

/**
 * 请求响应回来的结果
 */
@Data
public class Response<T> {

    /**
     * 错误状态
     */
    private Error error;

    /**
     * 请求成功返回的数据
     */
    private T data;
}
