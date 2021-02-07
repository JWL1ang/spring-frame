/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:29:39
 */

package top.vjin.frame.doc.config;

import top.vjin.frame.core.config.GenericEnvironmentPostProcessor;

import java.util.Properties;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
public class SwaggerEnvironmentPostProcessor implements GenericEnvironmentPostProcessor {

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        //去除swagger数值格式化警告
        props.put("logging.level.io.swagger.models.parameters.AbstractSerializableParameter", "ERROR");
        props.put("logging.level.springfox.documentation.spring.web.readers.operation", "WARN");
        return props;
    }
}
