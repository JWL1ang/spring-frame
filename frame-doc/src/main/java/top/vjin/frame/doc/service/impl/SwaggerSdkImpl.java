/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:25:54
 */

package top.vjin.frame.doc.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import top.vjin.frame.core.config.CoreProperties;
import top.vjin.frame.doc.config.SwaggerProperties;
import top.vjin.frame.doc.service.SwaggerSdk;

import java.time.Duration;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * 接口文档 sdk
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Setter(onMethod = @__(@Autowired))
public class SwaggerSdkImpl implements SwaggerSdk {

    private ApiInfo apiInfo;
    private SwaggerProperties properties;
    private CoreProperties coreProperties;

    @Override
    public DocketData getDocket(String groupName, String path) {
        String prefix = coreProperties.getApiPath() + path;
        return (DocketData) new DocketData(DocumentationType.SWAGGER_2, path).apiInfo(apiInfo).groupName(groupName)
                .directModelSubstitute(Duration.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(YearMonth.class, String.class)
                .select()
                .apis(basePackage(properties.getBasePackage())).paths(it -> it != null && it.startsWith(prefix)).build();
    }

    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     *
     * @param basePackage 基础包
     */
    private Predicate<RequestHandler> basePackage(final String basePackage) {
        String[] packages = basePackage.split(",");
        //判断链接是否匹配,再判断包名是否匹配
        return input -> input != null && Arrays.stream(packages).anyMatch(input.declaringClass().getPackage().getName()::startsWith);
    }
}
