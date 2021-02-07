/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 15:25:29
 */

package top.vjin.frame.doc.service;

import lombok.Getter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 接口文档 sdk
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface SwaggerSdk {

    /**
     * 获得文档组件
     *
     * @param groupName 组名
     * @param path      路径
     * @return 文档组件
     */
    DocketData getDocket(String groupName, String path);

    @Getter
    class DocketData extends Docket {

        /** 拦截路径 */
        private String path;

        public DocketData(DocumentationType documentationType, String path) {
            super(documentationType);
            this.path = path;
        }
    }
}
