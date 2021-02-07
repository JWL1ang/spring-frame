/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:15:34
 */

package top.vjin.frame.core.exception;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import top.vjin.frame.core.utils.MessageUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * web异常属性处理组件
 */
@Component
public class WebErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        //获取http状态码
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        HttpStatus status = getStatus(webRequest);

        //http状态吗加E后获得消息
        String errorCode = String.valueOf(status.value());
        String msg = MessageUtils.getMsg(errorCode);
        if (errorCode.equals(msg)) {
            //如果获取消息失败，则默认返回E500的消息
            errorCode = "500";
            msg = MessageUtils.getMsg(errorCode);
        }

        //异常属性中置入代码和消息
        errorAttributes.put("code", errorCode);
        errorAttributes.put("msg", msg);
        return errorAttributes;
    }

    private HttpStatus getStatus(RequestAttributes requestAttributes) {
        Integer statusCode = (Integer) requestAttributes.getAttribute("javax.servlet.error.status_code",
                RequestAttributes.SCOPE_REQUEST);
        return HttpStatus.valueOf(statusCode);
    }
}
