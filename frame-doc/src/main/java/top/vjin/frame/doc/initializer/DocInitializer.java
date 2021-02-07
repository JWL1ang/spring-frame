/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:31:50
 */

package top.vjin.frame.doc.initializer;

import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import top.vjin.frame.core.initializer.Initializer;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Aspect
@Component
@Setter(onMethod = @__(@Autowired))
public class DocInitializer implements Initializer {

    /** 是否允许初始化 */
    private static boolean isAllowSwaggerStart = false;
    private DocumentationPluginsBootstrapper documentationPluginsBootstrapper;

    @Override
    public void init() {
        isAllowSwaggerStart = true;
        documentationPluginsBootstrapper.start();
    }

    /**
     * 拦截 DocumentationPluginsBootstrapper.start() 方法spring启动前初始化
     */
    @Around("execution(void springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper.start())")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        return isAllowSwaggerStart ? pjp.proceed() : null;
    }
}
