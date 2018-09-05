package com.iotc.eft.dto;

import lombok.Data;

/**
 * 返回错误状态
 */
@Data
public class Error {

    /**
     * 错误状态
     */
    private String status;
    /**
     * 错误代号
     */
    private String code;
    /**
     * 错误描述
     */
    private String description;
}
