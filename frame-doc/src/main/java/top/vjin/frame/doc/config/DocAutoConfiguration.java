/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:27:34
 */

package top.vjin.frame.doc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.vjin.frame.doc.service.SwaggerSdk;
import top.vjin.frame.doc.service.impl.SwaggerSdkImpl;

/**
 * 接口文档自动配置
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "frame.doc.swagger.enable", havingValue = "true")
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
//@Import({BeanValidatorPluginsConfiguration.class})
@ComponentScan(value = "top.vjin.frame.doc", lazyInit = true)
public class DocAutoConfiguration {

    private final SwaggerProperties properties;

    @Autowired
    DocAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    SwaggerSdk swaggerSdk() {
        return new SwaggerSdkImpl();
    }

    /**
     * 接口信息组件
     */
    @Bean
    @ConditionalOnBean(SwaggerSdk.class)
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(properties.getTitle())
                .description(properties.getDescription()).version(properties.getVersion()).build();
    }

    /**
     * 引入外部资源
     */
    @Bean
    @Primary
    @ConditionalOnProperty("frame.doc.api.doc.resources[0].url")
    @ConditionalOnBean(SwaggerSdk.class)
    SwaggerResourcesProvider provider() {
        return properties::getResources;
    }

    /**
     * 配置API接口文档支持组件。
     */
    @Bean
    @ConditionalOnProperty(prefix = "frame.doc.swagger", name = "enable-all-api", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(SwaggerSdk.class)
    Docket allDocket(SwaggerSdk swaggerSdk) {
        return swaggerSdk.getDocket("ALL接口", "");
    }

    /**
     * 配置swagger接口分组
     */
    @Bean
    @ConditionalOnBean(SwaggerSdk.class)
    Docket userDocket(SwaggerSdk swaggerSdk) {
        return swaggerSdk.getDocket("用户接口", "/user");
    }

    /**
     * 配置swagger接口分组
     */
    @Bean
    @ConditionalOnBean(SwaggerSdk.class)
    Docket baseDocket(SwaggerSdk swaggerSdk) {
        return swaggerSdk.getDocket("公共接口", "/base");
    }

    /**
     * 配置swagger接口分组
     */
    @Bean
    @ConditionalOnBean(SwaggerSdk.class)
    Docket adminDocket(SwaggerSdk swaggerSdk) {
        return swaggerSdk.getDocket("管理员接口", "/admin");
    }

    /**
     * 配置swagger接口分组
     */
    @Bean
    @ConditionalOnProperty(prefix = "frame.doc.swagger", name = "enable-callback-api", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(SwaggerSdk.class)
    Docket callbackDocket(SwaggerSdk swaggerSdk) {
        return swaggerSdk.getDocket("回调接口", "/callback");
    }
}
