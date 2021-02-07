/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:51:32
 */

package top.vjin.frame.core.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import top.vjin.frame.core.config.CoreProperties;
import top.vjin.frame.core.exception.FrameException;
import top.vjin.frame.core.service.CoreSdk;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 核心服务实现
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class CoreSdkImpl implements CoreSdk {

    @Setter(onMethod = @__(@Autowired), onParam = @__(@Value("${spring.profiles.active:''}")))
    private String active;
    @Setter(onMethod = @__(@Autowired))
    private CoreProperties coreProperties;

    @Override
    public boolean isDebug() {
        if (coreProperties.getIsDebug() != null) return coreProperties.getIsDebug();
        return "dev".equals(active) || "test".equals(active);
    }

    @Override
    public String getOrigin() {
        return coreProperties.getOrigin();
    }

    @Override
    public String getDomain() {
        try {
            URL url = new URL(getOrigin());
            return url.getHost();
        } catch (MalformedURLException e) {
            throw FrameException.of("无法从服务器origin推断域名:" + e.getMessage(), e);
        }
    }

    @Override
    public String completeUrl(String path) {
        if (path.startsWith("http:") || path.startsWith("https:")) {
            return path;
        }
        return getOrigin() + (getOrigin().endsWith("/") || path.startsWith("/") ? "" : "/") + path;
    }

    @Override
    public String completeApiUrl(String path) {
        if (path.startsWith("http:") || path.startsWith("https:")) {
            return path;
        }

        String origin = coreProperties.getOrigin();
        String contextPath = coreProperties.getContextPath();
        String apiPath = coreProperties.getApiPath();

        String url = origin + "/" + contextPath + "/" + apiPath + "/" + path;

        String front = url.substring(0, 8);
        String back = url.substring(8).replaceAll("[/\\\\]+", "/");
        return front + back;
    }
}
