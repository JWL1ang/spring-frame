/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:24:8
 */

package top.vjin.frame.core.exception;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.vjin.frame.core.dto.Ret;
import top.vjin.frame.core.service.CoreSdk;
import top.vjin.frame.core.utils.MessageUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理配置
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
// @RestControllerAdvice
// @Order
@Setter(onMethod = @__(@Autowired))
public class ExceptionAdvice {

    private CoreSdk coreSdk;
    private ConversionService conversionService;

    /**
     * 处理异常
     */
    @ExceptionHandler(Exception.class)
    public Ret<?> handle(Exception e) {
        if (conversionService.canConvert(e.getClass(), Ret.class)) {
            Ret<?> ret = conversionService.convert(e, Ret.class);
            if (ret != null) {
                return ret;
            }
        }
        //对于预料之外的异常先记录日志，然后响应前端服务器繁忙
        log.error("意外异常：" + e.getMessage(), e);
        return get500Ret(e);
    }

    Ret<?> get500Ret(Exception e) {
        //统一响应用户服务器繁忙
        Ret<Object> ret = getRet("500");
        if (coreSdk.isDebug()) {
            ret.setMsg(e.getClass().getSimpleName() + ":" + e.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            ret.setData(sw.toString());
        }
        return ret;
    }

    /**
     * 获取响应对象
     *
     * @param code 消息代码
     * @param args 消息变量
     * @return 前端响应对象
     */
    private Ret<Object> getRet(String code, Object... args) {
        String msg = MessageUtils.getMsg(code, args);
        return new Ret<>().setCode(code).setMsg(msg);
    }
}
