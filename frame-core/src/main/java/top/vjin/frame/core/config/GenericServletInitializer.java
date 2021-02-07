/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:5:11
 */

package top.vjin.frame.core.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 通用servlet初始化者,application启动类 直接继承本类,无须额外代码
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class GenericServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(getClass());
    }
}
