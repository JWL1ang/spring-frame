/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:26:56
 */

package top.vjin.frame.doc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ConfigurationProperties(prefix = "frame.doc.swagger")
public class SwaggerProperties {

    /** 是否启用 */
    private Boolean enable = false;

    /** 标题 */
    private String title = "接口文档";

    /** 描述 */
    private String description = "接口文档不足之处请与工作人员联系。";

    /** 接口版本 */
    private String version = "1.0";

    /** 扫描包路径，每个包以英文逗号隔开 */
    private String basePackage = "top.vjin.frame";

    /** 启用all接口 */
    private boolean enableAllApi = true;

    /** 启用回调接口 */
    private boolean enableCallbackApi = true;

    /** swagger资源链接路径 */
    private List<SwaggerResource> resources = new ArrayList<>();
}
