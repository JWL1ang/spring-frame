/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:49:25
 */

package top.vjin.frame.core.service;

/**
 * 核心服务
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface CoreSdk {

    /**
     * 是否为开发环境,判断配置:spring.profiles.active=dev
     *
     * @return 如果是返回true, 否则返回fanlse
     */
    boolean isDebug();

    /**
     * 获得服务访问地址
     *
     * @return 服务访问地址
     */
    String getOrigin();

    /**
     * 获得服务访问域名
     *
     * @return 服务访问域名
     */
    String getDomain();

    /**
     * 补全本地链接,本方法不考虑web容器和api前缀
     *
     * @param path 链接路径
     * @return 完整链接
     */
    String completeUrl(String path);

    /**
     * 补全java api链接，适应web容器(server.servlet.context-path)、api前缀(frame.core.api-path)
     *
     * @param path 链接路径
     * @return 完整链接
     */
    String completeApiUrl(String path);
}
