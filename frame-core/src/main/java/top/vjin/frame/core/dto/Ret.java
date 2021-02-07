/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:28:6
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import top.vjin.frame.core.utils.MessageUtils;

import java.io.Serializable;

/**
 * 前端响应对象
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "接口响应对象")
public class Ret<T> implements Serializable {

    private static final long serialVersionUID = 2784192026170450950L;

    @ApiModelProperty(value = "错误代码,ok:操作成功,其它代码均为操作失败,如400:传参错误,404:路径不存在,500:未知错误,405:请求方式错误,403:无权操作,401:需要登录,409:资源状态不符。", example = "ok")
    private String code;

    @ApiModelProperty(value = "错误说明", example = "操作成功。")
    private String msg;

    @ApiModelProperty(value = "返回数据对象")
    private T data;

    /**
     * 将状态视为ok
     *
     * @param data 置入数据
     */
    public void asOk(T data) {
        setCode("ok").setMsg(MessageUtils.getMsg("ok")).setData(data);
    }

    /**
     * 返回成功响应对象
     *
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> Ret<T> ok(T data) {
        Ret<T> ret = new Ret<>();
        ret.asOk(data);
        return ret;
    }

    /**
     * 返回无数据的成功响应对象
     *
     * @return 响应对象
     */
    public static Ret<?> ok() {
        return ok(null);
    }
}
