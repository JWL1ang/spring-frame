/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:22:27
 */

package top.vjin.frame.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import top.vjin.frame.core.dto.Ret;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 异常视图，替代默认的页面异常视图
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
@Component("error")
@Setter(onMethod = @__(@Autowired))
public class WebErrorView implements View {

    private ErrorAttributes errorAttributes;
    private ObjectMapper objectMapper;

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //响应异常
        WebRequest webRequest = new ServletWebRequest(request);
        //获得错误属性
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        //构造响应对象
        Ret<String> error = new Ret<String>().setCode(errorMap.get("code").toString()).setMsg(errorMap.get("msg").toString());
        //响应前端
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(error));
    }
}
