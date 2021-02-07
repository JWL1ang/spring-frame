/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:7:7
 */

package top.vjin.frame.core.exception;

/**
 * 框架异常类，非业务流程中止时抛出此异常
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class FrameException extends RuntimeException {
    public FrameException() {
    }

    public FrameException(String message) {
        super(message);
    }

    public FrameException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameException(Throwable cause) {
        super(cause);
    }

    public FrameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 构造一个异常
     *
     * @param msg 异常消息
     * @return 构造一个框架异常
     */
    public static RuntimeException of(String msg) {
        return new FrameException(msg);
    }

    /**
     * 包装为框架异常
     *
     * @param msg 异常消息
     * @param e   异常链
     * @return 如果目标异常已是框架异常则直接返回原异常，否则包装成框架异常
     */
    public static RuntimeException of(String msg, Throwable e) {
        return (e instanceof FrameException || e instanceof BizException) ? (RuntimeException) e : new FrameException(msg, e);
    }
}
