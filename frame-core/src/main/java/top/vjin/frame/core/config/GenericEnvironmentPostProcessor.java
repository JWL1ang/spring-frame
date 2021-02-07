/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:30:25
 */

package top.vjin.frame.core.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * 默认参数设置处理接口，方便设置第三方组件的默认参数配置。
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface GenericEnvironmentPostProcessor extends EnvironmentPostProcessor {

    /**
     * 获取默认参数配置的名称，默认为：类名。
     *
     * @return 返回默认参数配置的名称。
     */
    default String getPropertiesName() {
        return getClass().getSimpleName();
    }

    /**
     * 获取是否启用默认参数设置的属性名，默认为：类名.enable。
     *
     * @return 返回是否启用默认参数设置的属性名。
     */
    default String getEnablePropertyName() {
        return getPropertiesName() + ".enable";
    }

    /**
     * 获取默认参数配置。
     *
     * @return 返回默认参数配置。
     */
    Properties getProperties();

    @Override
    default void postProcessEnvironment(ConfigurableEnvironment environment,
                                        SpringApplication application) {
        Boolean enableDefaultConfig =
                environment.getProperty(getEnablePropertyName(), Boolean.class, Boolean.TRUE);
        if (enableDefaultConfig) {
            PropertiesPropertySource propertiesPropertySource =
                    new PropertiesPropertySource(getPropertiesName(), getProperties());
            environment.getPropertySources().addLast(propertiesPropertySource);
        }
    }
}
