/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:11:7
 */

package top.vjin.frame.core.exception;

import lombok.Getter;

/**
 * 业务异常类，业务流程中止时抛出此异常，将信息带到前端
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Getter
public class BizException extends RuntimeException {

    /** 错误代码 */
    private final String code;

    /** 消息 */
    private final String msg;

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
