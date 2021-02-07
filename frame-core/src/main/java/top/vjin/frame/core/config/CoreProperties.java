/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:54:21
 */

package top.vjin.frame.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 核心配置
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ConfigurationProperties("frame.core")
public class CoreProperties {

    /** 是否为调试模式 */
    private Boolean isDebug;

    /** 接口路径 */
    private String apiPath = "/api";

    /** 接口路径 */
    private String contextPath = "";

    /** 服务访问地址 */
    private String origin = "http://localhost:8080";
}
