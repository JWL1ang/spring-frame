/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 16:22:2
 */

package top.vjin.frame.core.config;

import java.util.Properties;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
public class CoreEnvironmentPostProcessor implements GenericEnvironmentPostProcessor {

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        //去除jackson的null
        props.put("spring.jackson.default-property-inclusion", "NON_NULL");
        //以字符串形式序列化数字
        props.put("spring.jackson.generator.write-numbers-as-strings", "true");

        //spring 任务配置
        props.put("spring.task.execution.pool.max-size", "100");
        props.put("spring.task.execution.thread-name-prefix", "@Async-");
        props.put("spring.task.scheduling.thread-name-prefix", "@Scheduled-");

        //spring消息
        props.put("spring.messages.encoding", "GBK");
        props.put("spring.messages.use-code-as-default-message", "true");

        //默认静态文件路径
        props.put("spring.resources.static-locations", "classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/, ./static");

        //默认接口路径
        props.put("frame.core.api-path", "/api");
        return props;
    }
}
