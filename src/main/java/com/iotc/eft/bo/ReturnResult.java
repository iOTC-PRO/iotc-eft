package com.iotc.eft.bo;


import lombok.Data;

/**
 * @description 返回状态信息
 */
@Data
public class ReturnResult<T> {

    private String code;

    private String message;

    private T data;

}
